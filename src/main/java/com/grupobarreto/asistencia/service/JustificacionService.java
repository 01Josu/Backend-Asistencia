package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.JustificacionAdminDTO;
import com.grupobarreto.asistencia.dto.JustificacionRequest;
import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Justificacion;
import com.grupobarreto.asistencia.model.TipoJustificacion;
import com.grupobarreto.asistencia.repository.AsistenciaRepository;
import com.grupobarreto.asistencia.repository.JustificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.grupobarreto.asistencia.dto.JustificacionPendienteDTO;
import com.grupobarreto.asistencia.model.HorarioEmpleado;
import com.grupobarreto.asistencia.repository.HorarioEmpleadoRepository;
import java.time.Duration;

@Service
public class JustificacionService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    
    @Autowired
    private AsistenciaService asistenciaService;
    
    @Autowired
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    @Autowired
    private JustificacionRepository justificacionRepository;

    public String registrarJustificacion(JustificacionRequest request) {

        Asistencia asistencia = asistenciaRepository.findById(request.getIdAsistencia())
                .orElse(null);

        if (asistencia == null) {
            return "Asistencia no encontrada";
        }
        
        if (justificacionRepository.findByAsistencia(asistencia).isPresent()) {
            return "Ya existe una justificación para esta asistencia";
        }

        boolean esSobretiempo = necesitaJustificacionPorJornada(asistencia);

        if (!esSobretiempo) {
            return "La asistencia no tiene sobretiempo para justificar";
        }

        Justificacion j = new Justificacion();
        j.setAsistencia(asistencia);
        j.setMotivo(request.getMotivo());
        j.setAprobado(false);
        j.setTipo(TipoJustificacion.SOBRETIEMPO);

        justificacionRepository.save(j);

        return "Justificación registrada correctamente";
    }

    public String aprobarJustificacion(Long idJustificacion, boolean aprobado) {
        Justificacion j = justificacionRepository.findById(idJustificacion).orElse(null);
        if (j == null) return "Justificación no encontrada";

        j.setAprobado(aprobado);
        justificacionRepository.save(j);
        return "Justificación actualizada";
    }

    public Page<JustificacionAdminDTO> listar(Pageable pageable) {
        return justificacionRepository.listarParaAdmin(pageable);
    }
    
    public List<JustificacionPendienteDTO> obtenerPendientes(Long idEmpleado) {

        List<Asistencia> asistencias = asistenciaRepository.buscarAsistenciasSinJustificar(idEmpleado);
        List<JustificacionPendienteDTO> pendientes = new ArrayList<>();

        for (Asistencia a : asistencias) {

            if (necesitaJustificacionPorJornada(a)) {
                // Calcular horaSalidaEsperada para DTO
                LocalTime horaSalidaEsperada;
                if (a.getFecha().getDayOfWeek() == DayOfWeek.SATURDAY) {
                    horaSalidaEsperada = LocalTime.of(13, 0);
                } else {
                    HorarioEmpleado he = horarioEmpleadoRepository
                            .findHorarioVigentePorFecha(a.getEmpleado(), a.getFecha())
                            .orElse(null);
                    if (he == null || he.getHorario() == null) continue;
                    horaSalidaEsperada = he.getHorario().getHoraSalida();
                }

                pendientes.add(new JustificacionPendienteDTO(
                        a.getIdAsistencia(),
                        a.getFecha(),
                        a.getHoraSalidaReal(),
                        horaSalidaEsperada
                ));
            }
        }

        return pendientes;
    }
    
    private boolean necesitaJustificacionPorJornada(Asistencia a) {
        if (a.getHoraEntradaReal() == null || a.getHoraSalidaReal() == null) return false;

        LocalTime horaEntradaEsperada;
        LocalTime horaSalidaEsperada;

        if (a.getFecha().getDayOfWeek() == DayOfWeek.SATURDAY) {
            horaEntradaEsperada = LocalTime.of(9, 0);
            horaSalidaEsperada = LocalTime.of(13, 0);
        } else {
            HorarioEmpleado he = horarioEmpleadoRepository
                    .findHorarioVigentePorFecha(a.getEmpleado(), a.getFecha())
                    .orElse(null);
            if (he == null || he.getHorario() == null) return false;
            horaEntradaEsperada = he.getHorario().getHoraEntrada();
            horaSalidaEsperada = he.getHorario().getHoraSalida();
        }

        Duration horasTrabajadas = Duration.between(a.getHoraEntradaReal(), a.getHoraSalidaReal());
        Duration jornadaEsperada = Duration.between(horaEntradaEsperada, horaSalidaEsperada);

        // Excede 30 minutos exactos (en segundos)
        return horasTrabajadas.minus(jornadaEsperada).getSeconds() > 30 * 60;
    }
}

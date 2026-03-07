package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.JustificacionAdminDTO;
import com.grupobarreto.asistencia.dto.JustificacionRequest;
import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Horario;
import com.grupobarreto.asistencia.model.HorarioEmpleado;
import com.grupobarreto.asistencia.model.Justificacion;
import com.grupobarreto.asistencia.model.TipoJustificacion;
import com.grupobarreto.asistencia.repository.AsistenciaRepository;
import com.grupobarreto.asistencia.repository.HorarioEmpleadoRepository;
import com.grupobarreto.asistencia.repository.JustificacionRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JustificacionService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private JustificacionRepository justificacionRepository;
    
    @Autowired
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    private static final int MINUTOS_SOBRETIEMPO = 30;

    public String registrarJustificacion(JustificacionRequest request) {

        Asistencia asistencia = asistenciaRepository.findById(request.getIdAsistencia())
                .orElse(null);

        if (asistencia == null) {
            return "Asistencia no encontrada";
        }

        // Evitar duplicados
        if (justificacionRepository.findByAsistencia(asistencia).isPresent()) {
            return "Ya existe una justificación para esta asistencia";
        }

        boolean esSobretiempo = esSobretiempo(asistencia);

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

    private boolean esSobretiempo(Asistencia asistencia) {

        if (asistencia.getHoraSalidaReal() == null) {
            return false;
        }

        HorarioEmpleado horarioEmpleado = horarioEmpleadoRepository
                .findHorarioVigentePorFecha(
                        asistencia.getEmpleado(),
                        asistencia.getFecha()
                )
                .orElse(null);

        if (horarioEmpleado == null || horarioEmpleado.getHorario() == null) {
            return false;
        }

        Horario horario = horarioEmpleado.getHorario();

        LocalTime horaBase;

        // 👇 Ahora sí consideramos sábado
        if (asistencia.getFecha().getDayOfWeek() == DayOfWeek.SATURDAY) {
            horaBase = LocalTime.of(13, 0);
        } else {
            horaBase = horario.getHoraSalida();
        }

        return asistencia.getHoraSalidaReal()
                .isAfter(horaBase.plusMinutes(MINUTOS_SOBRETIEMPO));
    }

}

package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.JustificacionRequest;
import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Horario;
import com.grupobarreto.asistencia.model.HorarioEmpleado;
import com.grupobarreto.asistencia.model.Justificacion;
import com.grupobarreto.asistencia.model.TipoJustificacion;
import com.grupobarreto.asistencia.repository.AsistenciaRepository;
import com.grupobarreto.asistencia.repository.HorarioEmpleadoRepository;
import com.grupobarreto.asistencia.repository.JustificacionRepository;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

        boolean esTardanza =
                "TARDANZA".equalsIgnoreCase(asistencia.getEstadoAsistencia());

        boolean esSobretiempo = esSobretiempo(asistencia);

        if (!esTardanza && !esSobretiempo) {
            return "La asistencia no cumple condiciones para justificar";
        }

        TipoJustificacion tipo;
        if (esSobretiempo) {
            tipo = TipoJustificacion.SOBRETIEMPO;
        } else {
            tipo = TipoJustificacion.TARDANZA;
        }

        Justificacion j = new Justificacion();
        j.setAsistencia(asistencia);
        j.setMotivo(request.getMotivo());
        j.setAprobado(false);
        j.setTipo(tipo);

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

    public List<Justificacion> listar() {
        return justificacionRepository.findAll();
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

        LocalTime horaSalidaHorario = horario.getHoraSalida();

        return asistencia.getHoraSalidaReal()
                .isAfter(horaSalidaHorario.plusMinutes(MINUTOS_SOBRETIEMPO));
    }

}

package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.JustificacionRequest;
import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Justificacion;
import com.grupobarreto.asistencia.repository.AsistenciaRepository;
import com.grupobarreto.asistencia.repository.JustificacionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JustificacionService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private JustificacionRepository justificacionRepository;

    public String registrarJustificacion(JustificacionRequest request) {

        Asistencia asistencia = asistenciaRepository.findById(request.getIdAsistencia())
                .orElse(null);

        if (asistencia == null) {
            return "Asistencia no encontrada";
        }

        
        if (!"TARDANZA".equalsIgnoreCase(asistencia.getEstadoAsistencia())) {
            return "Solo se permite registrar justificación si la asistencia está en estado 'TARDANZA'";
        }

        
        if (justificacionRepository.findByAsistencia(asistencia).isPresent()) {
            return "Ya existe una justificación para esta asistencia";
        }

        Justificacion j = new Justificacion();
        j.setAsistencia(asistencia);
        j.setMotivo(request.getMotivo());
        j.setAprobado(false);

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

}

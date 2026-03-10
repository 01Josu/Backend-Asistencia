package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.JustificacionAdminDTO;
import com.grupobarreto.asistencia.dto.JustificacionRequest;
import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Justificacion;
import com.grupobarreto.asistencia.model.TipoJustificacion;
import com.grupobarreto.asistencia.repository.AsistenciaRepository;
import com.grupobarreto.asistencia.repository.HorarioEmpleadoRepository;
import com.grupobarreto.asistencia.repository.JustificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JustificacionService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    
    @Autowired
    private AsistenciaService asistenciaService;

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

        boolean esSobretiempo = asistenciaService.esSobretiempo(asistencia);

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
}

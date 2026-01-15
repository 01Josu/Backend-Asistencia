package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Justificacion;
import com.grupobarreto.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JustificacionRepository extends JpaRepository<Justificacion, Long> {
    Optional<Justificacion> findByAsistencia(Asistencia asistencia);
}

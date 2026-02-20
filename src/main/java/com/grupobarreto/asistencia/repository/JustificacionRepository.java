package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Justificacion;
import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.dto.JustificacionAdminDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JustificacionRepository extends JpaRepository<Justificacion, Long> {

    Optional<Justificacion> findByAsistencia(Asistencia asistencia);

    @Query(value = """
        SELECT new com.grupobarreto.asistencia.dto.JustificacionAdminDTO(
            j.idJustificacion,
            e.nombres,
            e.apellidos,
            a.fecha,
            j.tipo,
            j.motivo,
            j.aprobado
        )
        FROM Justificacion j
        JOIN j.asistencia a
        JOIN a.empleado e
        """,
        countQuery = """
        SELECT COUNT(j)
        FROM Justificacion j
        JOIN j.asistencia a
        JOIN a.empleado e
        """
    )
    Page<JustificacionAdminDTO> listarParaAdmin(Pageable pageable);
}
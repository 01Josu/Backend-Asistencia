package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    Optional<Asistencia> findByEmpleadoAndFecha(Empleado empleado, LocalDate fecha);

    List<Asistencia> findAllByEmpleado(Empleado empleado);

    @Query("""
        SELECT a
        FROM Asistencia a
        LEFT JOIN Justificacion j ON j.asistencia = a
        WHERE a.empleado.idEmpleado = :idEmpleado
        AND a.horaSalidaReal IS NOT NULL
        AND j.idJustificacion IS NULL
        ORDER BY a.fecha DESC
    """)
    List<Asistencia> buscarAsistenciasSinJustificar(Long idEmpleado);

}
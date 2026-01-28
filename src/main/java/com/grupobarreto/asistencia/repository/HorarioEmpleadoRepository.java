package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.HorarioEmpleado;
import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.model.Horario;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HorarioEmpleadoRepository
        extends JpaRepository<HorarioEmpleado, Long> {

    Optional<HorarioEmpleado> findByEmpleadoAndFechaFinIsNull(
            Empleado empleado
    );

    boolean existsByHorario(Horario horario);

    boolean existsByEmpleadoAndHorarioAndFechaFinIsNull(
            Empleado empleado,
            Horario horario
    );

    @Query("""
        SELECT he FROM HorarioEmpleado he
        WHERE he.empleado = :empleado
          AND he.fechaInicio <= :fecha
          AND (he.fechaFin IS NULL OR he.fechaFin >= :fecha)
    """)
    Optional<HorarioEmpleado> findHorarioVigentePorFecha(
            @Param("empleado") Empleado empleado,
            @Param("fecha") LocalDate fecha
    );
}

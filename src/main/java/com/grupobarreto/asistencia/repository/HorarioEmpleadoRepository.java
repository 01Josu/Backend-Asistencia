package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.HorarioEmpleado;
import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

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
}

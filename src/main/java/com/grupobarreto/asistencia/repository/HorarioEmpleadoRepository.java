package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.HorarioEmpleado;
import com.grupobarreto.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HorarioEmpleadoRepository extends JpaRepository<HorarioEmpleado, Long> {
    Optional<HorarioEmpleado> findByEmpleadoAndFechaFinIsNull(Empleado empleado);
}


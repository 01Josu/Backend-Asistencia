package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    Optional<Asistencia> findByEmpleadoAndFecha(Empleado empleado, LocalDate fecha);

    List<Asistencia> findAllByEmpleado(Empleado empleado);
}

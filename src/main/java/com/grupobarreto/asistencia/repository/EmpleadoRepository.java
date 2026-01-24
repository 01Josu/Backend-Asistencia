package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    boolean existsByCodigoEmpleado(String codigoEmpleado);
    List<Empleado> findByActivoTrue();
}

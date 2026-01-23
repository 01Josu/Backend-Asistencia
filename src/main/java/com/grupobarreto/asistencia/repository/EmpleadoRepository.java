package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    boolean existsByCodigoEmpleado(String codigoEmpleado);
}

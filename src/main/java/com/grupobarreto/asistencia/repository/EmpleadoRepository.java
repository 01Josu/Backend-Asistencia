package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}


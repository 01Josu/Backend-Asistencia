package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
}

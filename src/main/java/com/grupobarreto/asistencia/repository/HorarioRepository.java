package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByActivoTrue();

    boolean existsByIdHorarioAndActivoTrue(Long idHorario);
}

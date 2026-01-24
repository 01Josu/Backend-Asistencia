package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {

    List<Sede> findByActivoTrue();
}
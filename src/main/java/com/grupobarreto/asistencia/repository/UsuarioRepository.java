package com.grupobarreto.asistencia.repository;

import com.grupobarreto.asistencia.model.RolUsuario;
import com.grupobarreto.asistencia.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario);

    boolean existsByUsuario(String usuario);

    boolean existsByRol(RolUsuario rol);
    List<Usuario> findByUsuarioContainingIgnoreCase(String usuario);
    
}

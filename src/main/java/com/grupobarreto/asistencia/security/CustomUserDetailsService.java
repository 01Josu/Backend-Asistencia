package com.grupobarreto.asistencia.security;

import com.grupobarreto.asistencia.model.Usuario;
import com.grupobarreto.asistencia.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import org.springframework.security.authentication.DisabledException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log =
            LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        log.info("Intentando autenticar usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> {
                    log.warn("Usuario NO encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        log.debug("Usuario encontrado: id={}, activo={}, rol={}",
                usuario.getIdUsuario(),
                usuario.isActivo(),
                usuario.getRol()
        );

        if (!usuario.isActivo()) {
            log.warn("Usuario desactivado: {}", username);
            throw new DisabledException("Usuario desactivado");
        }

        String role = "ROLE_" + usuario.getRol().name();
        log.info("Asignando autoridad {} al usuario {}", role, username);

        return new User(
                usuario.getUsuario(),
                usuario.getPasswordHash(),
                true,   // enabled (ya validado)
                true,
                true,
                true,
                Collections.singletonList(
                        new SimpleGrantedAuthority(role)
                )
        );
    }
}

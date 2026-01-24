package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.model.Usuario;
import com.grupobarreto.asistencia.repository.UsuarioRepository;
import com.grupobarreto.asistencia.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void logout(String token) {

        Long idUsuario = jwtUtil.extractUserId(token);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setSessionVersion(usuario.getSessionVersion() + 1);

        usuarioRepository.save(usuario);
    }
}

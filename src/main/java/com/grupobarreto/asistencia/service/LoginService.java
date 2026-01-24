package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.LoginRequest;
import com.grupobarreto.asistencia.dto.LoginResponse;
import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.model.Usuario;
import com.grupobarreto.asistencia.repository.UsuarioRepository;
import com.grupobarreto.asistencia.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {

        if (request.getUsuario() == null || request.getUsuario().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {

            return new LoginResponse(
                    false,
                    "Usuario y contraseña obligatorios",
                    null, null, null, null, null, null
            );
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsuario(),
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            return new LoginResponse(
                    false,
                    "Usuario o contraseña incorrectos",
                    null, null, null, null, null, null
            );
        }

        Usuario usuario = usuarioRepository.findByUsuario(request.getUsuario())
                .orElse(null);

        if (usuario == null || !usuario.isActivo()) {
            return new LoginResponse(
                    false,
                    "Usuario inactivo",
                    null, null, null, null, null, null
            );
        }

        Empleado emp = usuario.getEmpleado();
        if (emp != null && !emp.isActivo()) {
            return new LoginResponse(
                    false,
                    "Empleado inactivo",
                    null, null, null, null, null, null
            );
        }

        usuario.setSessionVersion(usuario.getSessionVersion() + 1);
        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(
                usuario.getUsuario(),
                usuario.getRol().name(),
                usuario.getIdUsuario(),
                usuario.getSessionVersion()
        );

        return new LoginResponse(
                true,
                "Login exitoso",
                usuario.getIdUsuario(),
                emp != null ? emp.getIdEmpleado() : null,
                emp != null ? emp.getNombres() : null,
                emp != null ? emp.getApellidos() : null,
                usuario.getRol().name(),
                token
        );
    }
}

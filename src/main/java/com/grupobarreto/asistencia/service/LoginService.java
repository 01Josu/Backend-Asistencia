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

        // 1️⃣ Validación básica
        if (request.getUsuario() == null || request.getUsuario().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {

            return new LoginResponse(false, "Usuario y contraseña obligatorios",
                    null, null, null, null, null, null);
        }

        // 2️⃣ Autenticación real
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsuario(),
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            return new LoginResponse(false, "Usuario o contraseña incorrectos",
                    null, null, null, null, null, null);
        }

        // 3️⃣ Obtener usuario autenticado
        Usuario usuario = usuarioRepository.findByUsuario(request.getUsuario())
                .orElse(null);

        if (usuario == null || !usuario.isActivo()) {
            return new LoginResponse(false, "Usuario inactivo",
                    null, null, null, null, null, null);
        }

        // 4️⃣ Validación de empleado
        Empleado emp = usuario.getEmpleado();
        if (emp != null && !emp.isActivo()) {
            return new LoginResponse(false, "Empleado inactivo",
                    null, null, null, null, null, null);
        }

        // 5️⃣ JWT CON idUsuario
        String token = jwtUtil.generateToken(
                usuario.getUsuario(),
                usuario.getRol().name(),
                usuario.getIdUsuario()
        );

        // 6️⃣ Respuesta
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
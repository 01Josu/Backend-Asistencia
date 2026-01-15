package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.LoginRequest;
import com.grupobarreto.asistencia.dto.LoginResponse;

import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.model.Usuario;
import com.grupobarreto.asistencia.repository.EmpleadoRepository;
import com.grupobarreto.asistencia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public LoginResponse login(LoginRequest request) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        if (request.getUsuario() == null || request.getUsuario().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {

            return new LoginResponse(false, "Usuario y contraseña obligatorios",
                    null, null, null, null);
        }

        Usuario usuario = usuarioRepository.findByUsuario(request.getUsuario())
                .orElse(null);

        if (usuario == null) {
            return new LoginResponse(false, "Usuario incorrecto",
                    null, null, null, null);
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            return new LoginResponse(false, "Contraseña incorrecta", null, null, null, null);
        }

        Empleado emp = usuario.getEmpleado();

        if (!emp.isActivo()) {
            return new LoginResponse(false, "Empleado inactivo",
                    null, null, null, null);
        }

        return new LoginResponse(
                true,
                "Login exitoso",
                usuario.getIdUsuario(),
                emp.getIdEmpleado(),
                emp.getNombres(),
                emp.getApellidos()
        );
    }
}


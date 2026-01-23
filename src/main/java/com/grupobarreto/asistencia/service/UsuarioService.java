package com.grupobarreto.asistencia.service;

import com.grupobarreto.asistencia.dto.UsuarioRequest;
import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.model.RolUsuario;
import com.grupobarreto.asistencia.model.Usuario;
import com.grupobarreto.asistencia.repository.EmpleadoRepository;
import com.grupobarreto.asistencia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= CREAR USUARIO =================
    public Usuario crearUsuario(Long idEmpleado, String usuario, String password, String rol) {

        if (usuario == null || usuario.isBlank()) {
            throw new RuntimeException("El usuario es obligatorio");
        }

        if (password == null || password.isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        // 1️⃣ Validar usuario único
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new RuntimeException("El usuario ya existe");
        }

        // 2️⃣ Empleado opcional (ADMIN no necesita empleado)
        Empleado emp = null;
        if (idEmpleado != null) {
            emp = empleadoRepository.findById(idEmpleado)
                    .orElseThrow(() -> new RuntimeException("Empleado no existe"));
        }

        // 3️⃣ Validar rol
        RolUsuario rolEnum;
        try {
            rolEnum = RolUsuario.valueOf(rol);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido");
        }

        // 4️⃣ Crear usuario
        Usuario u = new Usuario();
        u.setEmpleado(emp);
        u.setUsuario(usuario);
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setRol(rolEnum);
        u.setActivo(true);

        return usuarioRepository.save(u);
    }

    // ================= LISTAR =================
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // ================= BUSCAR =================
    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // ================= ACTUALIZAR =================
    public Usuario actualizar(Long id, UsuarioRequest request) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getUsuario() != null && !request.getUsuario().isBlank()) {
            u.setUsuario(request.getUsuario());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRol() != null) {
            try {
                u.setRol(RolUsuario.valueOf(request.getRol()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Rol no válido");
            }
        }

        if (request.getActivo() != null) {
            u.setActivo(request.getActivo());
        }

        return usuarioRepository.save(u);
    }

    // ================= ELIMINAR =================
    public boolean eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) return false;
        usuarioRepository.deleteById(id);
        return true;
    }
}

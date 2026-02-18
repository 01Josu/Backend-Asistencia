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

    public Usuario crearUsuario(Long idEmpleado, String usuario, String password, String rol) {

        if (usuario == null || usuario.isBlank()) {
            throw new RuntimeException("El usuario es obligatorio");
        }

        if (password == null || password.isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new RuntimeException("El usuario ya existe");
        }

        Empleado emp = null;
        if (idEmpleado != null) {
            emp = empleadoRepository.findById(idEmpleado)
                    .orElseThrow(() -> new RuntimeException("Empleado no existe"));
        }

        RolUsuario rolEnum;
        try {
            rolEnum = RolUsuario.valueOf(rol);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido");
        }

        Usuario u = new Usuario();
        u.setEmpleado(emp);
        u.setUsuario(usuario);
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setRol(rolEnum);
        u.setActivo(true);

        return usuarioRepository.save(u);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario actualizar(Long id, UsuarioRequest request) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getUsuario() != null && !request.getUsuario().isBlank()) {

            if (!u.getUsuario().equals(request.getUsuario())) {

                boolean existe = usuarioRepository.existsByUsuario(request.getUsuario());

                if (existe) {
                    throw new RuntimeException("Ya existe un usuario con ese nombre");
                }
            }

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

    public boolean eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) return false;
        usuarioRepository.deleteById(id);
        return true;
    }
    
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByUsuarioContainingIgnoreCase(nombre);
    }
}

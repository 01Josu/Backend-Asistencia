package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.UsuarioBootstrapRequest;
import com.grupobarreto.asistencia.model.RolUsuario;
import com.grupobarreto.asistencia.model.Usuario;
import com.grupobarreto.asistencia.repository.UsuarioRepository;
import com.grupobarreto.asistencia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bootstrap")
public class BootstrapController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/admin")
    public Usuario crearAdminInicial(@RequestBody UsuarioBootstrapRequest request) {

        
        boolean existeAdmin = usuarioRepository.existsByRol(RolUsuario.ADMIN);
        if (existeAdmin) {
            throw new RuntimeException("Ya existe un administrador. Bootstrap bloqueado.");
        }

        
        return usuarioService.crearUsuario(
                null,
                request.getUsuario(),
                request.getPassword(),
                "ADMIN"
        );
    }
}

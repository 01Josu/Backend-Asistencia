package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.service.EmpleadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private static final Logger log =
            LoggerFactory.getLogger(EmpleadoController.class);

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<Empleado> listar() {
        log.info("Listando empleados");
        return empleadoService.listar();
    }

    @GetMapping("/{id}")
    public Empleado obtener(@PathVariable Long id) {
        log.info("Buscando empleado con id={}", id);
        return empleadoService.buscar(id);
    }

    // SOLO PARA PRUEBA DE SEGURIDAD (bien hecho 👍)
    @GetMapping("/test")
    public String test(Authentication auth) {
        log.info("Usuario autenticado: {}, roles={}",
                auth.getName(),
                auth.getAuthorities()
        );
        return auth.getAuthorities().toString();
    }
}

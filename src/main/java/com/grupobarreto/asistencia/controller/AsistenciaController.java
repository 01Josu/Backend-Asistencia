package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.model.RegistroAsistencia;
import com.grupobarreto.asistencia.model.RegistroResponse;
import com.grupobarreto.asistencia.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    // Recibe JSON en el body
    @PostMapping("/marcarEntrada")
    public RegistroResponse marcarEntrada(@RequestBody RegistroAsistencia payload) {
        return asistenciaService.marcarEntrada(payload.getIdEmpleado(), payload.getNombre());
    }

    // Recibe JSON en el body
    @PostMapping("/marcarSalida")
    public RegistroResponse marcarSalida(@RequestBody RegistroAsistencia payload) {
        return asistenciaService.marcarSalida(payload.getIdEmpleado(), payload.getNombre());
    }
}


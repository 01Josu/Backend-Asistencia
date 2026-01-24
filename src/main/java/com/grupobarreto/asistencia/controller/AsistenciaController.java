package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.MarcarAsistenciaRequest;
import com.grupobarreto.asistencia.dto.MarcarAsistenciaResponse;
import com.grupobarreto.asistencia.model.Asistencia;
import com.grupobarreto.asistencia.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping("/entrada")
    public MarcarAsistenciaResponse marcarEntrada(@RequestBody MarcarAsistenciaRequest request) {
        return asistenciaService.marcarEntrada(request);
    }

    @PostMapping("/salida")
    public MarcarAsistenciaResponse marcarSalida(@RequestBody MarcarAsistenciaRequest request) {
        return asistenciaService.marcarSalida(request);
    }
}


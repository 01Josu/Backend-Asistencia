package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.JustificacionRequest;
import com.grupobarreto.asistencia.service.JustificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/justificacion")
@CrossOrigin(origins = "http://localhost:4200")
public class JustificacionController {

    @Autowired
    private JustificacionService justificacionService;

    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody JustificacionRequest request) {
        String respuesta = justificacionService.registrarJustificacion(request);
        return ResponseEntity.ok(respuesta);
    }
}

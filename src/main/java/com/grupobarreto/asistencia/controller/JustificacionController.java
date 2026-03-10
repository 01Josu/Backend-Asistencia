package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.JustificacionRequest;
import com.grupobarreto.asistencia.dto.JustificacionPendienteDTO;
import com.grupobarreto.asistencia.service.JustificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/justificacion")
public class JustificacionController {

    @Autowired
    private JustificacionService justificacionService;

    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody JustificacionRequest request) {
        String respuesta = justificacionService.registrarJustificacion(request);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/pendientes/{idEmpleado}")
    public ResponseEntity<List<JustificacionPendienteDTO>> obtenerPendientes(
            @PathVariable Long idEmpleado) {

        List<JustificacionPendienteDTO> pendientes =
                justificacionService.obtenerPendientes(idEmpleado);

        return ResponseEntity.ok(pendientes);
    }
}
package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    @PostMapping
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token no encontrado");
        }

        String token = header.substring(7);

        logoutService.logout(token);

        return ResponseEntity.ok("Sesión cerrada correctamente");
    }
}

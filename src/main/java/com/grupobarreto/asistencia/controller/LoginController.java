package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.model.Empleado;
import com.grupobarreto.asistencia.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    // Recibe JSON
    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload) {
        String usuario = payload.get("usuario");
        String contraseña = payload.get("contraseña");

        Map<String, Object> response = new HashMap<>();

        try {
            Empleado empleado = loginService.login(usuario, contraseña);
            if (empleado != null) {
                response.put("success", true);
                response.put("mensaje", "Login exitoso");
                response.put("empleado", empleado); // devuelve el objeto empleado
            } else {
                response.put("success", false);
                response.put("mensaje", "Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}

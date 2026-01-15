package com.grupobarreto.asistencia.controller;

import com.grupobarreto.asistencia.dto.LoginRequest;
import com.grupobarreto.asistencia.dto.LoginResponse;
import com.grupobarreto.asistencia.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request);
        return ResponseEntity.ok(response);
    }
}

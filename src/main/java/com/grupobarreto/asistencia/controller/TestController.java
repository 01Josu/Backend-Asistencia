package com.grupobarreto.asistencia.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test/auth")
    public String testAuth(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}

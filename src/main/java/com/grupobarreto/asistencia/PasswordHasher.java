package com.grupobarreto.asistencia;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasher {

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String passwordPlano = "abcd"; 
        String hash = encoder.encode(passwordPlano);

        System.out.println("Hash generado: " + hash);
    }
}

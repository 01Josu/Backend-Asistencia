package com.grupobarreto.asistencia.config;

import com.grupobarreto.asistencia.security.JwtAuthenticationFilter;
import com.grupobarreto.asistencia.security.CustomUserDetailsService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private static final Logger log =
            LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("PasswordEncoder BCrypt configurado");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("AuthenticationProvider configurado con CustomUserDetailsService");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        log.info("AuthenticationManager inicializado");
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("Configurando SecurityFilterChain");

        http
            .csrf(csrf -> {
                csrf.disable();
                log.debug("CSRF deshabilitado (JWT Stateless)");
            })
            .cors(cors -> {
                cors.configurationSource(corsConfigurationSource());
                log.debug("CORS configurado");
            })
            .sessionManagement(sm -> {
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                log.debug("SessionManagement STATELESS");
            })
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> {
                log.info("Configurando reglas de autorización");
                auth
                    .requestMatchers("/api/login/**", "/api/bootstrap/**", "/error").permitAll()
                    .requestMatchers("/api/logout").authenticated()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().authenticated();
            })
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        log.info("JwtAuthenticationFilter registrado antes de UsernamePasswordAuthenticationFilter");

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        log.info("Configurando CORS");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
            "http://localhost:4200",
            "https://frontend-asistencia-2fqr.vercel.app",
            "https://frontend-asistencia-2fqr-git-master-in-whre-s-projects.vercel.app",
            "https://frontend-asistencia-2fqr-d12p1czft-in-whre-s-projects.vercel.app"
        ));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        log.debug("CORS origins permitidos: {}", config.getAllowedOrigins());

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

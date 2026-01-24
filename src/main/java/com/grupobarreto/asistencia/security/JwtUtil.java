package com.grupobarreto.asistencia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger log =
            LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        log.info("JWT inicializado (expiración={} ms)", expirationMs);
    }

    public String generateToken(
            String username,
            String rol,
            Long idUsuario,
            Integer sessionVersion
    ) {

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMs);

        log.info(
            "Generando token para usuario={} rol={} sessionVersion={} expira={}",
            username, rol, sessionVersion, expiration
        );

        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .claim("idUsuario", idUsuario)
                .claim("sessionVersion", sessionVersion)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;

        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Token no soportado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Token mal formado: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("Firma JWT inválida");
        } catch (IllegalArgumentException e) {
            log.warn("Token vacío o nulo");
        } catch (JwtException e) {
            log.warn("Error JWT genérico: {}", e.getMessage());
        }

        return false;
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("rol", String.class);
    }

    public Long extractUserId(String token) {
        return parseClaims(token).get("idUsuario", Long.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    public Integer extractSessionVersion(String token) {
        return parseClaims(token).get("sessionVersion", Integer.class);
    }

}

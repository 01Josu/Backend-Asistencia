package com.grupobarreto.asistencia.security;

import com.grupobarreto.asistencia.model.Usuario;
import com.grupobarreto.asistencia.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/login")
            || path.startsWith("/api/bootstrap")
            || path.startsWith("/error");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.debug("➡️ {} {}", request.getMethod(), request.getRequestURI());
        log.debug("Authorization header: {}", request.getHeader("Authorization"));

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.debug("No Authorization header o no empieza con Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);


            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("Token inválido o expirado");
            }

            log.debug("Token válido");
            Long idUsuario = jwtUtil.extractUserId(token);
            log.debug("idUsuario extraído: {}", idUsuario);
            Integer tokenSessionVersion =
                    jwtUtil.extractSessionVersion(token);

 
            Usuario usuario = usuarioRepository
                    .findById(idUsuario)
                    .orElseThrow(() ->
                            new RuntimeException("Usuario no existe"));

            
            if (!usuario.isActivo()) {
                throw new RuntimeException("Usuario inactivo");
            }


            if (!tokenSessionVersion.equals(usuario.getSessionVersion())) {
                throw new RuntimeException("Sesión inválida o cerrada");
            }


            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(
                            usuario.getUsuario()
                    );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.warn("JWT rechazado: {}", e.getMessage(), e);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "error": "No autorizado",
                  "message": "Sesión expirada, cerrada o inválida"
                }
            """);
        }
    }
}



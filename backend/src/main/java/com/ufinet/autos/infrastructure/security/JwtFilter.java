package com.ufinet.autos.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        // 1. Si no hay cabecera o no empieza con "Bearer ", simplemente continúa.
        //    Spring Security decidirá más adelante si la ruta es pública o necesita autenticación.
        //    ESTA ES LA LÓGICA CORRECTA Y DEFINITIVA.
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Si hay un token, lo procesamos.
        final String token = header.substring(7);
        try {
            Map<String, Object> claims = jwtUtil.validateToken(token);
            String username = (String) claims.get("sub");
            Number userIdNum = (Number) claims.get("userId");
            Long userId = userIdNum != null ? userIdNum.longValue() : null;

            // Si el token es válido, establecemos la autenticación en el contexto
            JwtUserDetails principal = new JwtUserDetails(username, userId);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // Si el token es inválido (expirado, malformado), simplemente limpiamos el contexto.
            // No enviamos un error aquí. Dejamos que Spring Security y nuestro
            // CustomAuthenticationEntryPoint manejen la respuesta si la ruta era protegida.
            SecurityContextHolder.clearContext();
        }

        // 3. Continuamos con el resto de los filtros.
        filterChain.doFilter(request, response);
    }
}


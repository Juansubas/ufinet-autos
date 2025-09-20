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

/**
 * Filtro de seguridad que se ejecuta una vez por cada petición HTTP.
 * Su función es interceptar las peticiones, buscar un JWT en la cabecera "Authorization",
 * validarlo y establecer la autenticación del usuario en el contexto de seguridad de Spring.
 */
public class JwtFilter extends OncePerRequestFilter {

    // Dependencia a nuestra utilidad de JWT para poder validar los tokens.
    private final JwtUtil jwtUtil;

    /**
     * Constructor que permite la inyección de la utilidad JwtUtil.
     * @param jwtUtil La instancia de JwtUtil gestionada por Spring.
     */
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Lógica principal del filtro que se ejecuta en cada petición.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Obtener la cabecera "Authorization" de la petición.
        final String header = request.getHeader("Authorization");

        // 2. Verificar si la cabecera existe y tiene el formato correcto ("Bearer <token>").
        // Si no existe o no tiene el formato, se cede el control al siguiente filtro en la cadena.
        // Spring Security se encargará de denegar el acceso más adelante si la ruta era protegida.
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Importante salir del método aquí.
        }

        // 3. Extraer el token de la cabecera (quitando el prefijo "Bearer ").
        final String token = header.substring(7);

        try {
            // 4. Validar el token usando nuestra utilidad JwtUtil.
            // Si el token es inválido (firma, expiración), lanzará una excepción.
            Map<String, Object> claims = jwtUtil.validateToken(token);
            String username = (String) claims.get("sub");
            Long userId = ((Number) claims.get("userId")).longValue();

            // 5. Si el token es válido, crear un objeto de autenticación.
            // Usamos un objeto JwtUserDetails que contiene la información principal del usuario.
            JwtUserDetails principal = new JwtUserDetails(username, userId);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            principal, // El objeto que representa al usuario autenticado.
                            null,      // Las credenciales (no se usan con JWT).
                            Collections.emptyList() // Los roles/autoridades (en este caso, sin roles).
                    );

            // 6. Establecer la autenticación en el contexto de seguridad de Spring.
            // Esto es lo que efectivamente "inicia sesión" al usuario para la petición actual.
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // Si jwtUtil.validateToken() lanza una excepción, significa que el token es inválido.
            // En este caso, simplemente limpiamos el contexto de seguridad para asegurar que no haya
            // ninguna autenticación residual de peticiones anteriores.
            SecurityContextHolder.clearContext();
        }

        // 7. Ceder el control al siguiente filtro en la cadena de seguridad.
        // La petición continuará su camino, ahora con un contexto de seguridad establecido (o limpio).
        filterChain.doFilter(request, response);
    }
}
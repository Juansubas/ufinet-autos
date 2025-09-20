package com.ufinet.autos.infrastructure.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador personalizado para errores de autenticación (AuthenticationException).
 * Se activa cuando un usuario no autenticado intenta acceder a un recurso protegido.
 * Devuelve una respuesta JSON estandarizada con el código de estado 401 Unauthorized.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Utilidad para convertir el objeto Map a una cadena JSON.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Este método se invoca cuando la autenticación falla.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 1. Establecer el código de estado HTTP a 401 Unauthorized.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 2. Definir el tipo de contenido de la respuesta como JSON.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 3. Crear el cuerpo de la respuesta JSON.
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "Se requiere autenticación para acceder a este recurso."); // Mensaje claro para el cliente.
        body.put("path", request.getServletPath()); // La ruta a la que se intentó acceder.
        body.put("timestamp", Instant.now().toString()); // Marca de tiempo del error.

        // 4. Escribir el mapa como un JSON en el cuerpo de la respuesta HTTP.
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
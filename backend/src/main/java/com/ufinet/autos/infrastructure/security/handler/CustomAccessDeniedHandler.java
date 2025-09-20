package com.ufinet.autos.infrastructure.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador personalizado para errores de autorización (AccessDeniedException).
 * Se activa cuando un usuario autenticado intenta acceder a un recurso
 * para el cual no tiene los permisos necesarios (ej: un usuario normal intentando acceder a una ruta de administrador).
 * Devuelve una respuesta JSON estandarizada con el código de estado 403 Forbidden.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // Utilidad para convertir el objeto Map a una cadena JSON.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Este método se invoca cuando la autorización es denegada.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 1. Establecer el código de estado HTTP a 403 Forbidden.
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 2. Definir el tipo de contenido de la respuesta como JSON.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 3. Crear el cuerpo de la respuesta JSON.
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_FORBIDDEN);
        body.put("error", "Forbidden");
        body.put("message", "No tienes permiso para acceder a este recurso."); // Mensaje claro.
        body.put("path", request.getServletPath());
        body.put("timestamp", Instant.now().toString());

        // 4. Escribir el mapa como un JSON en el cuerpo de la respuesta HTTP.
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
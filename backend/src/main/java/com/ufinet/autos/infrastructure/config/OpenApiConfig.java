package com.ufinet.autos.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
// 1. Anotación principal para la configuración general de OpenAPI
@OpenAPIDefinition(
        info = @Info(
                title = "Ufinet Autos API",
                version = "1.0",
                description = "Documentación de la API para el proyecto Ufinet Autos"
        ),
        // 2. Aplica el requisito de seguridad de forma GLOBAL a todos los endpoints
        security = @SecurityRequirement(name = "bearerAuth")
)
// 3. Define el esquema de seguridad que usaremos
@SecurityScheme(
        name = "bearerAuth", // Un nombre para referenciar este esquema. Debe coincidir con el de SecurityRequirement
        type = SecuritySchemeType.HTTP, // El tipo de seguridad es HTTP
        scheme = "bearer", // El esquema específico es "Bearer"
        bearerFormat = "JWT" // Un hint que indica que el formato del token es JWT
)
public class OpenApiConfig {
    // Esta clase puede estar vacía, ya que toda la configuración se hace con las anotaciones de arriba.
}

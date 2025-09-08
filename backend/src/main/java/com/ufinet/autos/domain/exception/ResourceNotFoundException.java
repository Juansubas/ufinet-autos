package com.ufinet.autos.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para indicar que un recurso solicitado no fue encontrado.
 * Al anotarla con @ResponseStatus(HttpStatus.NOT_FOUND), Spring Boot automáticamente
 * devolverá un código de estado 404 cuando esta excepción no sea capturada.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

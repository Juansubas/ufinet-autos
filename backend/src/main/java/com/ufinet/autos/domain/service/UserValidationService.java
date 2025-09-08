package com.ufinet.autos.domain.service;

import com.ufinet.autos.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidationService {

    /**
     * Valida las reglas de negocio invariantes para la creación de un usuario.
     *
     * @param user El usuario a validar.
     */
    public void validateForCreation(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        // Regla de negocio: El nombre de usuario no puede ser una palabra reservada.
        if (user.getUsername().equalsIgnoreCase("admin") || user.getUsername().equalsIgnoreCase("root")) {
            throw new IllegalArgumentException("El nombre de usuario no es válido.");
        }

        // Regla de negocio: La contraseña debe tener una longitud mínima.
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
    }
}

package com.ufinet.autos.domain.service;

import com.ufinet.autos.domain.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationServiceTest {

    private final UserValidationService validationService = new UserValidationService();

    @Test
    void shouldThrowExceptionIfUsernameIsEmpty() {
        User user = new User();
        user.setUsername("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateForCreation(user)
        );

        assertEquals("El nombre de usuario no puede estar vacío.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfUsernameIsAdmin() {
        User user = new User();
        user.setUsername("admin");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateForCreation(user)
        );

        assertEquals("El nombre de usuario no puede ser 'admin' o 'root'.", exception.getMessage());
    }

    @Test
    void shouldPassWithValidUsername() {
        User user = new User();
        user.setUsername("juan123");

        assertDoesNotThrow(() -> validationService.validateForCreation(user));
    }
}

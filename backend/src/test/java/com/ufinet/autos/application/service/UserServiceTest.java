package com.ufinet.autos.application.service;

import com.ufinet.autos.application.port.out.UserRepositoryPort;
import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.service.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserValidationService userValidationService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks y las inyecciones antes de cada prueba
        MockitoAnnotations.openMocks(this);

        // Creamos un usuario base para usar en las pruebas
        user = new User();
        user.setUsername("juan");
        user.setPassword("aValidPassword123");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // 1. Arrange (Configurar)
        // Simula que el username NO existe
        when(userRepositoryPort.findByUsername("juan")).thenReturn(Optional.empty());
        // Simula la codificación de la contraseña
        when(passwordEncoder.encode("aValidPassword123")).thenReturn("encodedPassword");
        // Simula la acción de guardado
        when(userRepositoryPort.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Por defecto, el mock de userValidationService.validateForCreation no hace nada (void),
        // lo cual es perfecto para el caso de éxito.

        // 2. Act (Actuar)
        User savedUser = userService.register(user);

        // 3. Assert (Verificar)
        assertNotNull(savedUser);
        assertEquals("juan", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());

        // Verifica que la validación y el guardado fueron llamados
        verify(userValidationService, times(1)).validateForCreation(user);
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionIfUsernameExists() {
        // 1. Arrange
        // Simula que el username SÍ existe
        when(userRepositoryPort.findByUsername("juan")).thenReturn(Optional.of(user));

        // 2. Act & 3. Assert
        // Verifica que se lanza la excepción correcta con el mensaje correcto
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(user)
        );

        assertEquals("El nombre de usuario 'juan' ya está en uso.", exception.getMessage());

        // Verifica que el método de guardado NUNCA fue llamado
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenDomainValidationFails() {
        // 1. Arrange
        // Simula que el servicio de validación lanza una excepción
        doThrow(new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres."))
                .when(userValidationService).validateForCreation(any(User.class));

        // 2. Act & 3. Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(user)
        );

        assertEquals("La contraseña debe tener al menos 8 caracteres.", exception.getMessage());

        // Verifica que NUNCA se interactuó con el repositorio si la validación falló (Fail-Fast)
        verify(userRepositoryPort, never()).findByUsername(anyString());
        verify(userRepositoryPort, never()).save(any(User.class));
    }
}

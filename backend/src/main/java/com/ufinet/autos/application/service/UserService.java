package com.ufinet.autos.application.service;

import com.ufinet.autos.application.port.in.UserUseCase;
import com.ufinet.autos.application.port.out.UserRepositoryPort;
import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.service.UserValidationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;

    public UserService(UserRepositoryPort userRepositoryPort,
                       PasswordEncoder passwordEncoder,
                       UserValidationService userValidationService) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
    }

    @Override
    public User register(User user) {
        // 1. Validar las reglas de negocio del dominio primero (Fail-Fast)
        userValidationService.validateForCreation(user);

        // 2. Validar reglas del caso de uso (depende de la infraestructura)
        Optional<User> exists = userRepositoryPort.findByUsername(user.getUsername());
        if (exists.isPresent()) {
            // Se lanza IllegalArgumentException para que el handler lo convierta en 400 Bad Request
            throw new IllegalArgumentException("El nombre de usuario '" + user.getUsername() + "' ya está en uso.");
        }

        Optional<User> existsEmail = userRepositoryPort.findByEmail(user.getEmail());
        if (existsEmail.isPresent()) {
            // Se lanza IllegalArgumentException para que el handler lo convierta en 400 Bad Request
            throw new IllegalArgumentException("El correo de usuario '" + user.getEmail() + "' ya está en uso.");
        }

        // 3. Procesar y persistir
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepositoryPort.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepositoryPort.findByUsername(username);
    }
}

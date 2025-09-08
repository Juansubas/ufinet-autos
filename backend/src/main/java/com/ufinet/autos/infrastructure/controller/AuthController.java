package com.ufinet.autos.infrastructure.controller;

import com.ufinet.autos.application.dto.JwtResponse;
import com.ufinet.autos.application.dto.LoginRequest;
import com.ufinet.autos.application.port.in.UserUseCase;
import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.infrastructure.controller.dto.UserRequest;
import com.ufinet.autos.infrastructure.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserUseCase userUseCase,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userUseCase = userUseCase;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        User user = request.toDomain();
        User saved = userUseCase.register(user);
        saved.setPassword(null);

        return ResponseEntity.status(201).body(
                Map.of(
                        "message", "User registered successfully",
                        "user", saved
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userUseCase.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Invalid credentials")
            );
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getId());

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "token", token
                )
        );
    }
}

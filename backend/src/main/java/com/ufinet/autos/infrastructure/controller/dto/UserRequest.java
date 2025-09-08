package com.ufinet.autos.infrastructure.controller.dto;

import com.ufinet.autos.domain.model.User;

public class UserRequest {

    private String username;
    private String email;
    private String password;

    // Constructor vacío (necesario para Spring)
    public UserRequest() {
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Método para convertir a dominio
    public User toDomain() {
        return new User(null, username, email, password);
    }
}

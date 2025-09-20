package com.ufinet.autos.infrastructure.persistence.entity;

import com.ufinet.autos.domain.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public UserJpaEntity() {
    }

    public UserJpaEntity(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Mapping helpers
    public static UserJpaEntity fromDomain(User u) {
        return new UserJpaEntity(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getPassword()
        );
    }

    public User toDomain() {
        return new User(this.id, this.username, this.email, this.password);
    }

    // Getters y Setters manuales
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}

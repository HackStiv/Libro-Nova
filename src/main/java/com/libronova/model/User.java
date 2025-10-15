package com.libronova.model;

import java.time.LocalDateTime;

/**
 * Clase que representa un usuario del sistema LibroNova
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; // ADMIN, LIBRARIAN, MEMBER
    private LocalDateTime createdAt;
    private boolean active;

    // Constructores
    public User() {}

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Verifica si el usuario es administrador
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    /**
     * Verifica si el usuario es bibliotecario
     */
    public boolean isLibrarian() {
        return "LIBRARIAN".equals(role);
    }

    /**
     * Verifica si el usuario es miembro
     */
    public boolean isMember() {
        return "MEMBER".equals(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", active=" + active +
                '}';
    }
}

package com.example.eadapp.data;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String role;

    public LoginRequest(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters if needed
}
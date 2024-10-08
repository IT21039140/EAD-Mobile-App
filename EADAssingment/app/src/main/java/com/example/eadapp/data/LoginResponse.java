package com.example.eadapp.data;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String refreshToken;

    // Getters and setters

}

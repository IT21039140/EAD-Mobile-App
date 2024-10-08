package com.example.eadapp.data;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private String role;
    private String address;
    private long mobileNumber;

    public RegisterRequest(String email, String username, String password, String role, String address, long mobileNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.mobileNumber = mobileNumber;
    }

    // Getters and setters (if needed)
}

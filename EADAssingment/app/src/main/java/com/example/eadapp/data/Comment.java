package com.example.eadapp.data;

import lombok.Data;

@Data
public class Comment {
    private String id;
    private String customerId;
    private String commentText;
    private int rating;

    // Getters and setters...
}


package com.example.eadapp.data;

import java.util.List;

import lombok.Data;

@Data
public class Vendor {
    private String id;
    private String email;
    private String username;
    private String address;
    private VendorDetails vendorDetails;

    // Getters and setters...

    @Data
    public static class VendorDetails {
        private int averageRating;
        private List<Comment> comments;

        // Getters and setters...
    }
}

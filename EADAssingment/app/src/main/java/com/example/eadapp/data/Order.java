package com.example.eadapp.data;

import java.util.List;

import lombok.Data;

@Data
public class Order {
    private String id;
    private String customerId;
    private String customerEmail;
    private String vendorId;
    private List<String> productIds;
    private String status;
    private String orderDate;
    private String deliveryDate;
    private String note;

    // Getters and Setters for all fields
}


package com.example.eadapp.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
    private String customerId;
    private String customerEmail;
    private String vendorId;
    private List<String> productIds;
    private String status;
    private String orderDate;
    private String deliveryDate;
    private String note;
}

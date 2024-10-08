package com.example.eadapp.data;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private String id;
    private String productID;
    private String name;
    private String description;
    private double price;
    private String category;
    private String image;  // This is the Base64 image string from the API
    private Bitmap bitmapImage;  // This is the decoded Bitmap image
    private String vendorName;
    private String vendorId;
    private int quantity;
    private int status;
}

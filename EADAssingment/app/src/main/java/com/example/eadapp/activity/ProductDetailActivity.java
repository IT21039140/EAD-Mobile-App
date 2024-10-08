package com.example.eadapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eadapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productName, productPrice, productDescription, productQuantity, vendorName;
    private ImageView productImage; // ImageView for product image
    private String vendorId;  // Declare vendorId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_description);
        productQuantity = findViewById(R.id.product_quantity);
        vendorName = findViewById(R.id.vendor_name);
        productImage = findViewById(R.id.product_image); // Initialize ImageView

        // Get the data passed from the MainActivity
        if (getIntent() != null) {
            productName.setText(getIntent().getStringExtra("product_name"));
            productPrice.setText(String.format("$%.2f", getIntent().getDoubleExtra("product_price", 0)));
            productDescription.setText(getIntent().getStringExtra("product_description"));
            productQuantity.setText("Available Quantity: " + getIntent().getIntExtra("product_quantity", 0));
            vendorName.setText("Vendor: " + getIntent().getStringExtra("vendor_name"));

            // Get vendorId from intent
            vendorId = getIntent().getStringExtra("vendor_id");

            // Get the image file path from the intent and display the image
            String imagePath = getIntent().getStringExtra("product_image_path");
            if (imagePath != null) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    productImage.setImageBitmap(bitmap);  // Set image from file path
                }
            }
        }

        // Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                Intent homeIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_orders) {
                Intent ordersIntent = new Intent(ProductDetailActivity.this, OrderTrackingActivity.class);
                startActivity(ordersIntent);
                return true;
            }

            return false;
        });

        // Set up OnClickListener for vendor name
        vendorName.setOnClickListener(v -> {
            // Navigate to VendorFeedbackActivity and pass vendorId
            Intent feedbackIntent = new Intent(ProductDetailActivity.this, VendorFeedbackActivity.class);
            feedbackIntent.putExtra("vendor_id", vendorId);
            feedbackIntent.putExtra("vendor_name", vendorName.getText().toString());
            startActivity(feedbackIntent);
        });
    }
}

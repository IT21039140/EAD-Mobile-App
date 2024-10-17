package com.example.eadapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eadapp.R;
import com.example.eadapp.data.CartManager;
import com.example.eadapp.data.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productName, productPrice, productDescription, productQuantity, vendorName;
    private ImageView productImage;
    private Button addToCartButton;
    private Product currentProduct;

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
        productImage = findViewById(R.id.product_image);
        addToCartButton = findViewById(R.id.add_to_cart_button);

        // Get the data passed from ProductAdapter
        if (getIntent() != null) {
            String id = getIntent().getStringExtra("id");
            String productId = getIntent().getStringExtra("productId");
            String name = getIntent().getStringExtra("product_name");
            double price = getIntent().getDoubleExtra("product_price", 0);
            String description = getIntent().getStringExtra("product_description");
            int quantity = getIntent().getIntExtra("product_quantity", 0);
            String vendor = getIntent().getStringExtra("vendor_name");
            String vendorId = getIntent().getStringExtra("vendor_id");
            String base64Image = getIntent().getStringExtra("image");

            productName.setText(name);
            productPrice.setText(String.format("$%.2f", price));
            productDescription.setText(description);
            productQuantity.setText("Available Quantity: " + quantity);
            vendorName.setText("Vendor: " + vendor);

            // Decode the Base64 image and display it
            if (base64Image != null && !base64Image.isEmpty()) {
                Bitmap decodedImage = decodeBase64Image(base64Image);
                if (decodedImage != null) {
                    productImage.setImageBitmap(decodedImage);
                }
            }

            // Create the current product object
            currentProduct = new Product(id, productId, name, description, price, null, base64Image, decodeBase64Image(base64Image), vendor, vendorId, quantity, 1);
        }

        // Handle Add to Cart button click
        addToCartButton.setOnClickListener(v -> {
            CartManager.getInstance().addProductToCart(currentProduct);
            Toast.makeText(ProductDetailActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();
        });

        // Set up bottom navigation
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

        // Handle vendor name click
        vendorName.setOnClickListener(v -> {
            Intent feedbackIntent = new Intent(ProductDetailActivity.this, VendorFeedbackActivity.class);
            feedbackIntent.putExtra("vendor_id", getIntent().getStringExtra("vendor_id"));
            feedbackIntent.putExtra("vendor_name", vendorName.getText().toString());
            startActivity(feedbackIntent);
        });
    }

    // Helper method to decode Base64 string into Bitmap
    private Bitmap decodeBase64Image(String base64Image) {
        try {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}

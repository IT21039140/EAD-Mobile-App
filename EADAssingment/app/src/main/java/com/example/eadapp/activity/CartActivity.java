package com.example.eadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadapp.R;
import com.example.eadapp.adapters.ProductAdapterImpl;
import com.example.eadapp.data.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity {

    private RecyclerView cartRecyclerView;
    private ProductAdapterImpl productAdapter;
    private List<Product> cartProductList;
    private Button checkoutButton, browseMoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);  // Ensure this matches the XML layout for the cart activity

        // Initialize RecyclerView
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize dummy cart product list
        cartProductList = new ArrayList<>();
        cartProductList.add(new Product(
                "1",
                "PI001",
                "Laptop",
                "High-performance laptop",
                999.99,
                "Electronics",
                null,
                null,
                "Vendor A",
                "V001",
                10,
                1
        ));//        cartProductList.add(new Product("Smartphone", "002","Latest Android smartphone", "asdfsdfsdf",599.99, "Vendor B", "1234xyz", null, "Vendor A"));

        cartProductList.add(new Product(
                "2",
                "PI002",
                "Smartphone",
                "Latest Android smartphone",
                599.99,
                "Electronics",
                null,
                null,
                "Vendor B",
                "V002",
                5,
                1
        ));

        // Set up adapter for RecyclerView
        productAdapter = new ProductAdapterImpl(this,cartProductList);
        cartRecyclerView.setAdapter(productAdapter);

        // Initialize checkout button
        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(view -> {
            // Simulate checkout process
            Toast.makeText(CartActivity.this, "Purchase successful!", Toast.LENGTH_SHORT).show();
        });

        // Initialize browse more items button
        browseMoreButton = findViewById(R.id.browse_more_button);
        browseMoreButton.setOnClickListener(view -> {
            // Navigate to MainActivity (Browse more items)
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                Intent homeIntent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                return true;
            } else if (itemId == R.id.navigation_orders) {
                Intent ordersIntent = new Intent(CartActivity.this, OrderTrackingActivity.class);
                startActivity(ordersIntent);
                return true;
            }

            return false;
        });
    }
}

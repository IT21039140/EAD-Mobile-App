package com.example.eadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadapp.R;
import com.example.eadapp.adapters.ProductAdapterImpl;
import com.example.eadapp.data.CartManager;
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
        setContentView(R.layout.cart_activity);

        // Initialize RecyclerView
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get cart items from CartManager
        cartProductList = CartManager.getInstance().getCartItems();

        // Set up adapter for RecyclerView
        productAdapter = new ProductAdapterImpl(this, cartProductList);
        cartRecyclerView.setAdapter(productAdapter);

        // Initialize checkout button
        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(view -> {
            // Navigate to Payment Gateway Activity and pass product details
            Intent paymentIntent = new Intent(CartActivity.this, PaymentGatewayActivity.class);
            ArrayList<Product> cartProductsArrayList = new ArrayList<>(cartProductList);

            // Pass product details to the PaymentGatewayActivity
            paymentIntent.putParcelableArrayListExtra("cart_items", new ArrayList<>(cartProductList));  // Pass product list
            startActivity(paymentIntent);
        });

        // Initialize browse more items button (if needed)
//        browseMoreButton = findViewById(R.id.browse_more_button);
//        browseMoreButton.setOnClickListener(view -> {
//            // Navigate to MainActivity (Browse more items)
//            Intent intent = new Intent(CartActivity.this, MainActivity.class);
//            startActivity(intent);
//        });

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

package com.example.eadapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadapp.R;
import com.example.eadapp.adapters.ProductAdapterImpl;
import com.example.eadapp.api.UserApi;
import com.example.eadapp.data.Product;
import com.example.eadapp.network.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ProductAdapterImpl productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);

        // Set GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Initialize empty product list
        productList = new ArrayList<>();

        // Set up adapter for RecyclerView
        productAdapter = new ProductAdapterImpl(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Fetch products from API
        fetchProducts();

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_orders) {
                Intent ordersIntent = new Intent(MainActivity.this, OrderTrackingActivity.class);
                startActivity(ordersIntent);
                return true;
            }

            return false;
        });
    }

    // Method to fetch products from API
    private void fetchProducts() {
        UserApi productApi = RetrofitClient.getClient().create(UserApi.class);
        Call<List<Product>> call = productApi.getAllProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();

                    // Convert Base64 image string to Bitmap for each product
                    for (Product product : productList) {
                        if (product.getImage() != null && !product.getImage().isEmpty()) {
                            product.setBitmapImage(decodeBase64Image(product.getImage()));
                        }
                    }

                    // Update adapter with new product list
                    productAdapter.setProductList(productList);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Method to decode Base64 string into Bitmap
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

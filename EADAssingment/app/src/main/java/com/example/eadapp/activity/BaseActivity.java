package com.example.eadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eadapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_nav);

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Handle navigation item clicks using the new setOnItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_orders) {
                Intent ordersIntent = new Intent(this, OrderTrackingActivity.class);
                startActivity(ordersIntent);
                return true;
            }

            return false;
        });
    }
}

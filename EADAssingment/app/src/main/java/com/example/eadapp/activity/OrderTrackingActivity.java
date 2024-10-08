package com.example.eadapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadapp.R;
import com.example.eadapp.adapters.OrderAdapter;
import com.example.eadapp.api.UserApi;
import com.example.eadapp.data.Order;
import com.example.eadapp.network.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTrackingActivity extends BaseActivity {

    private TextView orderStatus, orderStatusSubtext;
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;

    // SharedPreferences file name
    private static final String SHARED_PREFS = "user_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String USER_ID_KEY = "userId";

    private static final String TAG = "OrderTrackingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate your own layout inside the container
        getLayoutInflater().inflate(R.layout.order_tracking_activity, findViewById(R.id.container));

        // Initialize Views
        orderStatus = findViewById(R.id.order_status_header);  // Updated ID
        orderRecyclerView = findViewById(R.id.order_recycler_view);

        // Set up RecyclerView
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with empty list
        orderAdapter = new OrderAdapter(new ArrayList<>());
        orderRecyclerView.setAdapter(orderAdapter);

        // Fetch user ID and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, null);
        String userId = sharedPreferences.getString(USER_ID_KEY, null);

        Log.i(TAG, "token | userId :: " + token + " | " + userId);

        if (token != null && userId != null) {
            // Fetch orders when the activity is started
            fetchOrders(token, userId);
        } else {
            Toast.makeText(OrderTrackingActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        // Set up the bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                Intent ordersIntent = new Intent(OrderTrackingActivity.this, MainActivity.class);
                startActivity(ordersIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(OrderTrackingActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_orders) {
                // Already on Order Tracking, no need to navigate
                return true;
            }

            return false;
        });
    }

    // Method to fetch orders
    private void fetchOrders(String token, String userId) {
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
        Call<List<Order>> call = userApi.getOrdersByCustomer("Bearer " + token, userId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.i(TAG, "response ::: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "response ::: success " + response.body());

                    // Filter out "Delivered" orders
                    List<Order> orders = new ArrayList<>();
                    for (Order order : response.body()) {
                        Log.i(TAG, "order :::: " + order);
//                        if (!"Delivered".equals(order.getStatus())) {
                            orders.add(order);
//                        }
                    }

                    // Update the RecyclerView with the filtered orders
                    orderAdapter.updateOrders(orders);

                    // Update the UI if no orders are available
                    if (orders.isEmpty()) {
                        orderStatus.setText("No current orders.");
                    }
                } else {
                    Toast.makeText(OrderTrackingActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrderTrackingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

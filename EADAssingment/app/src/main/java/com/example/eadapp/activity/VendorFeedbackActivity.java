package com.example.eadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadapp.R;
import com.example.eadapp.adapters.CommentAdapter;
import com.example.eadapp.api.UserApi;
import com.example.eadapp.data.Comment;
import com.example.eadapp.data.Vendor;
import com.example.eadapp.network.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorFeedbackActivity extends BaseActivity {

    private RatingBar vendorRatingBar;
    private EditText vendorCommentEditText;
    private Button submitFeedbackButton;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;

    private TextView vendorUsername, vendorEmail, vendorAddress, vendorAverageRating;

    private String vendorId; // Vendor ID passed from previous activity
    private String token;    // Bearer token for authorization
    private String userId;   // Customer ID retrieved from SharedPreferences

    // SharedPreferences file name
    private static final String SHARED_PREFS = "user_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String USER_ID_KEY = "userId";

    private static final String TAG = "VendorFeedbackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_feedback_activity);

        // Retrieve the vendor ID and name passed from the intent
        vendorId = getIntent().getStringExtra("vendor_id");
        vendorId="66faae2e1a174fd88fee8e5d";
        String vendorName = getIntent().getStringExtra("vendor_name");

        // Retrieve token and userId (CustomerId) from SharedPreferences
        token = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(TOKEN_KEY, null);
        userId = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(USER_ID_KEY, null);

        // Initialize views
        TextView vendorNameTextView = findViewById(R.id.vendor_name_textview);
        vendorUsername = findViewById(R.id.vendor_username);
        vendorEmail = findViewById(R.id.vendor_email);
        vendorAddress = findViewById(R.id.vendor_address);
        vendorAverageRating = findViewById(R.id.vendor_average_rating);
        vendorRatingBar = findViewById(R.id.vendor_rating);
        vendorCommentEditText = findViewById(R.id.vendor_comment);
        submitFeedbackButton = findViewById(R.id.submit_feedback);
        commentRecyclerView = findViewById(R.id.comment_recycler_view);

        // Set up RecyclerView for comments
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        commentRecyclerView.setAdapter(commentAdapter);

        // Display the vendor's name
        if (vendorNameTextView != null && vendorName != null) {
            vendorNameTextView.setText(vendorName);
        }

        // Fetch vendor details including comments
        fetchVendorDetails(vendorId);

        // Handle feedback submission
        submitFeedbackButton.setOnClickListener(view -> {
            float rating = vendorRatingBar.getRating();
            String commentText = vendorCommentEditText.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(VendorFeedbackActivity.this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
            } else if (commentText.isEmpty()) {
                Toast.makeText(VendorFeedbackActivity.this, "Please add a comment.", Toast.LENGTH_SHORT).show();
            } else {
                // Submit feedback with userId (CustomerId)
                submitVendorFeedback(vendorId, userId, rating, commentText);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                Intent homeIntent = new Intent(VendorFeedbackActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(VendorFeedbackActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_orders) {
                Intent ordersIntent = new Intent(VendorFeedbackActivity.this, OrderTrackingActivity.class);
                startActivity(ordersIntent);
                return true;
            }

            return false;
        });
    }

    // Method to fetch vendor details and comments
    private void fetchVendorDetails(String vendorId) {
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
        Call<Vendor> call = userApi.getVendorDetails(vendorId);

        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Vendor vendor = response.body();
                    displayVendorDetails(vendor);
                } else {
                    Toast.makeText(VendorFeedbackActivity.this, "Failed to load vendor details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Toast.makeText(VendorFeedbackActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display vendor details on the UI
    private void displayVendorDetails(Vendor vendor) {
        vendorUsername.setText(vendor.getUsername());
        vendorEmail.setText(vendor.getEmail());
        vendorAddress.setText(vendor.getAddress());
        vendorAverageRating.setText(String.valueOf(vendor.getVendorDetails().getAverageRating()));

        // Set comments in the RecyclerView
        List<Comment> comments = vendor.getVendorDetails().getComments();
        commentAdapter.setComments(comments); // Update RecyclerView with comments
    }

    // Method to submit vendor feedback
    private void submitVendorFeedback(String vendorId, String userId, float rating, String commentText) {
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);

        // Create the payload
        JsonObject feedbackPayload = new JsonObject();
        feedbackPayload.addProperty("CustomerId", userId);  // Pass the userId as CustomerId
        feedbackPayload.addProperty("CommentText", commentText);
        feedbackPayload.addProperty("Rating", rating);

        Log.i(TAG, "feedbackPayload " +feedbackPayload);
        Log.i(TAG, "token " +token);


        Call<JsonObject> call = userApi.addVendorComment("Bearer " + token, vendorId, feedbackPayload);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "feedbackPayload " +response.body());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(VendorFeedbackActivity.this, "Feedback submitted successfully.", Toast.LENGTH_LONG).show();
                    // Update UI with new feedback
                    fetchVendorDetails(vendorId);  // Refresh vendor details after submitting feedback
                } else {
                    Toast.makeText(VendorFeedbackActivity.this, "Failed to submit feedback.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(VendorFeedbackActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}

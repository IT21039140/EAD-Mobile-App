package com.example.eadapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eadapp.R;
import com.example.eadapp.api.UserApi;
//import com.example.eadapp.api.UserApi;
import com.example.eadapp.data.OrderRequest;
import com.example.eadapp.data.OrderResponse;
import com.example.eadapp.data.Product;
import com.example.eadapp.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentGatewayActivity extends AppCompatActivity {

    private Button backToCartButton, payButton;
    private EditText cardNumber, cardExpiry, cardCvc;
    private List<Product> cartProducts;  // List to hold cart products
    private static final String TAG = "PaymentGatewayActivity";

    private static final String SHARED_PREFS = "user_prefs";

    private static final String TOKEN_KEY = "token";
    private static final String EMAIL_KEY = "email";
    private static final String USER_ID_KEY = "userId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_gateway_activity);

        // Initialize views
        backToCartButton = findViewById(R.id.back_to_cart_button);
        payButton = findViewById(R.id.pay_button);
        cardNumber = findViewById(R.id.card_number);
        cardExpiry = findViewById(R.id.card_expiry);
        cardCvc = findViewById(R.id.card_cvc);

        // Retrieve cart items passed from CartActivity
        cartProducts = getIntent().getParcelableArrayListExtra("cart_items");

        Log.i(TAG, "cartProducts::: " + cartProducts);

        if (cartProducts != null) {
            Toast.makeText(this, "Processing " + cartProducts.size() + " items", Toast.LENGTH_SHORT).show();
        }

        // Back to Cart Button Click Listener
        backToCartButton.setOnClickListener(view -> {
            // Navigate back to CartActivity
            Intent intent = new Intent(PaymentGatewayActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        });

        // Pay Button Click Listener
        payButton.setOnClickListener(view -> {
            String cardNum = cardNumber.getText().toString();
            String expiry = cardExpiry.getText().toString();
            String cvc = cardCvc.getText().toString();

            // Simple validation (you can add more checks)
            if (cardNum.isEmpty() || expiry.isEmpty() || cvc.isEmpty()) {
                Toast.makeText(PaymentGatewayActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Simulate payment process and then call the API to place the order
                processPayment();
            }
        });
    }

    private void processPayment() {
        Toast.makeText(PaymentGatewayActivity.this, "Processing Payment...", Toast.LENGTH_SHORT).show();

        // Retrieve the token, customerId, and customerEmail from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");  // Retrieve the token
        String customerId = sharedPreferences.getString(USER_ID_KEY, "");
        String customerEmail = sharedPreferences.getString(EMAIL_KEY, "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Error: Missing authentication token", Toast.LENGTH_SHORT).show();
            return; // Exit early if the token is missing
        }

        String vendorId = "VEND98765"; // This could be dynamic if necessary
        List<String> productIds = new ArrayList<>();
        for (Product product : cartProducts) {
            productIds.add(product.getProductID());
        }

        // Get current date and future delivery date
        String orderDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        String deliveryDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(cal.getTime());

        // Create the order request object
        OrderRequest orderRequest = new OrderRequest(customerId, customerEmail, vendorId, productIds, "Processing", orderDate, deliveryDate, "Arrive at 4 p.m");

        // Make the API call to place the order with token
        UserApi orderApi = RetrofitClient.getClient().create(UserApi.class);
        Call<OrderResponse> call = orderApi.placeOrder("Bearer " + token, orderRequest);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(PaymentGatewayActivity.this, "Payment Successful! Order placed.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PaymentGatewayActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PaymentGatewayActivity.this, "Failed to place order.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(PaymentGatewayActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API Error: ", t);
            }
        });
    }

}

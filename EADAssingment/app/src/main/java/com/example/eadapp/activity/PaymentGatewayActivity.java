package com.example.eadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eadapp.R;

public class PaymentGatewayActivity extends AppCompatActivity {

    private Button backToCartButton, payButton;
    private EditText cardNumber, cardExpiry, cardCvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_gateway_activity);  // Ensure this matches your payment gateway XML file

        // Initialize views
        backToCartButton = findViewById(R.id.back_to_cart_button);
        payButton = findViewById(R.id.pay_button);
        cardNumber = findViewById(R.id.card_number);
        cardExpiry = findViewById(R.id.card_expiry);
        cardCvc = findViewById(R.id.card_cvc);

        // Back to Cart Button Click Listener
        backToCartButton.setOnClickListener(view -> {
            // Navigate back to CartActivity
            Intent intent = new Intent(PaymentGatewayActivity.this, CartActivity.class);
            startActivity(intent);
            finish();  // Close this activity so it's removed from the back stack
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
                // Simulate payment process
                processPayment();

                // After successful payment, redirect to another activity or show success message
                // e.g., you could redirect to an OrderConfirmationActivity
            }
        });
    }

    private void processPayment() {
        // Mock payment process - show success/failure message
        Toast.makeText(PaymentGatewayActivity.this, "Payment Successful", Toast.LENGTH_LONG).show();

        // After payment is successful, navigate back to order confirmation or main activity
        Intent intent = new Intent(PaymentGatewayActivity.this, OrderConfirmationActivity.class);
        startActivity(intent);
        finish();
    }
}


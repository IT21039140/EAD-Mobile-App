package com.example.eadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eadapp.R;

public class OrderConfirmationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirmation_activity);

        Button continueShopping = findViewById(R.id.continue_shopping_button);
        continueShopping.setOnClickListener(view -> {
            // Navigate back to the main activity (product browsing)
//            Intent intent = new Intent(OrderConfirmationActivity.this, MainActivity.class);
//            startActivity(intent);
            finish();
        });
    }
}


package com.example.eadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eadapp.R;
import com.example.eadapp.api.UserApi;
import com.example.eadapp.data.RegisterRequest;
import com.example.eadapp.network.RetrofitClient;
import com.google.gson.annotations.JsonAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText, addressEditText, mobileNumberEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);  // Ensure this is your register XML file

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.password2);
        mobileNumberEditText = findViewById(R.id.mobileNumber);
        addressEditText = findViewById(R.id.address);
        registerButton = findViewById(R.id.register_button);

        // Register button click listener
        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String mobileNumber = mobileNumberEditText.getText().toString().trim();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !address.isEmpty() && !mobileNumber.isEmpty()) {
                // Proceed with registration
                registerUser(username, email, password, address, Long.parseLong(mobileNumber));
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String username, String email, String password, String address, long mobileNumber) {
        // Create Retrofit client and API service
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);

        // The role is set as "Customer" by default
        RegisterRequest registerRequest = new RegisterRequest(email, username, password, "Customer", address, mobileNumber);

        // Make the API call
        Call<String> call = userApi.registerUser(registerRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()||response.code()==200) {  // Check for success (2xx status code)
                    // Show the success message from the server (plain text)
                    String successMessage = response.body();
                    Toast.makeText(RegisterActivity.this, successMessage, Toast.LENGTH_LONG).show();

                    // Navigate to LoginActivity after successful registration
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {  // Handle bad request (400 status)
                    try {
                        // Get the error message from the server (plain text)
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error: Could not parse error message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle other non-successful responses
                    Toast.makeText(RegisterActivity.this, "Unexpected error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Show error message if request fails
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}

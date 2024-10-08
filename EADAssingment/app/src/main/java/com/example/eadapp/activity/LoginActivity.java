package com.example.eadapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eadapp.R;
import com.example.eadapp.api.UserApi;
import com.example.eadapp.data.LoginRequest;
import com.example.eadapp.data.LoginResponse;
import com.example.eadapp.network.RetrofitClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerRedirectTextView;

    // SharedPreferences file name
    private static final String SHARED_PREFS = "user_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String EMAIL_KEY = "email";
    private static final String USER_ID_KEY = "userId"; // Add key for userId

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerRedirectTextView = findViewById(R.id.register_redirect);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });

        registerRedirectTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        // Create Retrofit client and API service
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);

        // Prepare the login request payload
        LoginRequest loginRequest = new LoginRequest(email, password, "Customer");

        // Make the API call
        Call<LoginResponse> call = userApi.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i(TAG, "response ::: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    // Decode token to get email
                    String emailFromToken = decodeJwtToken(token);

                    // Save token and email in SharedPreferences
                    saveTokenAndEmail(token, emailFromToken);

                    // After login, fetch the userId using the token
                    fetchUserId(token);
                } else {
                    handleLoginError(response);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // Fetch the userId from API using the Bearer token
    private void fetchUserId(String token) {
        // Create Retrofit client and API service for user info
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);

        // Make the API call to get userId
        Call<JsonObject> call = userApi.getUserId("Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "response ::: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    // Extract the userId from the response
                    String userId = response.body().get("userId").getAsString();

                    // Save userId to SharedPreferences
                    saveUserId(userId);

                    // Navigate to MainActivity
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to fetch user ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error fetching user ID: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // Save userId in SharedPreferences
    private void saveUserId(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    // Method to decode JWT token and extract email
    private String decodeJwtToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String body = new String(Base64.decode(parts[1], Base64.DEFAULT));
                JSONObject jsonObject = new JSONObject(body);
                return jsonObject.getString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Save token and email in SharedPreferences
    private void saveTokenAndEmail(String token, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.putString(EMAIL_KEY, email);
        editor.apply(); // Save asynchronously
    }

    // Handle login error
    private void handleLoginError(Response<LoginResponse> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                JsonElement jsonElement = new JsonParser().parse(errorBody);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String errorMessage = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "Login failed";
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Login failed, please check your credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}

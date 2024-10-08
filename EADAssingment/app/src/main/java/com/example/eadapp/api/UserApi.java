package com.example.eadapp.api;

import com.example.eadapp.data.LoginRequest;
import com.example.eadapp.data.LoginResponse;
import com.example.eadapp.data.Order;
import com.example.eadapp.data.Product;
import com.example.eadapp.data.RegisterRequest;
import com.example.eadapp.data.Vendor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @Headers("Content-Type: application/json")
    @POST("/api/users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @Headers("Content-Type: application/json")
    @POST("/api/users/register")
    Call<String> registerUser(@Body RegisterRequest registerRequest);

    @GET("/api/users/user-id")
    Call<JsonObject> getUserId(@Header("Authorization") String token);

    @GET("/api/orders/customer/{userId}")
    Call<List<Order>> getOrdersByCustomer(@Header("Authorization") String token, @Path("userId") String userId);

    @GET("api/users/{userId}")
    Call<Vendor> getVendorDetails(@Path("userId") String vendorId);
    @POST("vendor/add-comment/{vendorId}")
    Call<JsonObject> addVendorComment(
            @Header("Authorization") String authHeader,
            @Path("vendorId") String vendorId,
            @Body JsonObject feedbackPayload
    );

    @GET("/api/product/getAll")
    Call<List<Product>> getAllProducts();

}

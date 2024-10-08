package com.example.eadapp.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.eadapp.data.Product;

import java.util.List;

public interface ProductAdapter {


    @NonNull
    ProductAdapterImpl.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
}

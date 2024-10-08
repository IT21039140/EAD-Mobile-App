package com.example.eadapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadapp.R;
import com.example.eadapp.activity.ProductDetailActivity;
import com.example.eadapp.activity.VendorFeedbackActivity;
import com.example.eadapp.data.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ProductAdapterImpl extends RecyclerView.Adapter<ProductAdapterImpl.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapterImpl(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product details
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.vendorName.setText(product.getVendorName());

        // Convert base64 image string to Bitmap
        byte[] decodedString = Base64.decode(product.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.productImage.setImageBitmap(decodedByte);

        // Save the image to a file and get the file path
        String imagePath = saveImageToFile(decodedByte, product.getName());

        // Handle product item click
        holder.itemView.setOnClickListener(v -> {
            // Navigate to ProductDetailActivity and pass product details
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_quantity", product.getQuantity());
//            intent.putExtra("vendor_name", product.getVendorName());
//            intent.putExtra("vendor_id", product.getVendorId());
            intent.putExtra("vendor_name", "xzy");
            intent.putExtra("vendor_id", "66faae2e1a174fd88fee8e5d");

            // Pass the image file path instead of Base64
            intent.putExtra("product_image_path", imagePath);
            context.startActivity(intent);
        });

        // Handle click on vendor name
        holder.vendorName.setOnClickListener(v -> {
            // Navigate to VendorFeedbackActivity and pass vendorId
            Intent intent = new Intent(context, VendorFeedbackActivity.class);
            intent.putExtra("vendor_name", product.getVendorName());
            intent.putExtra("vendor_id", product.getVendorId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice, vendorName;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            vendorName = itemView.findViewById(R.id.vendor_name);
            productImage = itemView.findViewById(R.id.product_image);  // ImageView for product image
        }
    }

    // Method to update the product list and notify the adapter
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    // Method to save the bitmap image to a file and return the file path
    private String saveImageToFile(Bitmap bitmap, String productName) {
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, productName + "_image.png");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }
}

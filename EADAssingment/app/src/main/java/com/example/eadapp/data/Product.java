package com.example.eadapp.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product implements Parcelable {
    private String id;
    private String productID;
    private String name;
    private String description;
    private double price;
    private String category;
    private String image;  // This is the Base64 image string from the API
    private Bitmap bitmapImage;  // This is the decoded Bitmap image
    private String vendorName;
    private String vendorId;
    private int quantity;
    private int status;

    // Parcelable implementation
    protected Product(Parcel in) {
        id = in.readString();
        productID = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        category = in.readString();
        image = in.readString();
        vendorName = in.readString();
        vendorId = in.readString();
        quantity = in.readInt();
        status = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(category);
        dest.writeString(image);
        dest.writeString(vendorName);
        dest.writeString(vendorId);
        dest.writeInt(quantity);
        dest.writeInt(status);
    }

}

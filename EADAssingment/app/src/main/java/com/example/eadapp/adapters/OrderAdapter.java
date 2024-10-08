package com.example.eadapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadapp.R;
import com.example.eadapp.data.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    private static final String TAG = "OrderAdapter";
    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderIdTextView.setText("Order ID: " + order.getId());
        holder.statusTextView.setText("Status: " + order.getStatus());
        holder.deliveryDateTextView.setText("Delivery Date: " + order.getDeliveryDate());
        holder.noteTextView.setText("Note: " + order.getNote());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Update order list in the adapter
    public void updateOrders(List<Order> orders) {
        Log.i(TAG,"orders::: "+orders);
        this.orderList = orders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, statusTextView, deliveryDateTextView, noteTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            statusTextView = itemView.findViewById(R.id.status);
            deliveryDateTextView = itemView.findViewById(R.id.delivery_date);
            noteTextView = itemView.findViewById(R.id.note);
        }
    }
}

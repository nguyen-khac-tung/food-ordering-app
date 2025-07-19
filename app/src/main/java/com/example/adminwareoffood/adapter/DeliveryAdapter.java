package com.example.adminwareoffood.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.DeliveryItemBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private final ArrayList<Order> orderList;
    private final Context context;

    public DeliveryAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeliveryItemBinding binding = DeliveryItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new DeliveryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);

        holder.binding.statusMoney.setOnClickListener(v -> {
            if ("Delivering".equalsIgnoreCase(order.getStatus())) {
                // Toggle between "Not Received" and "Received"
                String newPaymentStatus = "Not Received".equalsIgnoreCase(order.getPaymentStatus()) ? "Received" : "Not Received";

                // Update Firebase with new payment status
                DatabaseReference orderRef = FirebaseDatabase.getInstance()
                        .getReference("ORDERS")
                        .child(order.getUserId())
                        .child(order.getOrderId());

                orderRef.child("paymentStatus").setValue(newPaymentStatus);

                // If user clicks "Received", update order status to "completed"
                if ("Received".equalsIgnoreCase(newPaymentStatus)) {
                    orderRef.child("status").setValue("completed");
                }

                // Update local data and UI
                order.setPaymentStatus(newPaymentStatus);
                if ("Received".equalsIgnoreCase(newPaymentStatus)) {
                    order.setStatus("completed");
                }
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class DeliveryViewHolder extends RecyclerView.ViewHolder {
        private final DeliveryItemBinding binding;

        DeliveryViewHolder(DeliveryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            binding.customerName.setText(order.getUserName());

            // Display payment status
            String moneyStatus = order.getPaymentStatus() != null ? order.getPaymentStatus() : "Not Received";
            binding.statusMoney.setText(moneyStatus);

            // Set color based on payment status when Delivering
            int color;
            if ("Delivering".equalsIgnoreCase(order.getStatus())) {
                if ("Received".equalsIgnoreCase(moneyStatus)) {
                    color = Color.parseColor("#4caf50"); // green
                } else {
                    color = Color.parseColor("#ff3b3b"); // red
                }
            } else {
                color = Color.parseColor("#b3b3b3"); // gray for other statuses
            }
            binding.statusMoney.setTextColor(color);
            binding.statusColor.setCardBackgroundColor(color);
        }
    }
}
package com.example.adminwareoffood.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.CompletedOrderItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.CompletedViewHolder> {
    private final ArrayList<Order> orderList;
    private final Context context;

    public CompletedOrderAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public CompletedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CompletedOrderItemBinding binding = CompletedOrderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CompletedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CompletedViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class CompletedViewHolder extends RecyclerView.ViewHolder {
        private final CompletedOrderItemBinding binding;

        CompletedViewHolder(CompletedOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            // Set customer name
            binding.customerName.setText(order.getUserName() != null ? order.getUserName() : "N/A");

            // Format order date from timestamp to readable format
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedDate = order.getOrderDate() > 0 ? sdf.format(new Date(order.getOrderDate())) : "N/A";
            binding.orderDate.setText(formattedDate);

            // Set total amount
            binding.totalAmount.setText(String.valueOf(order.getTotalAmount()) + "$");

            // Set payment status
            String paymentStatus = order.getPaymentStatus() != null ? order.getPaymentStatus() : "Completed";
            binding.paymentStatus.setText(paymentStatus);

            // Set color to green for all completed items
            int color = Color.parseColor("#4caf50");
            binding.paymentStatus.setTextColor(color);
            binding.statusColor.setCardBackgroundColor(color);
        }
    }
}
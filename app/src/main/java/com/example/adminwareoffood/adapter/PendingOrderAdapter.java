package com.example.adminwareoffood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminwareoffood.model.Order;
import com.example.adminwareoffood.model.Cart;
import com.example.adminwareoffood.Constants;
import com.example.adminwareoffood.OrderDetailsActivity;
import com.example.food_ordering_app.databinding.PendingOrderItemBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {
    private final ArrayList<Order> orderList;
    private final Context context;

    public PendingOrderAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public PendingOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PendingOrderItemBinding binding = PendingOrderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PendingOrderViewHolder holder, int position) {
        holder.bind(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        private final PendingOrderItemBinding binding;
        private Order order;

        public PendingOrderViewHolder(PendingOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderId", order.getOrderId());
                context.startActivity(intent);
            });
        }

        public void bind(Order order) {
            this.order = order;
            binding.customerName.setText(order.getUserName());
            binding.pendingOrderQuantity.setText(order.getItems() != null ? String.valueOf(order.getItems().size()) : "0");

            if (order.getItems() != null && !order.getItems().isEmpty()) {
                Cart firstItem = order.getItems().get(0); // Lấy item đầu tiên
                Glide.with(context)
                        .load(firstItem.getFoodImageUrl()) // Tải ảnh từ URL
                        .placeholder(android.R.drawable.ic_menu_gallery) // Placeholder trong khi tải
                        .error(android.R.drawable.ic_menu_gallery) // Ảnh lỗi
                        .into(binding.orderFoodImage);
            } else {
                binding.orderFoodImage.setImageResource(android.R.drawable.ic_menu_gallery); // Placeholder nếu không có items
            }
            // Hiển thị trạng thái hiện tại
            String currentStatus = order.getStatus() != null ? order.getStatus() : "Unknown";
            binding.orderedAcceptionButton.setText(getButtonText(currentStatus));
            binding.orderedAcceptionButton.setOnClickListener(v -> {
                String status = order.getStatus();
                if (status != null && status.equals(Constants.StatusOrder.PENDING.getDisplayName())) {
                    updateOrderStatus(Constants.StatusOrder.CONFIRMED.getDisplayName());
                    binding.orderedAcceptionButton.setText(getButtonText(Constants.StatusOrder.CONFIRMED.getDisplayName()));
                    showToast("Order is confirmed");
                } else if (status != null && status.equals(Constants.StatusOrder.CONFIRMED.getDisplayName())) {
                    updateOrderStatus("Delivering");
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        orderList.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        showToast("Order is now in delivery");
                    }
                }
            });
        }

        private String getButtonText(String status) {
            if (status.equals(Constants.StatusOrder.PENDING.getDisplayName())) return "Accept";
            if (status.equals(Constants.StatusOrder.CONFIRMED.getDisplayName())) return "Dispatch";
            return "Accept";
        }

        private void updateOrderStatus(String newStatus) {
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                    .child("ORDERS")
                    .child(order.getUserId())
                    .child(order.getOrderId());
            orderRef.child("status").setValue(newStatus);
        }

        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
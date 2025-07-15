package com.example.adminwareoffood.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.DeliveryItemBinding;
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
        holder.bind(orderList.get(position));
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
            // Hiển thị tên khách hàng
            binding.customerName.setText(order.getUserName());
            // Hiển thị trạng thái thanh toán (giả định Order có trường paymentStatus)
            String moneyStatus = order.getPaymentStatus() != null ? order.getPaymentStatus() : "Pending";
            binding.statusMoney.setText(moneyStatus);
            // Thiết lập màu sắc cho trạng thái thanh toán
            int color;
            switch (moneyStatus.toLowerCase()) {
                case "received":
                    color = Color.parseColor("#4caf50"); // green
                    break;
                case "pending":
                    color = Color.parseColor("#b3b3b3"); // gray
                    break;
                case "not received":
                    color = Color.parseColor("#ff3b3b"); // red
                    break;
                default:
                    color = Color.BLACK;
            }
            binding.statusMoney.setTextColor(color);
            binding.statusColor.setCardBackgroundColor(color);
        }
    }
}
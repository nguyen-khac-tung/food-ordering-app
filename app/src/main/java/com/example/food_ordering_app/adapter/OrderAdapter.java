package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.OrderDetailActivity;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.databinding.OrderItemBinding;
import com.example.food_ordering_app.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderItemBinding binding =  OrderItemBinding.inflate(inflater, parent, false);
        return new OrderViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private final OrderItemBinding binding;
        private final Context context;

        public OrderViewHolder(OrderItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(int position) {
            Order order = orderList.get(position);

            binding.userReceiveOrder.setText(order.getUserName());
            binding.totalOrder.setText("$" + order.getTotalAmount());
            binding.statusOrder.setText(order.getStatus());
            if (order.getStatus().trim().equalsIgnoreCase(Constants.StatusOrder.COMPLETED.getDisplayName())) {
                binding.statusOrder.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            }

            // Lấy ảnh của món hàng đầu tiên để hiển thị
            if (order.getItems() != null && !order.getItems().isEmpty()) {
                Glide.with(context)
                        .load(order.getItems().get(0).getFoodImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(binding.imageOrder);
            }

            binding.viewDetailButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderId", order.getOrderId());
                context.startActivity(intent);
            });
        }
    }
}

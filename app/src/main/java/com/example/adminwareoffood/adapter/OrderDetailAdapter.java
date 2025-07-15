package com.example.adminwareoffood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminwareoffood.model.Cart;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.OrderDetailItemBinding;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private final ArrayList<Order> orderList;
    private final Context context;

    public OrderDetailAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderDetailItemBinding binding = OrderDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(OrderDetailViewHolder holder, int position) {
        if (!orderList.isEmpty()) {
            Order order = orderList.get(0);
            if (order.getItems() != null && !order.getItems().isEmpty()) {
                Cart item = order.getItems().get(position);
                holder.bind(item);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (orderList.isEmpty() || orderList.get(0).getItems() == null) return 0;
        return orderList.get(0).getItems().size();
    }

    class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        private final OrderDetailItemBinding binding;

        public OrderDetailViewHolder(OrderDetailItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Cart item) {
            binding.foodName.setText(item.getFoodName() != null ? item.getFoodName() : "N/A");
            binding.foodPrice.setText(String.valueOf(item.getFoodPrice()+ "$")); // Hiển thị trực tiếp giá trị int
            binding.foodQuantity.setText(String.valueOf(item.getQuantity() > 0 ? item.getQuantity() : 0));

            Glide.with(context)
                    .load(item.getFoodImageUrl() != null ? item.getFoodImageUrl() : "")
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.foodImage);
        }
    }
}
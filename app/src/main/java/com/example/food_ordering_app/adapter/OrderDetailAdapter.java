package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.FoodDetailActivity;
import com.example.food_ordering_app.PayOutActivity;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.databinding.OrderDetailItemBinding;
import com.example.food_ordering_app.models.Cart;
import com.example.food_ordering_app.models.Food;
import com.example.food_ordering_app.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<Cart> orderDetailItems;

    public OrderDetailAdapter(List<Cart> orderDetailItems) {
        this.orderDetailItems = orderDetailItems;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderDetailItemBinding binding =  OrderDetailItemBinding.inflate(inflater, parent, false);
        return new OrderDetailViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return orderDetailItems.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        private final OrderDetailItemBinding binding;
        private final Context context;

        public OrderDetailViewHolder(OrderDetailItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String foodId = orderDetailItems.get(position).getFoodId();

                    // Gọi hàm để lấy chi tiết Food và mở Activity
                    fetchFoodDetails(foodId);
                }
            });
        }

        private void fetchFoodDetails(String foodId) {
            DatabaseReference foodRef = FirebaseConfig.getDatabase()
                    .getReference(Constants.FirebaseRef.FOODS.toString())
                    .child(foodId);

            foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Food foodObject = snapshot.getValue(Food.class);
                        if (foodObject != null) {
                            // Firebase key là foodId, cần gán lại vào đối tượng
                            foodObject.setFoodId(snapshot.getKey());

                            Intent intent = new Intent(context, FoodDetailActivity.class);
                            intent.putExtra("foodObject", foodObject);
                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, "Food details not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("CartAdapter", "Failed to fetch food details.", error.toException());
                    Toast.makeText(context, "Failed to load details.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bind(int position) {
            Cart item = orderDetailItems.get(position);

            binding.orderDetailFoodName.setText(item.getFoodName());
            binding.orderDetailFoodPrice.setText("$" + item.getFoodPrice());
            binding.orderDetailFoodQuantity.setText(String.valueOf(item.getQuantity()));

            Glide.with(context)
                    .load(item.getFoodImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.orderDetailFoodImage);
        }
    }
}
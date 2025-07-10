package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.FoodDetailActivity;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.databinding.PopularItemBinding;
import com.example.food_ordering_app.models.Cart;
import com.example.food_ordering_app.models.Food;
import com.example.food_ordering_app.utils.CartHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private final List<Food> popularItems;

    public PopularAdapter(List<Food> popularItems) {
        this.popularItems = popularItems;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PopularItemBinding binding = PopularItemBinding.inflate(inflater, parent, false);
        return new PopularViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return popularItems.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        private final PopularItemBinding binding;
        private final Context context;

        public PopularViewHolder(@NonNull PopularItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Food clickedFood = popularItems.get(position);
                    Intent intent = new Intent(context, FoodDetailActivity.class);
                    // Đóng gói dữ liệu và gửi đi
                    intent.putExtra("foodObject", clickedFood);

                    context.startActivity(intent);
                }
            });
        }

        public void bind(int position) {
            Food foodItem = popularItems.get(position);
            binding.popularFoodName.setText(foodItem.getFoodName());
            binding.popularFoodPrice.setText("$" + foodItem.getFoodPrice());

            // Sử dụng Glide để tải ảnh từ URL
            Glide.with(context)
                    .load(foodItem.getFoodImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.popularFoodImage);

            binding.addToCartPopular.setOnClickListener(v -> {
                CartHelper.addToCart(context, foodItem);
            });
        }
    }
}
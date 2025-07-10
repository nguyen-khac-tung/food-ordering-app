package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_ordering_app.FoodDetailActivity;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.databinding.MenuItemBinding;
import com.example.food_ordering_app.models.Food;
import com.example.food_ordering_app.utils.CartHelper;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<Food> menuItems;

    public MenuAdapter(List<Food> menuItems) {
        this.menuItems = menuItems;
    }
    
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MenuItemBinding binding = MenuItemBinding.inflate(inflater, parent, false);
        return new MenuViewHolder(binding, parent.getContext());
    }

    
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        //Gắn dữ liệu vào ViewHolder
        holder.bind(position);
    }
    
    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    //Lớp ViewHolder nội (Inner Class)
    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private final MenuItemBinding binding;
        private final Context context;
        
        public MenuViewHolder(MenuItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Food clickedFood = menuItems.get(position);
                    Intent intent = new Intent(context, FoodDetailActivity.class);
                    // Đóng gói dữ liệu và gửi đi
                    intent.putExtra("foodObject", clickedFood);

                    context.startActivity(intent);
                }
            });
        }
        
        public void bind(int position) {
            Food foodItem = menuItems.get(position);
            binding.menuFoodName.setText(foodItem.getFoodName());
            binding.menuFoodPrice.setText("$" + foodItem.getFoodPrice());

            // Sử dụng Glide để tải ảnh từ URL
            Glide.with(context)
                    .load(foodItem.getFoodImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.menuFoodImage);

            binding.addToCartMenu.setOnClickListener(v -> {
                CartHelper.addToCart(context, foodItem);
            });
        }
    }

}
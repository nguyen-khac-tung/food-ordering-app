package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_app.FoodDetailActivity;
import com.example.food_ordering_app.databinding.MenuItemBinding;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<String> menuFoodNames;
    private final List<String> menuFoodPrices;
    private final List<Integer> menuFoodImages;
    private final Context context;
    
    public MenuAdapter(Context context, List<String> menuFoodNames, List<String> menuFoodPrices, List<Integer> menuFoodImages) {
        this.menuFoodNames = menuFoodNames;
        this.menuFoodPrices = menuFoodPrices;
        this.menuFoodImages = menuFoodImages;
        this.context = context; // Khởi tạo context
    }
    
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MenuItemBinding binding = MenuItemBinding.inflate(inflater, parent, false);
        return new MenuViewHolder(binding);
    }

    
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        //Gắn dữ liệu vào ViewHolder
        holder.bind(position);
    }
    
    @Override
    public int getItemCount() {
        return menuFoodNames.size();
    }

    //Lớp ViewHolder nội (Inner Class)
    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private final MenuItemBinding binding;
        
        public MenuViewHolder(MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Tạo Intent ngay tại đây
                    Intent intent = new Intent(context, FoodDetailActivity.class);

                    // Đóng gói dữ liệu và gửi đi
                    intent.putExtra("foodName", menuFoodNames.get(position));
                    intent.putExtra("foodPrice", menuFoodPrices.get(position));
                    intent.putExtra("foodImage", menuFoodImages.get(position));

                    // Khởi chạy Activity từ Context đã được truyền vào
                    context.startActivity(intent);
                }
            });
        }
        
        public void bind(int position) {
            binding.menuFoodName.setText(menuFoodNames.get(position));
            binding.menuFoodPrice.setText(menuFoodPrices.get(position));
            binding.menuFoodImage.setImageResource(menuFoodImages.get(position));
        }
    }
}
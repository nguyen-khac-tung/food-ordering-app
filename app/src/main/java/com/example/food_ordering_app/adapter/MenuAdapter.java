package com.example.food_ordering_app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_app.databinding.MenuItemBinding;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<String> menuFoodNames;
    private final List<String> menuFoodPrices;
    private final List<Integer> menuFoodImages;
    
    public MenuAdapter(List<String> menuFoodNames, List<String> menuFoodPrices, List<Integer> menuFoodImages) {
        this.menuFoodNames = menuFoodNames;
        this.menuFoodPrices = menuFoodPrices;
        this.menuFoodImages = menuFoodImages;
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
        }
        
        public void bind(int position) {
            binding.menuFoodName.setText(menuFoodNames.get(position));
            binding.menuFoodPrice.setText(menuFoodPrices.get(position));
            binding.menuFoodImage.setImageResource(menuFoodImages.get(position));
        }
    }
}
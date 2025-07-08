package com.example.food_ordering_app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_app.databinding.BuyAgainItemBinding;
import com.example.food_ordering_app.databinding.MenuItemBinding;

import java.util.List;

public class BuyAgainAdapter extends RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder> {

    private final List<String> buyAgainFoodNames;
    private final List<String> buyAgainFoodPrices;
    private final List<Integer> buyAgainFoodImages;

    public BuyAgainAdapter(List<String> buyAgainFoodNames, List<String> buyAgainFoodPrices, List<Integer> buyAgainFoodImages) {
        this.buyAgainFoodNames = buyAgainFoodNames;
        this.buyAgainFoodPrices = buyAgainFoodPrices;
        this.buyAgainFoodImages = buyAgainFoodImages;
    }

    @NonNull
    @Override
    public BuyAgainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BuyAgainItemBinding binding =  BuyAgainItemBinding.inflate(inflater, parent, false);
        return new BuyAgainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAgainViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return buyAgainFoodNames.size();
    }

    public class BuyAgainViewHolder extends RecyclerView.ViewHolder {
        private final BuyAgainItemBinding binding;

        public BuyAgainViewHolder(BuyAgainItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            binding.buyAgainFoodName.setText(buyAgainFoodNames.get(position));
            binding.buyAgainFoodPrice.setText(buyAgainFoodPrices.get(position));
            binding.buyAgainFoodImage.setImageResource(buyAgainFoodImages.get(position));
        }
    }
}

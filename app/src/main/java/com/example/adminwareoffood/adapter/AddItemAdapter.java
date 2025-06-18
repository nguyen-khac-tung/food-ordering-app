package com.example.adminwareoffood.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminwareoffood.databinding.ItemItemBinding;

import java.util.ArrayList;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder> {
    private final ArrayList<String> menuItemName;
    private final ArrayList<String> menuItemPrice;
    private final ArrayList<Integer> menuItemImage;
    private final int[] itemQuantities;

    public AddItemAdapter(ArrayList<String> menuItemName, ArrayList<String> menuItemPrice, ArrayList<Integer> menuItemImage) {
        this.menuItemName = menuItemName;
        this.menuItemPrice = menuItemPrice;
        this.menuItemImage = menuItemImage;
        this.itemQuantities = new int[menuItemName.size()];
        for (int i = 0; i < itemQuantities.length; i++) {
            itemQuantities[i] = 1;
        }
    }

    @NonNull
    @Override
    public AddItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemBinding binding = ItemItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AddItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return menuItemName.size();
    }

    class AddItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemItemBinding binding;

        AddItemViewHolder(ItemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int position) {
            binding.foodNameTextView.setText(menuItemName.get(position));
            binding.priceTextView.setText(menuItemPrice.get(position));
            binding.foodImageView.setImageResource(menuItemImage.get(position));
            binding.quantityTextView.setText(String.valueOf(itemQuantities[position]));

            binding.minusButton.setOnClickListener(v -> decreaseQuantity(position));
            binding.plusButton.setOnClickListener(v -> increaseQuantity(position));
            binding.deleteButton.setOnClickListener(v -> deleteItem(position));

        }

        private void decreaseQuantity(int position) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--;
                binding.quantityTextView.setText(String.valueOf(itemQuantities[position]));
            }
        }

        private void increaseQuantity(int position) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++;
                binding.quantityTextView.setText(String.valueOf(itemQuantities[position]));
            }
        }

        private void deleteItem(int position) {
            menuItemName.remove(position);
            menuItemPrice.remove(position);
            menuItemImage.remove(position);
            // Remove quantity
            int[] newQuantities = new int[itemQuantities.length - 1];
            for (int i = 0, j = 0; i < itemQuantities.length; i++) {
                if (i != position) {
                    newQuantities[j++] = itemQuantities[i];
                }
            }
            System.arraycopy(newQuantities, 0, itemQuantities, 0, newQuantities.length);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, menuItemName.size());
        }
    }
}
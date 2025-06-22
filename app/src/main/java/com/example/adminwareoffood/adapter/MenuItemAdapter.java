package com.example.adminwareoffood.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminwareoffood.databinding.ItemItemBinding;
import com.example.adminwareoffood.model.AllMenu;

import java.util.ArrayList;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder> {
    private  ArrayList<String> menuItemName;
    private  ArrayList<String> menuItemPrice;
    private  ArrayList<Integer> menuItemImage;
    private final int[] itemQuantities;

    //start
    private Context context;
    private ArrayList<AllMenu> menuList;
    private DatabaseReference databaseReference;

    public MenuItemAdapter(Context context, ArrayList<AllMenu> menuListItems, DatabaseReference databaseReference) {
        this.context = context;
        this.menuList = menuListItems; // Assign the passed list to the class member
        this.databaseReference = databaseReference;
        // Initialize itemQuantities based on the size of the new menuList
        if (this.menuList != null) {
            this.itemQuantities = new int[this.menuList.size()];
            for (int i = 0; i < this.itemQuantities.length; i++) {
                this.itemQuantities[i] = 1; // Default quantity
            }
        } else {
            this.itemQuantities = new int[0]; // Handle null menuList case
        }
    }

    public MenuItemAdapter(ArrayList<String> menuItemName, ArrayList<String> menuItemPrice, ArrayList<Integer> menuItemImage, int[] itemQuantities, Context context, ArrayList<AllMenu> menuList) {
        this.menuItemName = menuItemName;
        this.menuItemPrice = menuItemPrice;
        this.menuItemImage = menuItemImage;
        this.itemQuantities = itemQuantities;
        this.context = context;
        this.menuList = menuList;
    }


    //end

    public MenuItemAdapter(ArrayList<String> menuItemName, ArrayList<String> menuItemPrice, ArrayList<Integer> menuItemImage) {
        this.menuItemName = menuItemName;
        this.menuItemPrice = menuItemPrice;
        this.menuItemImage = menuItemImage;
        this.itemQuantities = new int[menuList.size()];
        for (int i = 0; i < itemQuantities.length; i++) {//từ menuItemname -> menuList
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
        return menuList.size();
    } //từ menuItemname -> menuList

    class AddItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemItemBinding binding;

        AddItemViewHolder(ItemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int position) {
            // Update
            AllMenu menuItem = menuList.get(position);
            String uriString = menuItem.getFoodImage(); // Giả sử có getter
            Uri uri = Uri.parse(uriString);
            Glide.with(context).load(uri).into(binding.foodImageView);

// Set text
            binding.foodNameTextView.setText(menuItem.getFoodName());
            binding.priceTextView.setText(menuItem.getFoodPrice());
            //binding.foodImageView.setImageResource(menuList.get(position));
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
            menuList.remove(position);//từ menuItemname -> menuList
            menuList.remove(position);//từ menuItemname -> menuList
            menuList.remove(position);//từ menuItemname -> menuList
            // Remove quantity
            int[] newQuantities = new int[itemQuantities.length - 1];
            for (int i = 0, j = 0; i < itemQuantities.length; i++) {
                if (i != position) {
                    newQuantities[j++] = itemQuantities[i];
                }
            }
            System.arraycopy(newQuantities, 0, itemQuantities, 0, newQuantities.length);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, menuList.size());
        }
    }
}
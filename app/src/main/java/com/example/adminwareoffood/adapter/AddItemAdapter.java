package com.example.adminwareoffood.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.adminwareoffood.model.MenuItem;
import com.example.food_ordering_app.databinding.ItemItemBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder> {
    private final List<MenuItem> itemList;
    private final OnItemDeleteListener onItemDeleteListener;

    public interface OnItemDeleteListener {
        void onDelete(MenuItem item);
    }
    public AddItemAdapter(List<MenuItem> itemList, OnItemDeleteListener onItemDeleteListener) {
        this.itemList = itemList;
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @NonNull
    @Override
    public AddItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemBinding binding = ItemItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AddItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemViewHolder holder, int position) {
        holder.bind(itemList.get(position), onItemDeleteListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class AddItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemItemBinding binding;

        AddItemViewHolder(ItemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MenuItem item, OnItemDeleteListener listener) {
            binding.foodNameTextView.setText(item.foodName);
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")); // Ví dụ cho VND
            binding.priceTextView.setText(currencyFormatter.format(item.foodPrice));
            Glide.with(binding.foodImageView.getContext())
                    .load(item.foodImageUrl)
                    .into(binding.foodImageView);

            binding.deleteButton.setOnClickListener(v ->{
                if (listener != null) {
                    listener.onDelete(item);
                }
            });
        }
    }
}
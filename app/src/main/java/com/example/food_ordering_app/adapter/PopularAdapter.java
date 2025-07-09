package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_app.FoodDetailActivity;
import com.example.food_ordering_app.databinding.PopularItemBinding;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private final List<String> popularFoodNames;
    private final List<String> popularFoodPrices;
    private final List<Integer> popularFoodImages;
    private final Context context;

    public PopularAdapter(Context context, List<String> popularFoodNames, List<String> popularFoodPrices, List<Integer> popularFoodImages) {
        this.popularFoodNames = popularFoodNames;
        this.popularFoodPrices = popularFoodPrices;
        this.popularFoodImages = popularFoodImages;
        this.context = context; // Khởi tạo context
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PopularItemBinding binding = PopularItemBinding.inflate(inflater, parent, false);
        return new PopularViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return popularFoodNames.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        private final PopularItemBinding binding;

        public PopularViewHolder(@NonNull PopularItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Tạo Intent ngay tại đây
                    Intent intent = new Intent(context, FoodDetailActivity.class);

                    // Đóng gói dữ liệu và gửi đi
                    intent.putExtra("foodName", popularFoodNames.get(position));
                    intent.putExtra("foodPrice", popularFoodPrices.get(position));
                    intent.putExtra("foodImage", popularFoodImages.get(position));

                    // Khởi chạy Activity từ Context đã được truyền vào
                    context.startActivity(intent);
                }
            });
        }

        public void bind(int position) {
            binding.popularFoodName.setText(popularFoodNames.get(position));
            binding.popularFoodPrice.setText(popularFoodPrices.get(position));
            binding.popularFoodImage.setImageResource(popularFoodImages.get(position));
        }
    }
}
package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_app.FoodDetailActivity;
import com.example.food_ordering_app.databinding.CartItemBinding;
import java.util.Arrays;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    
    private List<String> cartFoodNames;
    private List<String> cartFoodPrices;
    private List<Integer> cartFoodImages;
    private final Context context;
    
    private int[] foodQuantities;
    
    public CartAdapter(Context context, List<String> cartFoodNames, List<String> cartFoodPrices, List<Integer> cartFoodImages) {
        this.cartFoodNames = cartFoodNames;
        this.cartFoodPrices = cartFoodPrices;
        this.cartFoodImages = cartFoodImages;
        this.context = context; // Khởi tạo context
        
        // Khởi tạo mảng số lượng, mặc định mỗi item có số lượng là 1
        this.foodQuantities = new int[cartFoodNames.size()];
        Arrays.fill(this.foodQuantities, 1);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CartItemBinding binding = CartItemBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Gán dữ liệu cho ViewHolder
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartFoodNames.size();
    }

    // Lớp ViewHolder nội (inner class)
    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;

        public CartViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Tạo Intent ngay tại đây
                    Intent intent = new Intent(context, FoodDetailActivity.class);

                    // Đóng gói dữ liệu và gửi đi
                    intent.putExtra("foodName", cartFoodNames.get(position));
                    intent.putExtra("foodPrice", cartFoodPrices.get(position));
                    intent.putExtra("foodImage", cartFoodImages.get(position));

                    // Khởi chạy Activity từ Context đã được truyền vào
                    context.startActivity(intent);
                }
            });
        }

        public void bind(int position) {
            // Gán dữ liệu vào các view
            binding.cartFoodName.setText(cartFoodNames.get(position));
            binding.cartFoodPrice.setText(cartFoodPrices.get(position));
            binding.cartFoodImage.setImageResource(cartFoodImages.get(position));
            binding.cartFoodQuantity.setText(String.valueOf(foodQuantities[position]));
            
            binding.plusButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    increaseQuantity(currentPosition);
                }
            });

            binding.minusButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    decreaseQuantity(currentPosition);
                }
            });

            binding.deleteButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    deleteItem(currentPosition);
                }
            });
        }
    }

    // Các hàm xử lý logic của Adapter
    private void increaseQuantity(int position) {
        if (foodQuantities[position] < 10) {
            foodQuantities[position]++;
            // Thông báo cho adapter biết item này đã thay đổi để vẽ lại UI
            notifyItemChanged(position);
        }
    }

    private void decreaseQuantity(int position) {
        if (foodQuantities[position] > 1) {
            foodQuantities[position]--;
            notifyItemChanged(position);
        }
    }

    private void deleteItem(int position) {
        // Kiểm tra vị trí hợp lệ trước khi xóa
        if (position >= 0 && position < cartFoodNames.size()) {
            cartFoodNames.remove(position);
            cartFoodImages.remove(position);
            cartFoodPrices.remove(position);

            // Cần cập nhật lại mảng số lượng
            updateQuantitiesArrayAfterDeletion(position);

            // Thông báo cho adapter biết một item đã bị xóa
            notifyItemRemoved(position);
            // Thông báo cho adapter biết các item sau vị trí bị xóa đã thay đổi vị trí
            notifyItemRangeChanged(position, cartFoodNames.size());
        }
    }

    // Hàm phụ trợ để cập nhật mảng số lượng sau khi xóa
    private void updateQuantitiesArrayAfterDeletion(int deletedPosition) {
        int[] newQuantities = new int[cartFoodNames.size()];
        // Sao chép các phần tử trước vị trí đã xóa
        System.arraycopy(foodQuantities, 0, newQuantities, 0, deletedPosition);
        // Sao chép các phần tử sau vị trí đã xóa
        if (foodQuantities.length > deletedPosition + 1) {
            System.arraycopy(foodQuantities, deletedPosition + 1, newQuantities, deletedPosition, cartFoodNames.size() - deletedPosition);
        }
        foodQuantities = newQuantities;
    }
}
package com.example.food_ordering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.food_ordering_app.databinding.CartItemBinding;
import com.example.food_ordering_app.models.Cart;
import com.example.food_ordering_app.models.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Cart> cartItems;
    private final String userId;
    private Context parentContext;

    public CartAdapter(List<Cart> cartItems) {
        this.cartItems = cartItems;
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CartItemBinding binding = CartItemBinding.inflate(inflater, parent, false);
        parentContext = parent.getContext();
        return new CartViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Gán dữ liệu cho ViewHolder
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // Lớp ViewHolder nội (inner class)
    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;
        private final Context context;

        public CartViewHolder(@NonNull CartItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String foodId = cartItems.get(position).getFoodId();

                    // Gọi hàm để lấy chi tiết Food và mở Activity
                    fetchFoodDetails(foodId);
                }
            });
        }

        private void fetchFoodDetails(String foodId) {
            DatabaseReference foodRef = FirebaseConfig.getDatabase()
                    .getReference(Constants.FirebaseRef.FOODS.toString())
                    .child(foodId);

            foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Food foodObject = snapshot.getValue(Food.class);
                        if (foodObject != null) {
                            // Firebase key là foodId, cần gán lại vào đối tượng
                            foodObject.setFoodId(snapshot.getKey());

                            Intent intent = new Intent(context, FoodDetailActivity.class);
                            intent.putExtra("foodObject", foodObject);
                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, "Food details not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("CartAdapter", "Failed to fetch food details.", error.toException());
                    Toast.makeText(context, "Failed to load details.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bind(int position) {
            Cart cartItem = cartItems.get(position);

            binding.cartFoodName.setText(cartItem.getFoodName());
            binding.cartFoodPrice.setText("$" + cartItem.getFoodPrice());
            binding.cartFoodQuantity.setText(String.valueOf(cartItem.getQuantity()));

            Glide.with(context)
                    .load(cartItem.getFoodImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.cartFoodImage);

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
    private DatabaseReference getCartItemReference(String foodId) {
        return FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.CARTS.toString())
                .child(userId)
                .child(foodId);
    }

    private void increaseQuantity(int position) {
        Cart cartItem = cartItems.get(position);
        if (cartItem.getQuantity() < 10) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            getCartItemReference(cartItem.getFoodId()).setValue(cartItem);
            // Giao diện sẽ tự cập nhật nhờ ValueEventListener trong CartFragment
        } else {
            Toast.makeText(parentContext ,"Cannot order more than 10 items", Toast.LENGTH_SHORT).show();
        }
    }

    private void decreaseQuantity(int position) {
        Cart cartItem = cartItems.get(position);
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            getCartItemReference(cartItem.getFoodId()).setValue(cartItem);
            // Giao diện sẽ tự cập nhật
        } else {
            // Nếu số lượng là 1 và bấm giảm, xóa sản phẩm
            deleteItem(position);
        }
    }

    private void deleteItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            Cart cartItem = cartItems.get(position);
            getCartItemReference(cartItem.getFoodId()).removeValue();
            // Giao diện sẽ tự cập nhật
            Toast.makeText(parentContext ,"Food item has been removed", Toast.LENGTH_SHORT).show();
        }
    }
}
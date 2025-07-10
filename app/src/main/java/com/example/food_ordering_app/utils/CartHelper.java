package com.example.food_ordering_app.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.models.Cart;
import com.example.food_ordering_app.models.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CartHelper {

    // Constructor private để không ai có thể tạo instance của lớp tiện ích này
    private CartHelper() {}

    public static void addToCart(Context context, Food foodItem) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) {
            Toast.makeText(context.getApplicationContext(), "You need to be logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference cartRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.CARTS.toString())
                .child(userId)
                .child(foodItem.getFoodId());

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Sản phẩm đã có trong giỏ hàng, tăng số lượng
                    Cart cartItem = snapshot.getValue(Cart.class);
                    if (cartItem != null) {
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                        cartRef.setValue(cartItem);
                    }
                } else {
                    // Sản phẩm chưa có, thêm mới
                    Cart newCartItem = new Cart(
                            foodItem.getFoodId(),
                            foodItem.getFoodName(),
                            foodItem.getFoodPrice(),
                            foodItem.getFoodImageUrl(),
                            1 // Số lượng ban đầu là 1
                    );
                    cartRef.setValue(newCartItem);
                }
                Toast.makeText(context.getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


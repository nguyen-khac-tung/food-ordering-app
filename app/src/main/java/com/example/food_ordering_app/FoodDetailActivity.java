package com.example.food_ordering_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.food_ordering_app.databinding.ActivityFoodDetailBinding;
import com.example.food_ordering_app.models.Food;
import com.example.food_ordering_app.utils.CartHelper;

public class FoodDetailActivity extends AppCompatActivity {

    private ActivityFoodDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(v -> finish());

        // Nhận toàn bộ đối tượng Food từ Intent
        Food food = (Food) getIntent().getSerializableExtra("foodObject");
        if (food == null) { finish(); return; }

        binding.detailFoodName.setText(food.getFoodName());
        binding.detailFoodPrice.setText("$" + food.getFoodPrice());
        binding.detailFoodDescription.setText(food.getFoodDescription());
        if (food.getFoodIngredients() != null) {
            binding.detailFoodIngredient.setText(food.getFoodIngredients().replace("\\n", "\n"));
        }

        // Dùng Glide để tải ảnh từ URL
        Glide.with(this)
                .load(food.getFoodImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.detailFoodImage);

        binding.addToCartDetail.setOnClickListener(v -> {
            CartHelper.addToCart(this, food);
        });
    }
}
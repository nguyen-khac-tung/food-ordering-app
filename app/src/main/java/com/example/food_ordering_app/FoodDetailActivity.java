package com.example.food_ordering_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_ordering_app.databinding.ActivityFoodDetailBinding;

public class FoodDetailActivity extends AppCompatActivity {

    private ActivityFoodDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });

        String foodName = getIntent().getStringExtra("foodName");
        String foodPrice = getIntent().getStringExtra("foodPrice");
        int foodImage = getIntent().getIntExtra("foodImage", 0);
        binding.detailFoodName.setText(foodName);
        binding.detailFoodPrice.setText(foodPrice);
        binding.detailFoodImage.setImageResource(foodImage);
    }
}
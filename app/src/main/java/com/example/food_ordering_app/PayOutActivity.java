package com.example.food_ordering_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_ordering_app.databinding.ActivityPayOutBinding;
import com.example.food_ordering_app.fragment.CongratulationFragment;

public class PayOutActivity extends AppCompatActivity {

    private ActivityPayOutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityPayOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.buttonBack.setOnClickListener(v -> finish());

        binding.placeOrderButton.setOnClickListener(v -> {
            CongratulationFragment congratulationFragment = new CongratulationFragment();
            congratulationFragment.show(getSupportFragmentManager(), congratulationFragment.getTag());
        });

    }
}
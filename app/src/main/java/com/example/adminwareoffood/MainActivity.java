package com.example.adminwareoffood;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminwareoffood.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
        binding.allItemMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllItemActivity.class);
            startActivity(intent);
        });
        binding.outForDeliveryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OutForDeliveryActivity.class);
            startActivity(intent);
        });

        binding.profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminProfileActivity.class);
            startActivity(intent);
        });

        binding.createUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
            startActivity(intent);
        });binding.pendingOrderTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PendingOrderActivity.class);
            startActivity(intent);
        });
    }
}
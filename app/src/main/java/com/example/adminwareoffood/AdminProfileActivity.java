package com.example.adminwareoffood;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adminwareoffood.databinding.ActivityAddItemBinding;
import com.example.adminwareoffood.databinding.ActivityAdminProfileBinding;

public class AdminProfileActivity extends AppCompatActivity {
    private ActivityAdminProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        // Disable all fields initially
        binding.name.setEnabled(false);
        binding.address.setEnabled(false);
        binding.email.setEnabled(false);
        binding.phone.setEnabled(false);
        binding.password.setEnabled(false);

        // Toggle enable/disable on editButton click
        binding.editButton.setOnClickListener(v -> {
            boolean isEnabled = !binding.name.isEnabled();
            binding.name.setEnabled(isEnabled);
            binding.address.setEnabled(isEnabled);
            binding.email.setEnabled(isEnabled);
            binding.phone.setEnabled(isEnabled);
            binding.password.setEnabled(isEnabled);
            if (isEnabled) {
                binding.name.requestFocus();
            }
        });
    }
}
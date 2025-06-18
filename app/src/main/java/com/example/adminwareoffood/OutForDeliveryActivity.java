package com.example.adminwareoffood;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminwareoffood.adapter.DeliveryAdapter;
import com.example.adminwareoffood.databinding.ActivityOutForDeliveryBinding;

import java.util.ArrayList;

public class OutForDeliveryActivity extends AppCompatActivity {
    private ActivityOutForDeliveryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOutForDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ArrayList<String> customerName = new ArrayList<>();
        customerName.add("Danh Thuc");
        customerName.add("Thuc Tran");
        customerName.add("Tran Danh");

        ArrayList<String> moneyStatus = new ArrayList<>();
        moneyStatus.add("received");
        moneyStatus.add("not received");
        moneyStatus.add("Pending");



        DeliveryAdapter adapter = new DeliveryAdapter(customerName, moneyStatus);
        binding.deliveryRecycleView.setAdapter(adapter);
        binding.deliveryRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}
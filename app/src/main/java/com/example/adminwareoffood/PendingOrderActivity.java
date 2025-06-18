package com.example.adminwareoffood;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminwareoffood.adapter.DeliveryAdapter;
import com.example.adminwareoffood.adapter.PendingOrderAdapter;
import com.example.adminwareoffood.databinding.PendingOrderItemBinding;

import java.util.ArrayList;

public class PendingOrderActivity extends AppCompatActivity {
    private com.example.adminwareoffood.databinding.ActivityPendingOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = com.example.adminwareoffood.databinding.ActivityPendingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ArrayList<String> orderedCustomerName = new ArrayList<>();
        orderedCustomerName.add("Danh Thuc");
        orderedCustomerName.add("Thuc Tran");
        orderedCustomerName.add("Tran Danh");

        ArrayList<String> orderedQuantity = new ArrayList<>();
        orderedQuantity.add("8");
        orderedQuantity.add("9");
        orderedQuantity.add("4");

        ArrayList<Integer> orderedFoodImage = new ArrayList<>();
        orderedFoodImage.add(R.drawable.menu1);
        orderedFoodImage.add(R.drawable.menu2);
        orderedFoodImage.add(R.drawable.menu3);

        PendingOrderAdapter adapter = new PendingOrderAdapter(orderedCustomerName, orderedQuantity, orderedFoodImage, this );
        binding.pendingOrderRecycleView.setAdapter(adapter);
        binding.pendingOrderRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}
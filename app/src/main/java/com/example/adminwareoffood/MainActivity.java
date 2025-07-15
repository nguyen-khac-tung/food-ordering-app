package com.example.adminwareoffood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderRef = FirebaseDatabase.getInstance().getReference().child(Constants.FirebaseRef.ORDERS.name());
        fetchOrderCounts();

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
        });
        binding.pendingOrderTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PendingOrderActivity.class);
            startActivity(intent);
        });
        binding.logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
    private void fetchOrderCounts() {
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pendingOrderCount = 0;
                int completedCount = 0;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order.getStatus().equals(Constants.StatusOrder.PENDING.getDisplayName())) {
                            pendingOrderCount++;
                        }else if (order.getStatus().equals(Constants.StatusOrder.COMPLETED.getDisplayName())) {
                            completedCount++;
                        }
                    }
                }
                binding.totalPendingOrder.setText(String.valueOf(pendingOrderCount));
                binding.totalCompleted.setText(String.valueOf(completedCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load pending orders", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
package com.example.food_ordering_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.food_ordering_app.adapter.OrderDetailAdapter;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.databinding.ActivityFoodDetailBinding;
import com.example.food_ordering_app.databinding.ActivityOrderDetailBinding;
import com.example.food_ordering_app.models.Cart;
import com.example.food_ordering_app.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private OrderDetailAdapter adapter;
    private List<Cart> orderDetailItems;
    private String orderId;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(v -> finish());

        orderId = getIntent().getStringExtra("orderId");
        if (orderId == null || orderId.isEmpty()) {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orderRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.ORDERS.toString())
                .child(currentUserId)
                .child(orderId);

        setupRecyclerView();
        fetchOrderDetail();
    }

    private void setupRecyclerView() {
        orderDetailItems = new ArrayList<>();
        adapter = new OrderDetailAdapter(orderDetailItems);
        binding.orderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderDetailRecyclerView.setAdapter(adapter);
    }

    private void fetchOrderDetail() {
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderDetailItems.clear();
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        bindOrderDetails(order);
                        orderDetailItems.addAll(order.getItems());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Order details not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailActivity.this, "Failed to load order details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindOrderDetails(Order order) {
        binding.orderDetailUserName.setText(order.getUserName());
        binding.orderDetailAddress.setText(order.getUserAddress());
        binding.orderDetailPhone.setText(order.getUserPhone());
        binding.orderDetailTotal.setText("$" + order.getTotalAmount());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        binding.orderDetailDate.setText(sdf.format(new Date(order.getOrderDate())));

        updateStatusUI(order.getStatus().trim());
    }

    private void updateStatusUI(String status) {
        binding.orderDetailStatus.setText(status);

        if (status.equalsIgnoreCase(Constants.StatusOrder.COMPLETED.getDisplayName())) {
            binding.orderDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.textColor));
        }

        if (status.equalsIgnoreCase(Constants.StatusOrder.DELIVERING.getDisplayName())) {
            binding.receiveButton.setVisibility(View.VISIBLE);
            binding.receiveButton.setOnClickListener(v -> completeOrder());
        } else {
            binding.receiveButton.setVisibility(View.GONE);
        }
    }

    private void completeOrder() {
        // Vô hiệu hóa nút để tránh click nhiều lần
        binding.receiveButton.setEnabled(false);

        String newStatus = Constants.StatusOrder.COMPLETED.getDisplayName();

        orderRef.child("status")
                .setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật lại giao diện ngay lập tức
                    updateStatusUI(newStatus);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderDetailActivity.this, "Failed to update status. Please try again.", Toast.LENGTH_SHORT).show();
                    // Kích hoạt lại nút nếu có lỗi
                    binding.receiveButton.setEnabled(true);
                });
    }
}
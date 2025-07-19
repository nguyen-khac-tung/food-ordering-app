package com.example.adminwareoffood;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminwareoffood.adapter.CompletedOrderAdapter;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.ActivityCompletedDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompletedDetailsActivity extends AppCompatActivity {
    private ActivityCompletedDetailsBinding binding;
    private DatabaseReference ordersRef;
    private ArrayList<Order> orderList = new ArrayList<>();
    private CompletedOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCompletedDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        // Sử dụng binding để xử lý WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo tham chiếu đến bảng ORDERS trong Firebase
        ordersRef = FirebaseDatabase.getInstance().getReference().child("ORDERS");

        // Thiết lập RecyclerView
        adapter = new CompletedOrderAdapter(orderList, this);
        binding.completedRecyclerView.setAdapter(adapter);
        binding.completedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Firebase
        fetchCompletedOrders();
    }

    private void fetchCompletedOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            order.setOrderId(orderSnapshot.getKey());
                            order.setUserId(userSnapshot.getKey());

                            // Filter orders with status "Completed"
                            if ("Completed".equalsIgnoreCase(order.getStatus())) {
                                orderList.add(order);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();

                // Hiển thị thông báo nếu không có đơn hàng
                if (orderList.isEmpty()) {
                    binding.completedRecyclerView.setVisibility(View.GONE);

                } else {
                    binding.completedRecyclerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
                Toast.makeText(CompletedDetailsActivity.this, "Error loading orders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.adminwareoffood;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminwareoffood.adapter.OrderDetailAdapter;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.ActivityOrderDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;
    private DatabaseReference ordersRef;
    private String orderId;
    private ArrayList<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //back button
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());


        orderId = getIntent().getStringExtra("orderId");
        if (orderId == null) {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ordersRef = FirebaseDatabase.getInstance().getReference().child(Constants.FirebaseRef.ORDERS.name());

        binding.orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderDetailAdapter adapter = new OrderDetailAdapter(orderList, this);
        binding.orderDetailsRecyclerView.setAdapter(adapter);

        fetchOrderDetails();
    }

    private void fetchOrderDetails() {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                boolean orderFound = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            order.setOrderId(orderSnapshot.getKey());
                            order.setUserId(userSnapshot.getKey());
                            if (order.getOrderId() != null && order.getOrderId().equals(orderId)) {
                                orderList.add(order);
                                binding.userIdText.setText(order.getUserName() != null ? order.getUserName() : "N/A");
                                binding.userAddressText.setText(order.getUserAddress() != null ? order.getUserAddress() : "N/A");
                                binding.userPhoneText.setText(order.getUserPhone() != null ? order.getUserPhone() : "N/A");
                                binding.orderDetailsRecyclerView.getAdapter().notifyDataSetChanged();
                                orderFound = true;
                                return;
                            }
                        }
                    }
                }
                if (!orderFound) {
                    Toast.makeText(OrderDetailsActivity.this, "Order not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrderDetailsActivity.this, "Failed to load order details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
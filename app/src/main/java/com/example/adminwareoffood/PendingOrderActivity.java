package com.example.adminwareoffood;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.adminwareoffood.adapter.PendingOrderAdapter;
import com.example.adminwareoffood.model.Order;
import com.example.food_ordering_app.databinding.ActivityPendingOrderBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;

public class PendingOrderActivity extends AppCompatActivity {
    private ActivityPendingOrderBinding binding;
    private DatabaseReference ordersRef;
    private ArrayList<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPendingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Khởi tạo tham chiếu đến bảng ORDERS trong Firebase
        ordersRef = FirebaseDatabase.getInstance().getReference().child("ORDERS");

        // Lấy dữ liệu từ Firebase
        fetchOrders();

        // Thiết lập RecyclerView
        PendingOrderAdapter adapter = new PendingOrderAdapter(orderList, this);
        binding.pendingOrderRecycleView.setAdapter(adapter);
        binding.pendingOrderRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) { // Lặp qua userId
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) { // Lặp qua orderId
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            // Đặt orderId từ khóa của snapshot
                            order.setOrderId(orderSnapshot.getKey());
                            // Lấy userId từ snapshot cha
                            order.setUserId(userSnapshot.getKey());
                            // Chỉ thêm đơn hàng có trạng thái PENDING hoặc CONFIRMED
                            String status = order.getStatus();
                            if (status != null && (status.equals(Constants.StatusOrder.PENDING.getDisplayName()) ||
                                    status.equals(Constants.StatusOrder.CONFIRMED.getDisplayName()))) {
                                orderList.add(order);
                            }
                        }
                    }
                }
                // Cập nhật adapter
                binding.pendingOrderRecycleView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }
}
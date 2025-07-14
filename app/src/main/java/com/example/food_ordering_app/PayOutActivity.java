package com.example.food_ordering_app;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.databinding.ActivityPayOutBinding;
import com.example.food_ordering_app.fragment.CongratulationFragment;
import com.example.food_ordering_app.models.Cart;
import com.example.food_ordering_app.models.Order;
import com.example.food_ordering_app.models.User;
import com.example.food_ordering_app.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PayOutActivity extends AppCompatActivity {

    private ActivityPayOutBinding binding;
    private FirebaseAuth auth;
    private String currentUserId;
    private List<Cart> cartItems;
    private int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPayOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        cartItems = (List<Cart>) getIntent().getSerializableExtra("cartItems");
        totalAmount = getIntent().getIntExtra("totalAmount", 0);

        binding.totalOrder.setText("$" + totalAmount);
        loadUserProfile();

        binding.buttonBack.setOnClickListener(v -> finish());
        binding.placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void loadUserProfile() {
        DatabaseReference userRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.USERS.toString())
                .child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        binding.userReceiveOrder.setText(user.getName());
                        binding.userAddressReceiveOrder.setText(user.getLocation());
                        binding.userPhoneReceiveOrder.setText(user.getPhone());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PayOutActivity.this, "Failed to load user info.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void placeOrder() {
        String userName = binding.userReceiveOrder.getText().toString().trim();
        String userAddress = binding.userAddressReceiveOrder.getText().toString().trim();
        String userPhone = binding.userPhoneReceiveOrder.getText().toString().trim();

        if (userName.isEmpty() || userAddress.isEmpty() || userPhone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference orderRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.ORDERS.toString())
                .child(currentUserId)
                .push(); // push() tạo ra một key duy nhất cho đơn hàng

        String orderId = orderRef.getKey();
        long orderDate = System.currentTimeMillis();
        String orderStatus = Constants.StatusOrder.PENDING.getDisplayName();

        Order order = new Order(orderId, currentUserId, userName, userAddress, userPhone,
                orderDate, totalAmount, orderStatus, cartItems);

        orderRef.setValue(order)
                .addOnSuccessListener(aVoid -> {
                    removeCart();
                    CongratulationFragment congratulationFragment = new CongratulationFragment();
                    congratulationFragment.show(getSupportFragmentManager(), congratulationFragment.getTag());
                    NotificationHelper.showOrderStatusNotification(
                            this,
                            order.getOrderId(),
                            "Wait for confirmation", "Your order " + order.getOrderId() + " is waiting for confirmation, please wait a few minutes");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void removeCart() {
        DatabaseReference cartRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.CARTS.toString())
                .child(currentUserId);
        cartRef.removeValue();
    }
}
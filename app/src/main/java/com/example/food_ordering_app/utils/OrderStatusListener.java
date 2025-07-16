package com.example.food_ordering_app.utils;

import android.content.Context;

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.models.Notification;
import com.example.food_ordering_app.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderStatusListener {
    private final DatabaseReference ordersRef;
    private final Context context;
    private final Map<String, String> lastStatusMap = new HashMap<>();

    public OrderStatusListener(Context context, String userId) {
        this.context = context;
        ordersRef = FirebaseDatabase.getInstance().getReference(Constants.FirebaseRef.ORDERS.name()).child(userId);
    }

    public void startListening() {
        ordersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildChanged(@NotNull DataSnapshot snapshot, String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                if (order != null) {
                    String newStatus = order.getStatus();
                    String orderId = order.getOrderId();
                    String currentUserId = FirebaseAuth.getInstance().getUid();
                    if (currentUserId == null || orderId == null) return;
                    String lastStatus = lastStatusMap.get(orderId);
                    if (lastStatus != null && lastStatus.equals(newStatus)) {
                        return;
                    }
                    lastStatusMap.put(orderId, newStatus);
                    Constants.StatusOrder status = Constants.StatusOrder.fromValue(newStatus);
                    Notification notification = null;
                    switch (Objects.requireNonNull(status)) {
                        case PENDING:
                            notification = new Notification("Wait for confirmation",
                                    "Your order " + orderId + " is waiting for confirmation, please wait a few minutes",
                                    orderId, System.currentTimeMillis(), false);
                            break;
                        case CONFIRMED:
                            notification = new Notification("Order confirmed",
                                    "Your order " + orderId + " has been confirmed.",
                                    orderId, System.currentTimeMillis(), false);
                            break;
                        case DELIVERING:
                            notification = new Notification("Order is being shipped",
                                    "Your order " + orderId + " is being shipped.",
                                    orderId, System.currentTimeMillis(), false);
                            break;
                        case COMPLETED:
                            notification = new Notification("Order Completed",
                                    "Your order " + orderId + " is being shipped. Enjoy your meal!",
                                    orderId, System.currentTimeMillis(), false);
                            break;
                    }
                    if (notification != null) {
                        NotificationHelper.notifyAndSave(context, currentUserId, notification);
                    }
                }
            }

            @Override
            public void onChildAdded(@NotNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NotNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NotNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
            }
        });
    }
}

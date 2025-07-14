package com.example.food_ordering_app.utils;

import android.content.Context;

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.models.Order;
import com.google.firebase.database.*;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OrderStatusListener {
    private final DatabaseReference ordersRef;
    private final Context context;

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
                    Constants.StatusOrder status = Constants.StatusOrder.fromValue(order.getStatus());
                    switch (Objects.requireNonNull(status)) {
                        case PENDING:
                            NotificationHelper.showOrderStatusNotification(
                                    context,
                                    order.getOrderId(),
                                    "Wait for confirmation", "Your order " + order.getOrderId() + " is waiting for confirmation, please wait a few minutes");
                            break;
                        case CONFIRMED:
                            NotificationHelper.showOrderStatusNotification(
                                    context,
                                    order.getOrderId(),
                                    "Order confirmed", "Your order " + order.getOrderId() + " has been confirmed.");
                            break;
                        case DELIVERING:
                            NotificationHelper.showOrderStatusNotification(
                                    context,
                                    order.getOrderId(),
                                    "Order is being shipped", "Your order " + order.getOrderId() + " is being shipped.");
                            break;
                        case COMPLETED:
                            NotificationHelper.showOrderStatusNotification(
                                    context,
                                    order.getOrderId(),
                                    "Order Completed", "Your order " + order.getOrderId() + " is being shipped. Enjoy your meal!");
                            break;
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

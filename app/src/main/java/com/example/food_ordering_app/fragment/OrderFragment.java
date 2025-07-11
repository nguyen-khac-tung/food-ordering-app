package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.adapter.MenuAdapter;
import com.example.food_ordering_app.adapter.OrderAdapter;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.databinding.FragmentOrderBinding;
import com.example.food_ordering_app.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    private OrderAdapter adapter;
    private List<Order> orderList;
    private DatabaseReference orderRef;
    private ValueEventListener orderListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);

        setupRecyclerView();
        fetchOrders();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList);
        binding.orderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.orderRecyclerView.setAdapter(adapter);
        updateUIBasedOnOrders();
    }

    private void fetchOrders() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orderRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.ORDERS.toString())
                .child(currentUserId);

        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }

                // Đảo ngược list để đơn hàng mới nhất lên đầu
                Collections.reverse(orderList);
                adapter.notifyDataSetChanged();
                updateUIBasedOnOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderFragment", "Failed to fetch orders.", error.toException());
            }
        };

        orderRef.addValueEventListener(orderListener);
    }

    private void updateUIBasedOnOrders() {
        if (orderList.isEmpty()) {
            binding.emptyOrder.setVisibility(View.VISIBLE);
            binding.orderRecyclerView.setVisibility(View.GONE);
        } else {
            binding.emptyOrder.setVisibility(View.GONE);
            binding.orderRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (orderRef != null && orderListener != null) {
            orderRef.removeEventListener(orderListener);
        }
        binding = null;
    }
}
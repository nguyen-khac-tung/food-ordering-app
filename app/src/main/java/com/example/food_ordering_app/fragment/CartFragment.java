package com.example.food_ordering_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.PayOutActivity;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.adapter.CartAdapter;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.databinding.FragmentCartBinding;
import com.example.food_ordering_app.models.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartAdapter adapter;
    private List<Cart> cartItems;
    private DatabaseReference cartDatabaseRef;
    private ValueEventListener cartValueEventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        setupRecyclerView();
        retrieveCartItems();

        binding.proceedButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(requireContext(), PayOutActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>();
        adapter = new CartAdapter(cartItems);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(adapter);
        updateUIBasedOnCart();
    }

    private void retrieveCartItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cartDatabaseRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.CARTS.toString())
                .child(userId);

        cartValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Cart cart = itemSnapshot.getValue(Cart.class);
                    if (cart != null) {
                        cartItems.add(cart);
                    }
                }
                adapter.notifyDataSetChanged();
                updateUIBasedOnCart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) { // Kiểm tra context trước khi dùng
                    Log.e("CartFragment", "Failed to load cart items.", error.toException());
                    Toast.makeText(getContext(), "Failed to load cart.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        cartDatabaseRef.addValueEventListener(cartValueEventListener);
    }

    private void updateUIBasedOnCart() {
        if (cartItems.isEmpty()) {
            binding.emptyCartTextView.setVisibility(View.VISIBLE);
            binding.cartRecyclerView.setVisibility(View.GONE);
            binding.proceedButton.setEnabled(false);
            binding.proceedButton.setAlpha(0.8f);
        } else {
            binding.emptyCartTextView.setVisibility(View.GONE);
            binding.cartRecyclerView.setVisibility(View.VISIBLE);
            binding.proceedButton.setEnabled(true);
            binding.proceedButton.setAlpha(1.0f);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cartDatabaseRef != null && cartValueEventListener != null) {
            cartDatabaseRef.removeEventListener(cartValueEventListener);
        }
        binding = null;
    }
}
package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.food_ordering_app.R;
import com.example.food_ordering_app.adapter.MenuAdapter;
import com.example.food_ordering_app.databinding.FragmentMenuBinding;
import com.example.food_ordering_app.models.Food;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuFragment extends BottomSheetDialogFragment {

    private FragmentMenuBinding binding;
    private MenuAdapter adapter;
    private List<Food> menuItems;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;


    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);

        binding.buttonBack.setOnClickListener(v -> {
            dismiss();
        });

        // Khởi tạo RecyclerView và Adapter
        setupRecyclerView();

        // Lấy dữ liệu từ Firebase
        retrieveMenuItems();

        return  binding.getRoot();
    }

    private void setupRecyclerView() {
        menuItems = new ArrayList<>();
        adapter = new MenuAdapter(menuItems);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);
    }

    private void retrieveMenuItems() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("FOODS");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear(); // Xóa dữ liệu cũ trước khi thêm mới
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    if (food != null) {
                        food.setFoodId(foodSnapshot.getKey());
                        menuItems.add(food);
                    }
                }
                // Cập nhật lại adapter sau khi đã có dữ liệu
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MenuFragment", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
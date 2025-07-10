package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.food_ordering_app.adapter.MenuAdapter;
import com.example.food_ordering_app.databinding.FragmentSearchBinding;
import com.example.food_ordering_app.models.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private MenuAdapter adapter;
    private List<Food> originalMenuItems = new ArrayList<>();
    private List<Food> filteredMenuItems = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        adapter = new MenuAdapter(filteredMenuItems);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        //Thiết lập thanh tìm kiếm
        setupSearchView();

        //Lấy toàn bộ dữ liệu từ Firebase
        retrieveAllMenuItems();

        return binding.getRoot();
    }

    private void retrieveAllMenuItems() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("FOODS");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                originalMenuItems.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    if (food != null) {
                        food.setFoodId(foodSnapshot.getKey());
                        originalMenuItems.add(food);
                    }
                }

                showAllMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchFragment", "Failed to retrieve data.", error.toException());
            }
        });
    }

    private void showAllMenu() {
        filteredMenuItems.clear();
        filteredMenuItems.addAll(originalMenuItems);
        adapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMenuItems(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenuItems(newText);
                return true;
            }
        });
    }

    private void filterMenuItems(String query) {
        filteredMenuItems.clear();

        if (query.isEmpty()) {
            filteredMenuItems.addAll(originalMenuItems);
        } else {
            // Lặp qua danh sách GỐC để tìm kiếm
            for (Food foodItem : originalMenuItems) {
                if (foodItem.getFoodName().toLowerCase().contains(query.toLowerCase())) {
                    filteredMenuItems.add(foodItem);
                }
            }
        }

        // Thông báo cho adapter rằng dữ liệu đã thay đổi để cập nhật giao diện
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Tránh memory leak
    }
}
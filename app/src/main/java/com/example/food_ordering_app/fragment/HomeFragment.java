package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.databinding.FragmentHomeBinding;
import com.example.food_ordering_app.adapter.PopularAdapter;
import com.example.food_ordering_app.models.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.viewAllMenu.setOnClickListener(v -> {
            MenuFragment menuBottomDialog = new MenuFragment();
            menuBottomDialog.show(getParentFragmentManager(), "Test");;
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupBanner();
        retrieveAndDisplayPopularItems();
    }

    private void setupBanner() {
        List<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        ImageSlider imageSlider = binding.imageSlider;
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
    }

    private void retrieveAndDisplayPopularItems() {
        DatabaseReference databaseReference = FirebaseConfig.getDatabase().getReference(Constants.FirebaseRef.FOODS.toString());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Food> allMenuItems = new ArrayList<>();
                    for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                        Food food = foodSnapshot.getValue(Food.class);
                        if (food != null) {
                            food.setFoodId(foodSnapshot.getKey());
                            allMenuItems.add(food);
                        }
                    }

                    displayRandomPopularItems(allMenuItems);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Failed to retrieve popular items.", error.toException());
            }
        });
    }

    private void displayRandomPopularItems(List<Food> allMenuItems) {
        // Xáo trộn toàn bộ danh sách
        Collections.shuffle(allMenuItems);

        int numberOfItemsToShow = Math.min(allMenuItems.size(), 6);
        List<Food> popularItems = allMenuItems.subList(0, numberOfItemsToShow);

        PopularAdapter adapter = new PopularAdapter(popularItems);
        binding.popularRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.popularRecycleView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
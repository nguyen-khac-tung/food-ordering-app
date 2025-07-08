package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.databinding.FragmentHomeBinding;
import com.example.food_ordering_app.adapter.PopularAdapter;

import java.util.ArrayList;
import java.util.Arrays;
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

        //Banner
        List<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        ImageSlider imageSlider = binding.imageSlider;
        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        //Popular Foods
        List<String> foodNames = Arrays.asList("Burger", "Sandwich", "Momo", "Item");
        List<String> foodPrices = Arrays.asList("$5", "$7", "$8", "$10");
        List<Integer> foodImages = Arrays.asList(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4);
        
        PopularAdapter adapter = new PopularAdapter(foodNames, foodPrices, foodImages);
        
        binding.popularRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.popularRecycleView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
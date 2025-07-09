package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.food_ordering_app.R;
import com.example.food_ordering_app.adapter.MenuAdapter;
import com.example.food_ordering_app.databinding.FragmentMenuBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;
import java.util.List;

public class MenuFragment extends BottomSheetDialogFragment {

    private FragmentMenuBinding binding;

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

        //Menu
        List<String> foodNames = Arrays.asList("Burger", "Sandwich", "Momo", "Item", "Burger", "Sandwich", "Momo", "Item", "Burger", "Sandwich", "Momo", "Item");
        List<String> foodPrices = Arrays.asList("$5", "$7", "$8", "$10", "$5", "$7", "$8", "$10", "$5", "$7", "$8", "$10");
        List<Integer> foodImages = Arrays.asList(
                R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4,
                R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4,
                R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4);

        MenuAdapter adapter = new MenuAdapter(getContext(), foodNames, foodPrices, foodImages);

        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        return  binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
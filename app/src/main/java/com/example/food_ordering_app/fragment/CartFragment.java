package com.example.food_ordering_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.food_ordering_app.PayOutActivity;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.adapter.CartAdapter;
import com.example.food_ordering_app.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        List<String> cartFoodName = new ArrayList<>(Arrays.asList("Burger", "Sandwich", "Momo", "Item", "Sandwich", "Momo"));
        List<String> cartItemPrice = new ArrayList<>(Arrays.asList("$5", "$6", "$8", "$9", "$10", "$10"));
        List<Integer> cartImage = new ArrayList<>(Arrays.asList(
                R.drawable.menu1,
                R.drawable.menu2,
                R.drawable.menu3,
                R.drawable.menu4,
                R.drawable.menu5,
                R.drawable.menu6
        ));

        CartAdapter adapter = new CartAdapter(getContext(), cartFoodName, cartItemPrice, cartImage);

        // Thiết lập RecyclerView
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(adapter);

        binding.proceedButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PayOutActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
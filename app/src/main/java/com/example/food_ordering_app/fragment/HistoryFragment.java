package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_ordering_app.R;
import com.example.food_ordering_app.adapter.BuyAgainAdapter;
import com.example.food_ordering_app.databinding.FragmentHistoryBinding;

import java.util.Arrays;
import java.util.List;


public class HistoryFragment extends Fragment {

   private FragmentHistoryBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        List<String> foodNames = Arrays.asList("Burger", "Sandwich", "Momo", "Item", "Burger", "Sandwich", "Momo", "Item", "Burger", "Sandwich", "Momo", "Item");
        List<String> foodPrices = Arrays.asList("$5", "$7", "$8", "$10", "$5", "$7", "$8", "$10", "$5", "$7", "$8", "$10");
        List<Integer> foodImages = Arrays.asList(
                R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4,
                R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4,
                R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4);

        BuyAgainAdapter adapter = new BuyAgainAdapter(getContext(), foodNames, foodPrices, foodImages);

        binding.buyAgainRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.buyAgainRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
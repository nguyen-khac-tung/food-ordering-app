package com.example.food_ordering_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.food_ordering_app.R;
import com.example.food_ordering_app.adapter.MenuAdapter;
import com.example.food_ordering_app.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private MenuAdapter adapter;

    private final List<String> originalFoodNames = Arrays.asList("Burger", "Sandwich", "Momo", "Item", "Burger", "Sandwich", "Momo", "Item", "Burger", "Sandwich", "Momo", "Item");
    private final List<String> originalFoodPrices = Arrays.asList("$5", "$7", "$8", "$10", "$5", "$7", "$8", "$10", "$5", "$7", "$8", "$10");
    private final List<Integer> originalFoodImages = Arrays.asList(
            R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4,
            R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4,
            R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4);

    private List<String> filteredMenuFoodName = new ArrayList<>();
    private List<String> filteredMenuItemPrice = new ArrayList<>();
    private List<Integer> filteredMenuImage = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        adapter = new MenuAdapter(getContext(), filteredMenuFoodName, filteredMenuItemPrice, filteredMenuImage);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        //set up search view
        setupSearchView();

        //Show all menu
        showAllMenu();
        return binding.getRoot();
    }

    private void showAllMenu() {
        filteredMenuFoodName.clear();
        filteredMenuItemPrice.clear();
        filteredMenuImage.clear();

        filteredMenuFoodName.addAll(originalFoodNames);
        filteredMenuItemPrice.addAll(originalFoodPrices);
        filteredMenuImage.addAll(originalFoodImages);
        adapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                filterMenuItems(query);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                filterMenuItems(newText);
                return true;
            }
        });
    }

    private void filterMenuItems(String query) {
        filteredMenuFoodName.clear();
        filteredMenuItemPrice.clear();
        filteredMenuImage.clear();

        // Lặp qua danh sách gốc để tìm các mục khớp với truy vấn
        for (int index = 0; index < originalFoodNames.size(); index++) {
            String foodName = originalFoodNames.get(index);

            // Kiểm tra xem tên món ăn có chứa chuỗi truy vấn hay không
            if (foodName.toLowerCase().contains(query.toLowerCase())) {
                filteredMenuFoodName.add(foodName);
                filteredMenuItemPrice.add(originalFoodPrices.get(index));
                filteredMenuImage.add(originalFoodImages.get(index));
            }
        }

        // Thông báo cho adapter rằng dữ liệu đã thay đổi để cập nhật giao diện
        adapter.notifyDataSetChanged();
    }
}
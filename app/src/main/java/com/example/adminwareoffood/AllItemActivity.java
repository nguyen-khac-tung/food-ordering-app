package com.example.adminwareoffood;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminwareoffood.adapter.AddItemAdapter;
import com.example.adminwareoffood.databinding.ActivityAllItemBinding;

import java.util.ArrayList;

public class AllItemActivity extends AppCompatActivity {
    private ActivityAllItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAllItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        
        ArrayList<String> menuFoodName = new ArrayList<>();
        menuFoodName.add("Burger");
        menuFoodName.add("Sanwich");
        menuFoodName.add("Momo");
        menuFoodName.add("Item1");
        menuFoodName.add("item2");
        menuFoodName.add("Item3");

        ArrayList<String> menuItemPrice = new ArrayList<>();
        menuItemPrice.add("$5");
        menuItemPrice.add("$10");
        menuItemPrice.add("$6");
        menuItemPrice.add("$7");
        menuItemPrice.add("$12");
        menuItemPrice.add("$8");

        ArrayList<Integer> menuImage = new ArrayList<>();
        menuImage.add(R.drawable.menu1);
        menuImage.add(R.drawable.menu2);
        menuImage.add(R.drawable.menu3);
        menuImage.add(R.drawable.menu4);
        menuImage.add(R.drawable.menu5);
        menuImage.add(R.drawable.menu3);

        AddItemAdapter adapter = new AddItemAdapter(menuFoodName, menuItemPrice, menuImage);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.menuRecyclerView.setAdapter(adapter);
    }
}
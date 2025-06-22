package com.example.adminwareoffood;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminwareoffood.adapter.MenuItemAdapter;
import com.example.adminwareoffood.databinding.ActivityAllItemBinding;
import com.example.adminwareoffood.model.AllMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllItemActivity extends AppCompatActivity {
    private ActivityAllItemBinding binding;

    //update start
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private ArrayList<AllMenu> menuItems = new ArrayList<>();

    //en

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAllItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //change start
        databaseReference = FirebaseDatabase.getInstance().getReference("Menu");
        retrieveMenuItems();
        //end
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        
//        ArrayList<String> menuFoodName = new ArrayList<>();
//        menuFoodName.add("Burger");
//        menuFoodName.add("Sanwich");
//        menuFoodName.add("Momo");
//        menuFoodName.add("Item1");
//        menuFoodName.add("item2");
//        menuFoodName.add("Item3");
//
//        ArrayList<String> menuItemPrice = new ArrayList<>();
//        menuItemPrice.add("$5");
//        menuItemPrice.add("$10");
//        menuItemPrice.add("$6");
//        menuItemPrice.add("$7");
//        menuItemPrice.add("$12");
//        menuItemPrice.add("$8");
//
//        ArrayList<Integer> menuImage = new ArrayList<>();
//        menuImage.add(R.drawable.menu1);
//        menuImage.add(R.drawable.menu2);
//        menuImage.add(R.drawable.menu3);
//        menuImage.add(R.drawable.menu4);
//        menuImage.add(R.drawable.menu5);
//        menuImage.add(R.drawable.menu3);

//        MenuItemAdapter adapter = new MenuItemAdapter(menuFoodName, menuItemPrice, menuImage);
//        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        binding.menuRecyclerView.setAdapter(adapter);
    }

    //new method
    private void retrieveMenuItems() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = database.getReference().child("vendor").child("menu");

        // fetch data from firebase
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear existing items before populating
                menuItems.clear();

                // loop through each food item
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    AllMenu menuItem = foodSnapshot.getValue(AllMenu.class);
                    if (menuItem != null) {
                        menuItems.add(menuItem);
                    }
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
                Log.d("DatabaseError: Error", "Error: " + error.getMessage());
            }
        });
    }
    private void setAdapter() {
        MenuItemAdapter adapter = new MenuItemAdapter(AllItemActivity.this, menuItems, databaseReference);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.menuRecyclerView.setAdapter(adapter);
    }

}
package com.example.adminwareoffood;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity  {

    private ActivityOrderDetailsBinding binding;

    private String userName = null;
    private String address = null;
    private String phoneNumber = null;
    private String totalPrice = null;

    private List<String> foodNames = new ArrayList<>();
    private List<String> foodImages = new ArrayList<>();
    private List<String> foodQuantity = new ArrayList<>();
    private List<String> foodPrices = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);

    }

    private fun getDataFromIntent(){
        val receivedOrderDetails = intent.getParcelableExtra<OrderDetails>("UserOrderDetails")
                if(receivedOrderDetails != null) {
                    userName = receivedOrderDetails.userName
                    foodNames = receivedOrderDetails.foodNames!!
                    foodImages = receivedOrderDetails.foodImages!!
                    foodQuantity = receivedOrderDetails.foodQuantities!!
                    address = receivedOrderDetails.address
                    phoneNumber = receivedOrderDetails.phoneNumber
                    foodPrices = receivedOrderDetails.foodPrices!!
                    totalPrice = receivedOrderDetails.totalPrice

                setUserDetails()
                setAdapter()
                }
    }

    private fun setUserDetails() {
        binding.userName.text = userName
        binding.address.text = address
        binding.phoneNumber.text = phoneNumber
        binding.totalPrice.text = totalPrice

        // Set up RecyclerView or other UI components to display food details
        // For example, you can use a RecyclerView adapter to show foodNames, foodImages, etc.
    }

    private fun setAdapter() {
        // Assuming you have a RecyclerView in your layout to display food items
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = FoodItemAdapter(foodNames, foodImages, foodPrices, foodQuantity)
        binding.foodRecyclerView.adapter = adapter
    }
}
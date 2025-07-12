package com.example.adminwareoffood;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminwareoffood.adapter.DeliveryAdapter;
import com.example.adminwareoffood.adapter.PendingOrderAdapter;
import com.example.adminwareoffood.databinding.ActivityPendingOrderBinding;
import com.example.adminwareoffood.databinding.PendingOrderItemBinding;
import com.example.adminwareoffood.model.OrderDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderActivity extends AppCompatActivity {
    private ActivityPendingOrderBinding binding;
    //start
    private List<String> listOfName = new ArrayList<>();
    private List<String> listOfTotalPrice = new ArrayList<>();
    private List<Integer> listOfImageFirstFoodOrder = new ArrayList<>();
    private List<OrderDetails> listOfOrderItem = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference databaseOrderDetails;


    //end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPendingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //change start
        //initialize the database
        database = FirebaseDatabase.getInstance();
        //initialize the database reference for order details
        databaseOrderDetails = database.getReference("OrderDetails");
        getOrdersDetails();
        //end
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

//        ArrayList<String> orderedCustomerName = new ArrayList<>();
//        orderedCustomerName.add("Danh Thuc");
//        orderedCustomerName.add("Thuc Tran");
//        orderedCustomerName.add("Tran Danh");
//
//        ArrayList<String> orderedQuantity = new ArrayList<>();
//        orderedQuantity.add("8");
//        orderedQuantity.add("9");
//        orderedQuantity.add("4");
//
//        ArrayList<Integer> orderedFoodImage = new ArrayList<>();
//        orderedFoodImage.add(R.drawable.menu1);
//        orderedFoodImage.add(R.drawable.menu2);
//        orderedFoodImage.add(R.drawable.menu3);
//
//        PendingOrderAdapter adapter = new PendingOrderAdapter(orderedCustomerName, orderedQuantity, orderedFoodImage, this );
//        binding.pendingOrderRecycleView.setAdapter(adapter);
//        binding.pendingOrderRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getOrdersDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetails orderDetails = orderSnapshot.getValue(OrderDetails.class);
                    if (orderDetails != null) {
                        listOfOrderItem.add(orderDetails);
                    }
                }

                // Cập nhật dữ liệu lên RecyclerView
                addDataToListForRecyclerView();
            }

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void addDataToListForRecyclerView() {
        for (OrderDetails orderItem : listOfOrderItem) {
            if (orderItem.getUserName() != null) {
                listOfName.add(orderItem.getUserName());
            }
            if (orderItem.getTotalPrice() != null) {
                listOfTotalPrice.add(orderItem.getTotalPrice());
            }
            if (orderItem.getFoodImages() != null) {
                for (String image : orderItem.getFoodImages()) {
                    if (!image.isEmpty()) {
                        listOfImageFirstFoodOrder.add(Integer.valueOf(image));
                    }
                }
            }
        }

        setAdapter();
    }

    private void setAdapter() {
        Context context = null;
        binding.pendingOrderRecycleView.setLayoutManager(new LinearLayoutManager(context));
        PendingOrderAdapter adapter = new PendingOrderAdapter(context, listOfName, listOfTotalPrice, listOfImageFirstFoodOrder);
        binding.pendingOrderRecycleView.setAdapter(adapter);
    }


}
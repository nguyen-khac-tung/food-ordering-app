package com.example.adminwareoffood.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.view.View;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    @SuppressLint("RestrictedApi")
    private Context context;
    private List<String> foodName;
    private List<String> foodImage;
    private List<Integer> foodQuantity;
    private List<String> foodPrice;

    public OrderDetailsAdapter(Context context, List<String> foodName, List<String> foodImage,
                               List<Integer> foodQuantity, List<String> foodPrice) {
        this.context = context;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodQuantity = foodQuantity;
        this.foodPrice = foodPrice;
    }

    public static class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        public OrderDetailsViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public OrderDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(OrderDetailsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return foodName.size(); // or any other list size
    }
}

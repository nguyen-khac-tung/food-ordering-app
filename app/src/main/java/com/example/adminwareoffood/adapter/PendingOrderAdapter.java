package com.example.adminwareoffood.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminwareoffood.databinding.PendingOrderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {

    //private final Context context;

    private final List<String> customerNames;
    private final List<String> quantity;
    private final List<Integer> foodImage;
    private final android.content.Context context;
    //private val itemClicked:onItemClicked


    public PendingOrderAdapter(ArrayList<String> customerNames, ArrayList<String> quantity, ArrayList<Integer> foodImage, android.content.Context context) {
        this.customerNames = customerNames;
        this.quantity = quantity;
        this.foodImage = foodImage;
        this.context = context;
    }


    @Override
    public PendingOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PendingOrderItemBinding binding = PendingOrderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PendingOrderViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        private final PendingOrderItemBinding binding;
        private boolean isAccept = false;

        public PendingOrderViewHolder(PendingOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            binding.customerName.setText(customerNames.get(position));
            binding.pendingOrderQuantity.setText(quantity.get(position));
//            binding.orderFoodImage.setImageResource(foodImage.get(position));
            //start
            String uriString = String.valueOf(foodImage.get(position));
            Uri uri = Uri.parse(uriString);
            Glide.with(context).load(uri).into(binding.orderFoodImage);


            binding.orderedAcceptionButton.setText(isAccept ? "Dispatch" : "Accept");
            binding.orderedAcceptionButton.setOnClickListener(v -> {
                if (!isAccept) {
                    isAccept = true;
                    binding.orderedAcceptionButton.setText("Dispatch");
                    showToast("Order is accepted");
                } else {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        customerNames.remove(adapterPosition);
                        quantity.remove(adapterPosition);
                        foodImage.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        showToast("Order is dispatched");
                    }
                }
            });
        }

        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.adminwareoffood.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adminwareoffood.databinding.DeliveryItemBinding;

import java.util.ArrayList;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private final ArrayList<String> customerNames;
    private final ArrayList<String> moneyStatus;

    public DeliveryAdapter(ArrayList<String> customerNames, ArrayList<String> moneyStatus) {
        this.customerNames = customerNames;
        this.moneyStatus = moneyStatus;
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeliveryItemBinding binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DeliveryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    class DeliveryViewHolder extends RecyclerView.ViewHolder {
        private final DeliveryItemBinding binding;

        DeliveryViewHolder(DeliveryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int position) {
            binding.customerName.setText(customerNames.get(position));
            binding.statusMoney.setText(moneyStatus.get(position));
            int color;
            switch (moneyStatus.get(position).toLowerCase()) {
                case "received":
                    color = Color.parseColor("#4caf50"); // green
                    break;
                case "pending":
                    color = Color.parseColor("#b3b3b3"); // gray
                    break;
                case "not received":
                    color = Color.parseColor("#ff3b3b"); // red
                    break;
                default:
                    color = Color.BLACK;
            }
            binding.statusMoney.setTextColor(color);
            binding.statusColor.setCardBackgroundColor(color);
        }
    }
}

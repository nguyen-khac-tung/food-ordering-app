package com.example.food_ordering_app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_ordering_app.MainActivity;
import com.example.food_ordering_app.databinding.FragmentCongratulationBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CongratulationFragment extends BottomSheetDialogFragment {

    private FragmentCongratulationBinding binding;

    public CongratulationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentCongratulationBinding.inflate(inflater, container, false);

       binding.goHomeButton.setOnClickListener(v -> {
           Intent intent = new Intent(requireContext(), MainActivity.class);
           startActivity(intent);
       });

       return  binding.getRoot();
    }
}
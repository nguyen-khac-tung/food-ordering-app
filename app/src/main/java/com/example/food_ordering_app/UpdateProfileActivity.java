package com.example.food_ordering_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_app.databinding.ActivityUpdateProfileBinding;
import com.example.food_ordering_app.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {
    private ActivityUpdateProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constants.FirebaseRef.USERS.name())
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    binding.editName.setText(user.getName());
                    binding.editPhone.setText(user.getPhone());
                    binding.editAddress.setText(user.getLocation());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Unable to load data.", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> updateUserInfo());
    }

    private void updateUserInfo() {
        String name = binding.editName.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();
        String address = binding.editAddress.getText().toString().trim();
        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all information.", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference(Constants.FirebaseRef.USERS.name())
                .child(userId);
        userRef.child("name").setValue(name);
        userRef.child("phone").setValue(phone);
        userRef.child("location").setValue(address)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UpdateProfileActivity.this, "Updated successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Update failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

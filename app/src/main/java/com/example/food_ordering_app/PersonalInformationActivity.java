package com.example.food_ordering_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.food_ordering_app.databinding.ActivityPersonalInfoBinding;
import com.example.food_ordering_app.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PersonalInformationActivity extends AppCompatActivity {
    private ActivityPersonalInfoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPersonalInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSnapshotUser(binding);
        binding.buttonBack.setOnClickListener(v -> finish());
        binding.btnUpdate.setOnClickListener(v -> {
            startActivity(new Intent(this, UpdateProfileActivity.class));
        });
    }

    private void getSnapshotUser(ActivityPersonalInfoBinding biding) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constants.FirebaseRef.USERS.name())
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    binding.textFullName.setText(user.getName());
                    binding.textEmail.setText(user.getEmail());
                    binding.textPhone.setText(user.getPhone());
                    binding.textRole.setText(user.getRole());
                    binding.textLocation.setText(user.getLocation());
                    binding.textStatus.setText(user.isActive() ? "Activity" : "Inactivity");
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(new Date(user.getCreatedAt()));
                    binding.textCreatedAt.setText(formattedDate);
                    Glide.with(PersonalInformationActivity.this)
                            .load(user.getProfileImage())
                            .placeholder(R.drawable.ic_user_default)
                            .into(binding.imageAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalInformationActivity.this, "Unable to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSnapshotUser(binding);
    }
}

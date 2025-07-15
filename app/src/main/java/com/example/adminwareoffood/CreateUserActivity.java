package com.example.adminwareoffood;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.food_ordering_app.databinding.ActivityCreateUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUserActivity extends AppCompatActivity {

    private ActivityCreateUserBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("USERS");

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.createUserButton.setOnClickListener(v -> {
            String name = binding.name.getText().toString().trim();
            String emailOrPhone = binding.emailOrPhone.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String location = ""; // Không có trường location trên layout
            String phone = "";    // Không có trường phone trên layout
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(emailOrPhone) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(emailOrPhone, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                usersRef.child(uid).setValue(new AdminUser(name, emailOrPhone, location, phone))
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        } else {Toast.makeText(this, "Create user failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Model cho user admin
    public static class AdminUser {
        public boolean active;
        public long createdAt;
        public String email, location, name, phone, profileImage, role;
        public AdminUser() {}
        public AdminUser(String name, String email, String location, String phone) {
            this.active = true;
            this.createdAt = System.currentTimeMillis();
            this.email = email;
            this.location = location;
            this.name = name;
            this.phone = phone;
            this.profileImage = "";
            this.role = "ROLE_USER";
        }
    }
}
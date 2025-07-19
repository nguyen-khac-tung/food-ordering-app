package com.example.adminwareoffood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminwareoffood.LoginActivity;
import com.example.adminwareoffood.model.UserAdmin;
import com.example.food_ordering_app.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference(Constants.FirebaseRef.ADMINS.name());

        String[] locationList = {"Vietnam", "Thailand", "Japan", "China", "Singapore"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                locationList
        );
        binding.listOfLocation.setAdapter(adapter);

        binding.createUserButton.setOnClickListener(v -> {
            String userName = binding.name.getText().toString().trim();
            String nameOfRestaurant = binding.restaurantName.getText().toString().trim();
            String email = binding.emailOrPhone.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String location = binding.listOfLocation.getText().toString().trim();

            if (userName.isEmpty() || nameOfRestaurant.isEmpty() || email.isEmpty() || password.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful() && auth.getCurrentUser() != null) {
                            String userId = auth.getCurrentUser().getUid();
                            UserAdmin userAdmin = new UserAdmin(userName, nameOfRestaurant, email, location, "ROLE_ADMIN");
                            database.child(userId).setValue(userAdmin)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Account created successfully as admin", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Failed to save user data: " + (dbTask.getException() != null ? dbTask.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            String errorMessage = "Authentication failed";
                            if (task.getException() != null) {
                                errorMessage += ": " + task.getException().getMessage();
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.alreadyHaveAccountButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}
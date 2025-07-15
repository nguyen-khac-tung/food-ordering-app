package com.example.adminwareoffood;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_app.databinding.ActivityAdminProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AdminProfileActivity extends AppCompatActivity {
    private ActivityAdminProfileBinding binding;
    private DatabaseReference adminRef;
    private String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy user hiện tại
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        adminId = user.getUid();
        adminRef = FirebaseDatabase.getInstance().getReference(Constants.FirebaseRef.ADMINS.name()).child(adminId);

        // Load thông tin admin
        loadAdminInfo();

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Ban đầu disable các trường
        setFieldsEnabled(false);

        // Bật/tắt chỉnh sửa khi nhấn edit
        binding.editButton.setOnClickListener(v -> {
            boolean isEnabled = !binding.name.isEnabled();
            setFieldsEnabled(isEnabled);
            if (isEnabled) {
                binding.name.requestFocus();
            }
        });

        // Lưu thông tin
        binding.saveInfomation.setOnClickListener(v -> {
            String name = binding.name.getText().toString();
            String address = binding.address.getText().toString();
            String email = binding.email.getText().toString();
            String phone = binding.phone.getText().toString();
            String password = binding.password.getText().toString();

            Map<String, Object> updates = new HashMap<>();
            updates.put("userName", name);
            updates.put("location", address);
            updates.put("email", email);
            updates.put("phone", phone);
            updates.put("password", password);

            adminRef.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show());
        });
    }

    private void setFieldsEnabled(boolean enabled) {
        binding.name.setEnabled(enabled);
        binding.address.setEnabled(enabled);
        binding.email.setEnabled(enabled);
        binding.phone.setEnabled(enabled);
        binding.password.setEnabled(enabled);
    }

    private void loadAdminInfo() {
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.name.setText(snapshot.child("userName").getValue(String.class));
                    binding.address.setText(snapshot.child("location").getValue(String.class));
                    binding.email.setText(snapshot.child("email").getValue(String.class));
                    binding.phone.setText(snapshot.child("phone").getValue(String.class));
                    binding.password.setText(snapshot.child("password").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminProfileActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
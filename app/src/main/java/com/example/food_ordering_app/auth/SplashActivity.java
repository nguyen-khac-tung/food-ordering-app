package com.example.food_ordering_app.auth;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_app.MainActivity;
import com.example.food_ordering_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            // Không có user trong cache, chưa đăng nhập
            if (currentUser == null) {
                navigateTo(LoginActivity.class);
            }
            // Nếu có người dùng trong cache, ta cần xác thực lại với server
            if(currentUser != null) {
                currentUser.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Tải lại thành công -> người dùng vẫn hợp lệ. Chuyển đến Main.
                        navigateTo(MainActivity.class);
                    } else {
                        // Tải lại thất bại -> người dùng bị vô hiệu hóa hoặc đã bị xóa.
                        FirebaseAuth.getInstance().signOut();
                        navigateTo(LoginActivity.class);
                    }
                });
            }

        }, SPLASH_DELAY);
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(SplashActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}

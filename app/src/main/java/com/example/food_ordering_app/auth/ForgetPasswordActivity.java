package com.example.food_ordering_app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_app.R;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";
    private FirebaseAuth mAuth;
    private EditText etForgotEmail;
    private static final String APP_URL = "https://prn392.firebaseapp.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mail);
        mAuth = FirebaseAuth.getInstance();
        etForgotEmail = findViewById(R.id.etForgotEmail);
        Button btnSendCode = findViewById(R.id.btnSendCode);
        btnSendCode.setOnClickListener(v -> sendPasswordResetEmail(etForgotEmail.getText().toString().trim()));
        TextView tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvBackToLogin.setOnClickListener(v -> startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class)));
    }

    private void sendPasswordResetEmail(String email) {
        if (email.isEmpty()) {
            etForgotEmail.setError("Email is required");
            etForgotEmail.requestFocus();
            return;
        }
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(APP_URL)
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                        "com.example.food_ordering_app",
                        true,
                        "12"
                )
                .build();
        mAuth.sendPasswordResetEmail(email, actionCodeSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email has been sent!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        Log.d(TAG, "sendEmail:success");
                    } else {
                        Toast.makeText(this, "Email could not be sent. Please try again.", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "sendEmail:failure");
                    }
                });
    }
}

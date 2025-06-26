package com.example.food_ordering_app.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_app.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class NewPasswordActivity extends AppCompatActivity {
    private static final String TAG = "EnterNewPasswordActivity";
    private FirebaseAuth mAuth;
    private EditText etNewPassword, etConfirmNewPassword;
    private TextInputLayout newPasswordInputField;
    private String oobCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        newPasswordInputField = findViewById(R.id.newPasswordInputField);
        Button btnResetPassword = findViewById((R.id.btnResetPassword));
        Uri data = getIntent().getData();
        if (data != null && data.getQueryParameter("oobCode") != null) {
            oobCode = data.getQueryParameter("oobCode");
            Log.d(TAG, "onCreateGetOobCode: success");
        } else {
            Toast.makeText(this, "Invalid reset link!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "onCreateGetOobCode: failure");
            finish();
        }
        btnResetPassword.setOnClickListener(v ->
                handleNewPassword(etNewPassword.getText().toString().trim(), etConfirmNewPassword.getText().toString().trim())
        );
    }

    private void handleNewPassword(String newPassword, String confirmPassword) {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance()
                .confirmPasswordReset(oobCode, newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password has been reset!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "handleNewPassword: success");
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Log.w(TAG, "handleNewPassword: failure");
                        String errorMessage;
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            errorMessage = "Password is too weak.";
                            newPasswordInputField.setError(errorMessage);
                            newPasswordInputField.setErrorIconDrawable(null);
                            newPasswordInputField.requestFocus();
                        } catch (Exception e) {
                            errorMessage = "Handle new password fail!";
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}

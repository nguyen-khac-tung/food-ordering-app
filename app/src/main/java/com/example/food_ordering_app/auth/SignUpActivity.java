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

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private TextInputLayout confirmPasswordInputField, passwordInputField;
    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        confirmPasswordInputField = findViewById(R.id.confirmPasswordInputField);
        passwordInputField = findViewById(R.id.passwordInputField);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(v -> registerUser());
        TextView tvAlreadyAccount = findViewById(R.id.tvAlreadyAccount);
        tvAlreadyAccount.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordInputField.setError("Password is required");
            confirmPasswordInputField.setErrorIconDrawable(null);
            passwordInputField.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordInputField.setError("Confirm Password is required");
            confirmPasswordInputField.setErrorIconDrawable(null);
            confirmPasswordInputField.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordInputField.setError("Passwords do not match");
            confirmPasswordInputField.setErrorIconDrawable(null);
            confirmPasswordInputField.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, createUserTask -> {
                    if (createUserTask.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmailAndPassword:success");
                        String uid = Objects.requireNonNull(createUserTask.getResult().getUser()).getUid();
                        User userData = new User(name, email);
                        DatabaseRef = FirebaseConfig.getDatabase().getReference(Constants.FirebaseRef.USERS.name());
                        DatabaseRef.child(uid).setValue(userData)
                                .addOnCompleteListener(saveUserDataTask -> {
                                    if (saveUserDataTask.isSuccessful()) {
                                        Log.d(TAG, "userDataSavedToRealtimeDatabase:success");
                                    } else {
                                        Log.e(TAG, "userDataSavedToRealtimeDatabase:failure", saveUserDataTask.getException());
                                    }
                                });
                        Toast.makeText(SignUpActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, ChooseLocationActivity.class));
                        finish();
                    } else {
                        Log.w(TAG, "createUserWithEmailAndPassword:failure", createUserTask.getException());
                        String errorMessage;
                        try {
                            throw Objects.requireNonNull(createUserTask.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            errorMessage = "Password is too weak.";
                            etPassword.setError(errorMessage);
                            etPassword.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            errorMessage = "Email address is invalid or incorrectly formatted.";
                            etEmail.setError(errorMessage);
                            etEmail.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e) {
                            errorMessage = "This email address already has an account.";
                            etEmail.setError(errorMessage);
                            etEmail.requestFocus();
                        } catch (Exception e) {
                            errorMessage = "Registration failed: " + e.getMessage();
                        }
                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}

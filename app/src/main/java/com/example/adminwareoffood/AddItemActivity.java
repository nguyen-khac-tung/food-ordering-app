package com.example.adminwareoffood;

import android.net.Uri;
import android.os.Bundle;
import android.view.View; // Added: Missing import for View.OnClickListener
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
// import androidx.activity.result.PickVisualMediaRequest; // Keep this if you intend to use PickVisualMedia later, otherwise remove
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminwareoffood.databinding.ActivityAddItemBinding;
import com.example.adminwareoffood.model.AllMenu;
// import com.google.firebase.Firebase; // This specific import is usually not needed directly

// Firebase Imports - Added
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddItemActivity extends AppCompatActivity {
    private ActivityAddItemBinding binding;
    // Changed: ActivityResultLauncher type for GetContent() should be for the input type (mime type string)
    private ActivityResultLauncher<String> pickImageLauncher;

    //food item details
    private String foodName; // Ensure this is assigned the value from the EditText
    private String foodPrice;
    private String foodDescription;
    private String foodIngredient;
    private Uri foodImageUri = null; // This will store the URI of the selected image

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance(); // Corrected: Initialize Firebase Auth
        database = FirebaseDatabase.getInstance(); // Corrected: Initialize Firebase Database

        // Set click listener for AddItemButton
        binding.AddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from input fields and assign to class members
                foodName = binding.foodName.getText().toString().trim();
                foodPrice = binding.foodPrice.getText().toString().trim();
                foodDescription = binding.foodDescription.getText().toString().trim();
                foodIngredient = binding.foodIngredient.getText().toString().trim();

                // Check if any field is empty
                if (!foodName.isEmpty() && !foodPrice.isEmpty() &&
                        !foodDescription.isEmpty() && !foodIngredient.isEmpty() && foodImageUri != null) { // Added: Check for foodImageUri

                    uploadData();
                    // Corrected: Use AddItemActivity.this for context
                    // Toast.makeText(AddItemActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    // finish(); // Consider moving finish() to after successful upload in uploadData()
                } else if (foodImageUri == null) {
                    Toast.makeText(AddItemActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddItemActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for select image button
        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Corrected: Launch with "image/*" for GetContent contract
                pickImageLauncher.launch("image/*");
            }
        });

        // Initialize ActivityResultLauncher for picking image
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), // Use GetContent for picking any content (filtered by mime type)
                uri -> {
                    if (uri != null) {
                        foodImageUri = uri; // Store the selected image URI
                        binding.selectedImage.setImageURI(uri);
                    }
                }
        );

        binding.backButton.setOnClickListener(v -> {
            // Replaced deprecated getOnBackPressedDispatcher().onBackPressed() with finish() for simple back navigation
            finish();
        });

        // The uploadData() method was incorrectly placed here.
        // It has been moved outside of onCreate()
    }

    // Moved uploadData() method here, outside of onCreate()
    private void uploadData() {
        // Get a reference to the "menu" node in the database
        DatabaseReference menuRef = database.getReference("menu");

        // Generate a unique key for the new menu item
        // Ensure newItemKey is final or effectively final if accessed in inner classes like listeners
        final String newItemKey = menuRef.push().getKey();

        if (foodImageUri != null && newItemKey != null) {
            // Show a progress indicator here if desired (e.g., ProgressBar)

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            // Corrected: Use a forward slash for directory structure in Storage
            StorageReference imageRef = storageRef.child("menu_images/" + newItemKey + ".jpg");

            UploadTask uploadTask = imageRef.putFile(foodImageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                    // Create new menu item
                    AllMenu newItem = new AllMenu(
                            foodName, // Uses the class member variable
                            foodPrice,
                            foodDescription,
                            foodIngredient, 
                            downloadUrl.toString()
                    );

                    menuRef.child(newItemKey).setValue(newItem)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AddItemActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Finish activity after successful upload
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AddItemActivity.this, "Data upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                // Hide progress indicator here
                            });
                }).addOnFailureListener(e -> {
                    Toast.makeText(AddItemActivity.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Hide progress indicator here
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(AddItemActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // Hide progress indicator here
            });
        } else {
            if (foodImageUri == null) {
                Toast.makeText(AddItemActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
            if (newItemKey == null) {
                Toast.makeText(AddItemActivity.this, "Failed to generate item key", Toast.LENGTH_SHORT).show();
            }
            // Hide progress indicator here if it was shown
        }
    }

    // If you intend to use the newer PickVisualMedia contract, your launcher and its call would look like this:
    /*
    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncherModern;

    // In onCreate:
    pickMediaLauncherModern = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        if (uri != null) {
            foodImageUri = uri;
            binding.selectedImage.setImageURI(uri);
        } else {
            // Log.d("PhotoPicker", "No media selected");
        }
    });

    // In selectImage.setOnClickListener:
    pickMediaLauncherModern.launch(new PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build());
    */
}
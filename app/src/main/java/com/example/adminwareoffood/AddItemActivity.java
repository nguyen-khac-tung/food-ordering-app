package com.example.adminwareoffood;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.food_ordering_app.databinding.ActivityAddItemBinding;
import com.example.adminwareoffood.model.MenuItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = "AddItemActivity";
    private ActivityAddItemBinding binding;
    private Uri selectedImageUri;
    private ActivityResultLauncher<PickVisualMediaRequest> pickImageLauncher;
    private DatabaseReference dbRef;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("FOODS");
        storageRef = FirebaseStorage.getInstance().getReference("menuImages");

        // Khởi tạo pickImageLauncher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        binding.selectedImage.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "Không chọn được ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Khởi tạo MediaManager với cấu hình Cloudinary
        Map config = new HashMap<>();
        config.put("cloud_name", "dak6p5n8s"); // Thay bằng cloud name thực tế của bạn
        config.put("api_key", "666987486165934"); // Thay bằng API key thực tế của bạn
        config.put("api_secret", "9m4AdssI33RSghnyUFqCTcCMNZ0"); // Thay bằng API secret thực tế của bạn
        MediaManager.init(this, config);

        // Thiết lập các sự kiện cho button
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.selectImage.setOnClickListener(v -> {
            pickImageLauncher.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.addItemButton.setOnClickListener(v -> addItemToFirebase());
    }

    private void addItemToFirebase() {
        String name = binding.enterFoodName.getText().toString().trim();
        String priceStr = binding.enterFoodPrice.getText().toString().trim();
        String description = binding.foodDescription.getText().toString().trim();
        String ingredients = binding.foodIngredient.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin và chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog dialog = ProgressDialog.show(this, "", "Đang tải lên...", true);

        // Tải ảnh lên Cloudinary
        MediaManager.get().upload(selectedImageUri).unsigned("prm_unsigned") // Thay bằng upload preset thực tế
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d(TAG, "Upload started: " + requestId);
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        Log.d(TAG, "Upload progress: " + bytes + "/" + totalBytes);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        runOnUiThread(() -> {
                            Log.d(TAG, "Upload success: " + resultData.toString());
                            String imageUrl = (String) resultData.get("secure_url");
                            if (imageUrl != null) {
                                String id = dbRef.push().getKey();
                                MenuItem item = new MenuItem(id, name, price, description, ingredients, imageUrl);
                                dbRef.child(id).setValue(item)
                                        .addOnSuccessListener(unused -> {
                                            dialog.dismiss();
                                            Toast.makeText(AddItemActivity.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            dialog.dismiss();
                                            Toast.makeText(AddItemActivity.this, "Thêm món thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(AddItemActivity.this, "Tải ảnh thất bại: Không nhận được URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        runOnUiThread(() -> {
                            Log.e(TAG, "Upload error: " + error.getDescription());
                            dialog.dismiss();
                            Toast.makeText(AddItemActivity.this, "Tải ảnh thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();
    }
}
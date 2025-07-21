package com.example.food_ordering_app.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.MainActivity;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseLocationActivity extends AppCompatActivity {
    private static final String TAG = "ChooseLocation";
    private TextView tvLocation;
    private Context context;
    private String selectedCity = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);
        LinearLayout locationInput = findViewById(R.id.locationInput);
        tvLocation = findViewById(R.id.tvLocation);
        context = this;
        Button btnFinish = findViewById(R.id.btnFinish);
        locationInput.setOnClickListener(v -> {
            List<String> cities = getCityListFromAsset(context);
            new AlertDialog.Builder(context)
                    .setTitle("Choose your location")
                    .setItems(cities.toArray(new String[0]), (dialog, which) -> {
                        selectedCity = cities.get(which);
                        tvLocation.setText(cities.get(which));
                    })
                    .show();
        });
        btnFinish.setOnClickListener(v -> {
            if (selectedCity == null) {
                Toast.makeText(context, "Please select a city first.", Toast.LENGTH_SHORT).show();
                return;
            }
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DatabaseReference userRef = FirebaseConfig.getDatabase()
                    .getReference(Constants.FirebaseRef.USERS.name())
                    .child(uid)
                    .child("location");
            userRef.setValue(selectedCity.trim())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "chooseLocation: " + selectedCity);
                            Toast.makeText(context, "Location saved successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChooseLocationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.d(TAG, "chooseLocation: " + selectedCity);
                            Toast.makeText(context, "Error saving location.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    public List<String> getCityListFromAsset(Context context) {
        List<String> cities = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.vietnam_cities);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                cities.add(jsonArray.getString(i));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }
}

package com.example.adminwareoffood;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminwareoffood.adapter.AddItemAdapter;
import com.example.adminwareoffood.model.MenuItem;
import com.example.food_ordering_app.databinding.ActivityAllItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllItemActivity extends AppCompatActivity {
    private ActivityAllItemBinding binding;
    private List<MenuItem> itemList;
    private AddItemAdapter adapter;
    private DatabaseReference dbRef;
    private String lastKey = null;
    private static final int ITEMS_PER_PAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAllItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        itemList = new ArrayList<>();

        adapter = new AddItemAdapter(itemList, item -> {
            dbRef.child(item.getId()).removeValue()
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(AllItemActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(AllItemActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show());
        });

        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.menuRecyclerView.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("FOODS");
        loadItemsFromFirebase();

        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.loadMoreButton.setOnClickListener(v -> loadMoreItems());
    }

    private void loadItemsFromFirebase() {
        Query query = dbRef.orderByKey().limitToFirst(ITEMS_PER_PAGE);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    itemList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MenuItem item = data.getValue(MenuItem.class);
                        if (item != null) {
                            itemList.add(item);
                        }
                    }
                    // Cập nhật key cuối
                    for (DataSnapshot child : snapshot.getChildren()) {
                        lastKey = child.getKey();
                    }
                    adapter.notifyDataSetChanged();
                    toggleLoadMoreButton(snapshot.getChildrenCount() >= ITEMS_PER_PAGE);
                } else {
                    toggleLoadMoreButton(false);
                    Toast.makeText(AllItemActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AllItemActivity", "Lỗi: " + error.getMessage());
                toggleLoadMoreButton(false);
            }
        });
    }

    private void loadMoreItems() {
        if (lastKey == null) {
            Toast.makeText(this, "Không có thêm dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }
        Query query = dbRef.orderByKey().startAfter(lastKey).limitToFirst(ITEMS_PER_PAGE);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MenuItem item = data.getValue(MenuItem.class);
                        if (item != null) {
                            itemList.add(item);
                        }
                    }
                    for (DataSnapshot child : snapshot.getChildren()) {
                        lastKey = child.getKey();
                    }
                    adapter.notifyDataSetChanged();
                    toggleLoadMoreButton(snapshot.getChildrenCount() >= ITEMS_PER_PAGE);
                } else {
                    lastKey = null;
                    toggleLoadMoreButton(false);
                    Toast.makeText(AllItemActivity.this, "Đã tải hết dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AllItemActivity", "Lỗi: " + error.getMessage());
            }
        });
    }

    private void toggleLoadMoreButton(boolean enable) {
        binding.loadMoreButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
}

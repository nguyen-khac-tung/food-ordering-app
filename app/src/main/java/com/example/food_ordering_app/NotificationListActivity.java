package com.example.food_ordering_app;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_app.adapter.NotificationAdapter;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private final List<Notification> notificationList = new ArrayList<>();
    private DatabaseReference notiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                String uid = FirebaseAuth.getInstance().getUid();
                if (uid != null && pos < notificationList.size()) {
                    Notification noti = notificationList.get(pos);
                    FirebaseConfig.getDatabase()
                            .getReference(Constants.FirebaseRef.NOTIFICATIONS.name())
                            .child(uid)
                            .orderByChild("timestamp")
                            .equalTo(noti.getTimestamp())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        child.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                    adapter.removeItem(pos);
                }
            }
        }).attachToRecyclerView(recyclerView);
        loadNotifications();
        ImageButton backBtn = findViewById(R.id.buttonB);
        backBtn.setOnClickListener(v -> finish());

    }

    private void loadNotifications() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;
        notiRef = FirebaseConfig.getDatabase()
                .getReference(Constants.FirebaseRef.NOTIFICATIONS.name())
                .child(uid);
        notiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Notification noti = snap.getValue(Notification.class);
                    if (noti != null) notificationList.add(noti);
                }
                Collections.sort(notificationList, (a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

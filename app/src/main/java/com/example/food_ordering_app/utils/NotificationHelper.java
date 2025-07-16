package com.example.food_ordering_app.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.food_ordering_app.Constants;
import com.example.food_ordering_app.R;
import com.example.food_ordering_app.config.FirebaseConfig;
import com.example.food_ordering_app.models.Notification;
import com.google.firebase.database.DatabaseReference;

public class NotificationHelper {

    private static final String CHANNEL_ID = "order_status_channel";
    private static final DatabaseReference databaseRef = FirebaseConfig.getDatabase().getReference(Constants.FirebaseRef.NOTIFICATIONS.name());

    public static void showOrderStatusNotification(Context context, Notification notification) {
        createNotificationChannel(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_history)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notification.getOrderId().hashCode(), builder.build());
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Updates";
            String description = "Order status notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void saveNotificationForUser(String userId, Notification notification) {
        String notiId = databaseRef.child(userId).push().getKey();
        if (notiId == null) return;
        databaseRef.child(userId).child(notiId).setValue(notification)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseNoti", "Notification saved"))
                .addOnFailureListener(e -> Log.e("FirebaseNoti", "Failed: " + e.getMessage()));
    }

    public static void notifyAndSave(Context context, String userId, Notification notification) {
        showOrderStatusNotification(context, notification);
        saveNotificationForUser(userId, notification);
    }

}

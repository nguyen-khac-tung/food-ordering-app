package com.example.food_ordering_app.config;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {
    private static final String DATABASE_URL = "https://prn392-default-rtdb.asia-southeast1.firebasedatabase.app";
    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance(DATABASE_URL);
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}

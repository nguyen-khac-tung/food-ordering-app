package com.example.adminwareoffood;

public class Constants {

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER
    }

    public enum StatusOrder {
        PENDING("Pending"),
        CONFIRMED("Confirmed"),
        DELIVERING("Delivering"),
        COMPLETED("Completed");

        private final String displayName;

        StatusOrder(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum FirebaseRef {
        USERS,
        ADMINS,
        FOODS,
        CARTS,
        ORDERS;
    }
}

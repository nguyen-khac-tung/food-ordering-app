package com.example.food_ordering_app;

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
        public static StatusOrder fromValue(String value) {
            for (StatusOrder status : values()) {
                if (status.getDisplayName().equalsIgnoreCase(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    public enum FirebaseRef {
        USERS,
        ADMINS,
        FOODS,
        CARTS,
        ORDERS;
    }
}


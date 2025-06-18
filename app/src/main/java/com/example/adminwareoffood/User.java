package com.example.adminwareoffood;

public class User {
    public String userName;
    public String nameOfRestaurant;
    public String email;
    public String location;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String nameOfRestaurant, String email, String location) {
        this.userName = userName;
        this.nameOfRestaurant = nameOfRestaurant;
        this.email = email;
        this.location = location;
    }
}
package com.example.adminwareoffood.model;

import com.example.adminwareoffood.Constants;

public class UserAdmin {
    public String userName;
    public String nameOfRestaurant;
    public String email;
    public String location;
    public String role;

    public String phone;


    public UserAdmin() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserAdmin(String nameOfRestaurant, String userName, String email, String location, String role) {
        this.nameOfRestaurant = nameOfRestaurant;
        this.userName = userName;
        this.email = email;
        this.location = location;
        this.role = Constants.Role.ROLE_ADMIN.name();
        this.phone = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNameOfRestaurant() {
        return nameOfRestaurant;
    }

    public void setNameOfRestaurant(String nameOfRestaurant) {
        this.nameOfRestaurant = nameOfRestaurant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
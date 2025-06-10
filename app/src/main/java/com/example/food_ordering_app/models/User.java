package com.example.food_ordering_app.models;

import com.example.food_ordering_app.Constants;

public class User {
    private String name;
    private String email;
    private String phone;
    private String profileImage;
    private String role;
    private String location;
    private boolean isActive;
    private long createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.phone = "";
        this.profileImage = "";
        this.role = Constants.Role.ROLE_USER.name();
        this.location = "";
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
    }

    public User(String name, String email, String location) {
        this.name = name;
        this.email = email;
        this.phone = "";
        this.profileImage = "";
        this.role = Constants.Role.ROLE_USER.name();
        this.location = location;
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
    }
}


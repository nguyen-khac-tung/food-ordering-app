package com.example.food_ordering_app.models;

import com.google.firebase.Timestamp;

public class Notification {
    private String title;
    private String message;
    private String orderId;
    private Long timestamp;
    private Boolean isRead;

    public Notification() {
    }

    public Notification(String title, String message, String orderId, Long timestamp, boolean isRead) {
        this.title = title;
        this.message = message;
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

package com.example.adminwareoffood.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private String userName;
    private String userAddress;
    private String userPhone;
    private long orderDate;
    private int totalAmount;
    private String status;

    private String paymentStatus; // Optional field for payment status
    private List<Cart> items;

    public Order() {
    }

    public Order(String orderId, List<Cart> items, String paymentStatus, String status, int totalAmount, long orderDate, String userPhone, String userAddress, String userName, String userId) {
        this.orderId = orderId;
        this.items = items;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userName = userName;
        this.userId = userId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserAddress() { return userAddress; }
    public void setUserAddress(String userAddress) { this.userAddress = userAddress; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Cart> getItems() { return items; }
    public void setItems(List<Cart> items) { this.items = items; }
}
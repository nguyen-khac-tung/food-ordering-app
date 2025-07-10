package com.example.food_ordering_app.models;

public class Cart {
    private String foodId;
    private String foodName;
    private int foodPrice;
    private String foodImageUrl;
    private int quantity;

    public Cart() {
    }

    public Cart(String foodId, String foodName, int foodPrice, String foodImageUrl, int quantity) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImageUrl = foodImageUrl;
        this.quantity = quantity;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }


    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodImageUrl(String foodImageUrl) {
        this.foodImageUrl = foodImageUrl;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
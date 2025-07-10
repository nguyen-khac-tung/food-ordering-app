package com.example.food_ordering_app.models;

import java.io.Serializable;

public class Food implements Serializable {
    private String foodId;
    private String foodName;
    private String foodDescription;
    private int foodPrice;
    private String foodImageUrl;
    private String foodIngredients;

    public Food() {
    }

    public Food(String foodName, String foodDescription, int foodPrice, String foodImageUrl, String foodIngredients) {
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodImageUrl = foodImageUrl;
        this.foodIngredients = foodIngredients;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public String getFoodIngredients() {
        return foodIngredients;
    }

}
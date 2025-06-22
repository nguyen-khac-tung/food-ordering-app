package com.example.adminwareoffood.model;

public class AllMenu {
    private String foodName = null;
    private String foodPrice = null;
    private String foodDescription = null;
    private String foodImage = null;
    private String foodIngredient = null;

    public AllMenu() {
        // Default constructor required for calls to DataSnapshot.getValue(AllMenu.class)
    }
    public AllMenu(String foodName, String foodPrice, String foodDescription, String foodImage, String foodIngredient) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
        this.foodIngredient = foodIngredient;
    }

}

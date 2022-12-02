package com.foodies.mealplanner.model;

import java.math.BigDecimal;

/**
 * Model class for meals
 */
public class Meal {

    private String mealName;
    private String mealDescription;
    private String mealIngredients;
    private String mealType;
    private BigDecimal mealPrice;
    private String mealStatus;

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public String getMealIngredients() {
        return mealIngredients;
    }

    public void setMealIngredients(String mealIngredients) {
        this.mealIngredients = mealIngredients;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public BigDecimal getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(BigDecimal mealPrice) {
        
        this.mealPrice = mealPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getMealStatus() {
        return mealStatus;
    }

    public void setMealStatus(String mealStatus) {
        this.mealStatus = mealStatus;
    }
}

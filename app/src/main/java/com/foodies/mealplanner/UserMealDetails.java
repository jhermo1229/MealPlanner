package com.foodies.mealplanner;

/**
 * Model class for user meals chosen by the user.
 */
public class UserMealDetails {

    private Integer deliveryFrequency;
    private String mealType;
    private Integer mealQuantity;

    public Integer getDeliveryFrequency() {
        return deliveryFrequency;
    }

    public void setDeliveryFrequency(Integer deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Integer getMealQuantity() {
        return mealQuantity;
    }

    public void setMealQuantity(Integer mealQuantity) {
        this.mealQuantity = mealQuantity;
    }

}

package com.foodies.mealplanner.model;

/**
 * This is the model for email
 *
 * @author herje
 * @version 1
 */
public class Email {


    private String deliveryDate;
    private Boolean isSent;
    private MealPlanWeek mealPlanWeek;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public MealPlanWeek getMealPlanWeek() {
        return mealPlanWeek;
    }

    public void setMealPlanWeek(MealPlanWeek mealPlanWeek) {
        this.mealPlanWeek = mealPlanWeek;
    }
}

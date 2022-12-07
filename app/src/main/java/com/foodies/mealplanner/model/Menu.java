package com.foodies.mealplanner.model;

/**
 * Model for menu
 *
 *  @author herje
 *  @version 1
 */
public class Menu {

    private String menuName;
    private Meal meatMeal;
    private Meal vegetableMeal;
    private Meal bothMeal;
    private String imageUrl;


    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Meal getMeatMeal() {
        return meatMeal;
    }

    public void setMeatMeal(Meal meatMeal) {
        this.meatMeal = meatMeal;
    }

    public Meal getVegetableMeal() {
        return vegetableMeal;
    }

    public void setVegetableMeal(Meal vegetableMeal) {
        this.vegetableMeal = vegetableMeal;
    }

    public Meal getBothMeal() {
        return bothMeal;
    }

    public void setBothMeal(Meal bothMeal) {
        this.bothMeal = bothMeal;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

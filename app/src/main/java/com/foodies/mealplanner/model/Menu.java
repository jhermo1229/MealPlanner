package com.foodies.mealplanner.model;

/**
 * Model for menu
 */
public class Menu {

    private String menuName;
    private Meal meatMeal;
    private Meal vegetableMeal;
    private Meal bothMeal;


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
}

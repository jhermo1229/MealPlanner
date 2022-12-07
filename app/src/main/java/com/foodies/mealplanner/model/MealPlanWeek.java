package com.foodies.mealplanner.model;

import java.util.List;

/**
 * Model for the final meal plan to be sent to the customers
 *
 *
 * @author herje
 * @version 1
 */
public class MealPlanWeek {

    private List<String> emailAddressList;
    private Menu mondayMenu;
    private Menu wednesdayMenu;
    private Menu fridayMenu;
    private String notes;

    public List<String> getEmailAddressList() {
        return emailAddressList;
    }

    public void setEmailAddressList(List<String> emailAddressList) {
        this.emailAddressList = emailAddressList;
    }

    public Menu getMondayMenu() {
        return mondayMenu;
    }

    public void setMondayMenu(Menu mondayMenu) {
        this.mondayMenu = mondayMenu;
    }

    public Menu getWednesdayMenu() {
        return wednesdayMenu;
    }

    public void setWednesdayMenu(Menu wednesdayMenu) {
        this.wednesdayMenu = wednesdayMenu;
    }

    public Menu getFridayMenu() {
        return fridayMenu;
    }

    public void setFridayMenu(Menu fridayMenu) {
        this.fridayMenu = fridayMenu;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

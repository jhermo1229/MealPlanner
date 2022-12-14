package com.foodies.mealplanner.Interface;

import com.foodies.mealplanner.model.Meal;

/**
 * Call back for Meal - since firestore is asynchronous, this is a workaround
 * Will wait for Meal to be fetch then callback will be called back to the view.
 * Strategy Design Pattern
 * @author herje
 * @version 1
 */
public interface MealNameCallBack {

    void onCallBack(Meal meal);
}

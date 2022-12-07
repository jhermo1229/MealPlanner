package com.foodies.mealplanner.Interface;

import com.foodies.mealplanner.model.Meal;

import java.util.List;

/**
 * Call back for List of Meal - since firestore is asynchronous, this is a workaround
 * Will wait for Meal to be fetch then callback will be called back to the view.
 * @author herje
 * @version 1
 */
public interface MealListCallBack {
    void onCallBack(List<Meal> mealList);
}

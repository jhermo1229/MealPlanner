package com.foodies.mealplanner.Interface;

import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.Menu;

import java.util.List;

public interface MenuListCallBack {

    void onCallBack(List<Menu> menuList);
}

package com.foodies.mealplanner.Interface;

import com.foodies.mealplanner.model.Menu;

import java.util.List;

/**
 * Call back for menu - since firestore is asynchronous, this is a workaround
 * Will wait for menu to be fetch then callback will be called back to the view.
 * Strategy Design Pattern
 *
 * @author herje
 * @version 1
 */
public interface MenuListCallBack {

    void onCallBack(List<Menu> menuList);
}

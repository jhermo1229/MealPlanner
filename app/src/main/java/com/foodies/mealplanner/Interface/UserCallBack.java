package com.foodies.mealplanner.Interface;

import com.foodies.mealplanner.model.User;

/**
 * Call back for user - since firestore is asynchronous, this is a workaround
 * Will wait for user to be fetch then callback will be called back to the view.
 * @author herje
 * @version 1
 */
public interface UserCallBack {
    void onCallBack(User user);

}

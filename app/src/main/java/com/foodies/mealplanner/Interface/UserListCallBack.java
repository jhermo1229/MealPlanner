package com.foodies.mealplanner.Interface;

import com.foodies.mealplanner.model.User;

import java.util.List;

/**
 * Call back for list of users - since firestore is asynchronous, this is a workaround
 * Will wait for user to be fetch then callback will be called back to the view.
 * @author herje
 * @version 1
 */
public interface UserListCallBack {

    void onCallBack(List<User> userList);
}

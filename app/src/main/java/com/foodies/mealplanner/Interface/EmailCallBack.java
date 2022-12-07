package com.foodies.mealplanner.Interface;

import com.foodies.mealplanner.model.Email;

/**
 * Call back for email - since firestore is asynchronous, this is a workaround
 * Will wait for email to be fetch then callback will be called back to the view.
 * @author herje
 * @version 1
 */
public interface EmailCallBack {

    void onCallBack(Email email);
}

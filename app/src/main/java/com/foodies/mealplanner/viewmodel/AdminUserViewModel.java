package com.foodies.mealplanner.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foodies.mealplanner.model.User;

/**
 * View model for admin - admin focused.
 * @author herje
 * @version 1
 */
public class AdminUserViewModel extends ViewModel {

    //Create a live mutable data
    private final MutableLiveData<User> selectedItem = new MutableLiveData<>();

    public AdminUserViewModel() {
        Log.i("AdminUserViewModel", "AdminUserViewModel is created");
    }

    public LiveData<User> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(User user) {
        selectedItem.setValue(user);
    }
}

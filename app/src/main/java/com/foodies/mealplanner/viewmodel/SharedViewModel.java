package com.foodies.mealplanner.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foodies.mealplanner.model.User;

/**
 * View Model class to share User model to different fragments at SignupActivity
 */
public class SharedViewModel extends ViewModel {

    //Create a live mutable data
    private final MutableLiveData<User> selectedItem = new MutableLiveData<>();

    public SharedViewModel() {
        Log.i("SharedViewModel", "ViewModel is created");
    }

    public LiveData<User> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(User user) {
        selectedItem.setValue(user);
    }
}

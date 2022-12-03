package com.foodies.mealplanner.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foodies.mealplanner.model.User;

public class CustomerViewModel extends ViewModel {

    //Create a live mutable data
    private final MutableLiveData<User> selectedItem = new MutableLiveData<>();

    public CustomerViewModel() {
        Log.i("CustomerViewModel", "CustomerViewModel is created");
    }

    public LiveData<User> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(User user) {
        selectedItem.setValue(user);
    }
}

package com.example.mealplanner;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<User> selectedItem = new MutableLiveData<>();

    public SharedViewModel() {
        Log.i("SharedViewModel", "ViewModel is created");
    }

    protected void onCleared() {
        super.onCleared();
        Log.i("SharedViewModel", "ViewModel is destroyed");
    }

    public LiveData<User> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(User user) {
        selectedItem.setValue(user);
    }
}

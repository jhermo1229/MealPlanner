package com.foodies.mealplanner.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foodies.mealplanner.model.Meal;

public class MealViewModel extends ViewModel {

    //Create a live mutable data
    private final MutableLiveData<Meal> selectedItem = new MutableLiveData<>();

    public MealViewModel() {
        Log.i("MealViewModel", "MealViewModel is created");
    }

    public LiveData<Meal> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Meal meal) {
        selectedItem.setValue(meal);
    }
}
package com.foodies.mealplanner.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foodies.mealplanner.model.Menu;

/**
 * View model for menu - menu focused.
 * @author herje
 * @version 1
 */
public class MenuViewModel extends ViewModel {

    //Create a live mutable data
    private final MutableLiveData<Menu> selectedItem = new MutableLiveData<>();

    public MenuViewModel() {
        Log.i("MenuViewModel", "MenuViewModel is created");
    }

    public LiveData<Menu> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Menu menu) {
        selectedItem.setValue(menu);
    }
}

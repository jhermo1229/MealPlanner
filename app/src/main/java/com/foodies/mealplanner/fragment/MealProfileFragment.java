package com.foodies.mealplanner.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.viewmodel.MealProfileViewModel;

public class MealProfileFragment extends Fragment {

    private MealProfileViewModel mViewModel;

    public static MealProfileFragment newInstance() {
        return new MealProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_profile, container, false);
    }

}
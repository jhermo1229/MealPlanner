package com.example.mealplanner;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MealTypeFragment extends Fragment {

    public static final String TAG = MealTypeFragment.class.getName();
    private Button vegetableBtn, meatBtn, bothBtn, nextBtn;
    private View mealTypeView;

    public MealTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mealTypeView = inflater.inflate(R.layout.fragment_meal_type, container, false);
        vegetableBtn = mealTypeView.findViewById(R.id.oneButton);
        meatBtn = mealTypeView.findViewById(R.id.twoButton);
        bothBtn = mealTypeView.findViewById(R.id.threeButton);

        vegetableBtn.setOnClickListener((mealsDeliveryView) -> {

            vegetableBtn.setBackgroundColor(Color.RED);
            meatBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            bothBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        meatBtn.setOnClickListener((mealsDeliveryView) -> {

            meatBtn.setBackgroundColor(Color.RED);
            vegetableBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            bothBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        bothBtn.setOnClickListener((mealsDeliveryView) -> {

            bothBtn.setBackgroundColor(Color.RED);
            meatBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            vegetableBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        return mealTypeView;
    }
}
package com.example.mealplanner;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MealsDeliveryRateFragment extends Fragment {

    public static String TAG = MealsDeliveryRateFragment.class.getName();
    private Button oneBtn, twoBtn, threeBtn, onePersonBtn, twoPersonBtn, threePersonBtn, nextBtn;
    private View mealsDeliveryView;

    public MealsDeliveryRateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mealsDeliveryView = inflater.inflate(R.layout.fragment_meals_delivery_rate, container, false);
        oneBtn = mealsDeliveryView.findViewById(R.id.oneButton);
        twoBtn = mealsDeliveryView.findViewById(R.id.twoButton);
        threeBtn = mealsDeliveryView.findViewById(R.id.threeButton);

        oneBtn.setOnClickListener((mealsDeliveryView) -> {

            oneBtn.setBackgroundColor(Color.RED);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        twoBtn.setOnClickListener((mealsDeliveryView) -> {

            twoBtn.setBackgroundColor(Color.RED);
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        threeBtn.setOnClickListener((mealsDeliveryView) -> {

            threeBtn.setBackgroundColor(Color.RED);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        onePersonBtn = mealsDeliveryView.findViewById(R.id.onePersonButton);
        twoPersonBtn = mealsDeliveryView.findViewById(R.id.twoPersonButton);
        threePersonBtn = mealsDeliveryView.findViewById(R.id.threePersonButton);

        onePersonBtn.setOnClickListener((mealsDeliveryView) -> {

            onePersonBtn.setBackgroundColor(Color.RED);
            twoPersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            threePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        twoPersonBtn.setOnClickListener((mealsDeliveryView) -> {

            twoPersonBtn.setBackgroundColor(Color.RED);
            onePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            threePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        threePersonBtn.setOnClickListener((mealsDeliveryView) -> {

            threePersonBtn.setBackgroundColor(Color.RED);
            twoPersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
            onePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        });

        nextBtn = mealsDeliveryView.findViewById(R.id.nextButton);

        nextBtn.setOnClickListener((mealsDeliveryView) -> {
            MealTypeFragment mealTypeFrag = new MealTypeFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MealTypeFragment.TAG);
            transaction.replace(R.id.homeFrame, mealTypeFrag);

            transaction.commit();
        });

        return mealsDeliveryView;
    }
}
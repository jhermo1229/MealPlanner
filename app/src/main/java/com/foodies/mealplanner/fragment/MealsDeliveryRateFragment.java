package com.foodies.mealplanner.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.viewmodel.SharedViewModel;
import com.foodies.mealplanner.model.UserMealDetails;


public class MealsDeliveryRateFragment extends Fragment {

    public static String TAG = MealsDeliveryRateFragment.class.getName();
    private Button oneBtn, twoBtn, threeBtn, onePersonBtn, twoPersonBtn, threePersonBtn, nextBtn;
    private View mealsDeliveryView;
    private SharedViewModel sharedViewModel;
    private UserMealDetails userMealDetails =new UserMealDetails();

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

        oneBtn.setBackgroundColor(Color.RED);
        userMealDetails.setDeliveryFrequency(1);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        oneBtn.setOnClickListener((mealsDeliveryView) -> {

            oneBtn.setBackgroundColor(Color.RED);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        twoBtn.setOnClickListener((mealsDeliveryView) -> {

            twoBtn.setBackgroundColor(Color.RED);
            userMealDetails.setDeliveryFrequency(2);
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        threeBtn.setOnClickListener((mealsDeliveryView) -> {

            threeBtn.setBackgroundColor(Color.RED);
            userMealDetails.setDeliveryFrequency(3);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        onePersonBtn = mealsDeliveryView.findViewById(R.id.onePersonButton);
        twoPersonBtn = mealsDeliveryView.findViewById(R.id.twoPersonButton);
        threePersonBtn = mealsDeliveryView.findViewById(R.id.threePersonButton);
        onePersonBtn.setBackgroundColor(Color.RED);
        userMealDetails.setMealQuantity(1);

        onePersonBtn.setOnClickListener((mealsDeliveryView) -> {

            onePersonBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealQuantity(1);
            twoPersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        twoPersonBtn.setOnClickListener((mealsDeliveryView) -> {

            twoPersonBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealQuantity(2);
            onePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        threePersonBtn.setOnClickListener((mealsDeliveryView) -> {

            threePersonBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealQuantity(3);
            twoPersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            onePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        nextBtn = mealsDeliveryView.findViewById(R.id.nextButton);

        nextBtn.setOnClickListener((mealsDeliveryView) -> {
            MealTypeFragment mealTypeFrag = new MealTypeFragment();
            sharedViewModel.getSelectedItem().observe(getActivity(), users -> {
                users.setUserMealDetails(userMealDetails);
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MealTypeFragment.TAG);
            transaction.replace(R.id.signupHomeFrame, mealTypeFrag);

            transaction.commit();
        });

        return mealsDeliveryView;
    }
}
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


public class MealTypeFragment extends Fragment {

    public static final String TAG = MealTypeFragment.class.getName();
    private Button vegetableBtn, meatBtn, bothBtn, nextBtn;
    private View mealTypeView;
    private SharedViewModel sharedViewModel;
    private UserMealDetails userMealDetails =new UserMealDetails();

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
        userMealDetails.setMealType("B");
        bothBtn.setBackgroundColor(Color.RED);

        vegetableBtn.setOnClickListener((mealsDeliveryView) -> {

            vegetableBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealType("V");
            meatBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            bothBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        meatBtn.setOnClickListener((mealsDeliveryView) -> {

            meatBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealType("M");
            vegetableBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            bothBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        bothBtn.setOnClickListener((mealsDeliveryView) -> {

            bothBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealType("B");
            meatBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            vegetableBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        nextBtn = mealTypeView.findViewById(R.id.nextButton);

        nextBtn.setOnClickListener((mealsDeliveryView) -> {
            PaymentDetailsFragment paymentDetailsFrag = new PaymentDetailsFragment();

            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
            sharedViewModel.getSelectedItem().observe(getActivity(), users -> {
                users.getUserMealDetails().setMealType(userMealDetails.getMealType());
            });

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(PaymentDetailsFragment.TAG);
            transaction.replace(R.id.signupHomeFrame, paymentDetailsFrag);

            transaction.commit();
        });

        return mealTypeView;
    }
}
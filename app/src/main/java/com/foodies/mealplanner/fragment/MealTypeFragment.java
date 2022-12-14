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
import com.foodies.mealplanner.viewmodel.SignupViewModel;
import com.foodies.mealplanner.model.UserMealDetails;

/**
 * Fragment for meal type
 * @author herje
 * @version 1
 */
public class MealTypeFragment extends Fragment {

    public static final String TAG = MealTypeFragment.class.getName();
    public static final String BOTH = "B";
    public static final String VEGETABLE = "V";
    public static final String MEAT = "M";
    private Button vegetableBtn, meatBtn, bothBtn, nextBtn;
    private View mealTypeView;
    private SignupViewModel signupViewModel;
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

        //Set values of button to default
        userMealDetails.setMealType(BOTH);
        bothBtn.setBackgroundColor(Color.RED);

        //Observer Design Pattern
        //if button is clicked set button to red and set other button to default color
        //set meal to VEGETABLE
        vegetableBtn.setOnClickListener((mealsDeliveryView) -> {

            vegetableBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealType(VEGETABLE);
            meatBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            bothBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        //Observer Design Pattern
        //if button is clicked set button to red and set other button to default color
        //set meal to MEAT
        meatBtn.setOnClickListener((mealsDeliveryView) -> {

            meatBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealType(MEAT);
            vegetableBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            bothBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        //Observer Design Pattern
        //if button is clicked set button to red and set other button to default color
        //set meal to BOTH (MEAT AND VEGETABLE)
        bothBtn.setOnClickListener((mealsDeliveryView) -> {

            bothBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealType(BOTH);
            meatBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            vegetableBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        nextBtn = mealTypeView.findViewById(R.id.personalNextButton);

        //Observer Design Pattern
        nextBtn.setOnClickListener((mealTypeView) -> {
            PaymentDetailsFragment paymentDetailsFrag = new PaymentDetailsFragment();

            signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
            signupViewModel.getSelectedItem().observe(getActivity(), users -> {
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
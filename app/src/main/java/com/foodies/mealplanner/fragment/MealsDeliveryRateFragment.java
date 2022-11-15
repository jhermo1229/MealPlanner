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
 * Fragment for meal delivery rate
 */
public class MealsDeliveryRateFragment extends Fragment {

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static String TAG = MealsDeliveryRateFragment.class.getName();
    private Button oneBtn, twoBtn, threeBtn, onePersonBtn, twoPersonBtn, threePersonBtn, nextBtn;
    private View mealsDeliveryView;
    private SignupViewModel signupViewModel;
    private UserMealDetails userMealDetails =new UserMealDetails();

    public MealsDeliveryRateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mealsDeliveryView = inflater.inflate(R.layout.fragment_meals_delivery_rate, container, false);

        //Delivery Frequency
        oneBtn = mealsDeliveryView.findViewById(R.id.oneButton);
        twoBtn = mealsDeliveryView.findViewById(R.id.twoButton);
        threeBtn = mealsDeliveryView.findViewById(R.id.threeButton);

        //Meal quantity
        onePersonBtn = mealsDeliveryView.findViewById(R.id.onePersonButton);
        twoPersonBtn = mealsDeliveryView.findViewById(R.id.twoPersonButton);
        threePersonBtn = mealsDeliveryView.findViewById(R.id.threePersonButton);

        //set the buttons value to default
        setDefaultValues();

        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        //if button is clicked set button to red and set other button to default color
        //set delivery frequency to button number
        oneBtn.setOnClickListener((mealsDeliveryView) -> {
            userMealDetails.setDeliveryFrequency(ONE);
            oneBtn.setBackgroundColor(Color.RED);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        //if button is clicked set button to red and set other button to default color
        //set delivery frequency to button number
        twoBtn.setOnClickListener((mealsDeliveryView) -> {

            twoBtn.setBackgroundColor(Color.RED);
            userMealDetails.setDeliveryFrequency(TWO);
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threeBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        //if button is clicked set button to red and set other button to default color
        //set delivery frequency to button number
        threeBtn.setOnClickListener((mealsDeliveryView) -> {

            threeBtn.setBackgroundColor(Color.RED);
            userMealDetails.setDeliveryFrequency(THREE);
            twoBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            oneBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        onePersonBtn.setBackgroundColor(Color.RED);
        userMealDetails.setMealQuantity(ONE);

        //if button is clicked set button to red and set other button to default color
        //set meal quantity to button number
        onePersonBtn.setOnClickListener((mealsDeliveryView) -> {

            onePersonBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealQuantity(ONE);
            twoPersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        //if button is clicked set button to red and set other button to default color
        //set meal quantity to button number
        twoPersonBtn.setOnClickListener((mealsDeliveryView) -> {

            twoPersonBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealQuantity(TWO);
            onePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            threePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        //if button is clicked set button to red and set other button to default color
        //set meal quantity to button number
        threePersonBtn.setOnClickListener((mealsDeliveryView) -> {

            threePersonBtn.setBackgroundColor(Color.RED);
            userMealDetails.setMealQuantity(THREE);
            twoPersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
            onePersonBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_logo));
        });

        nextBtn = mealsDeliveryView.findViewById(R.id.personalNextButton);

        nextBtn.setOnClickListener((mealsDeliveryView) -> {
            MealTypeFragment mealTypeFrag = new MealTypeFragment();

            //Add new data to sharedViewModel
            signupViewModel.getSelectedItem().observe(getActivity(), users -> {
                users.setUserMealDetails(userMealDetails);
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MealTypeFragment.TAG);
            transaction.replace(R.id.signupHomeFrame, mealTypeFrag);

            transaction.commit();
        });

        return mealsDeliveryView;
    }

    /**
     * This method will set the values of delivery frequency and meal quantity to 1.
     */
    private void setDefaultValues(){
        userMealDetails.setDeliveryFrequency(ONE);
        oneBtn.setBackgroundColor(Color.RED);
        onePersonBtn.setBackgroundColor(Color.RED);
        userMealDetails.setMealQuantity(ONE);

    }
}
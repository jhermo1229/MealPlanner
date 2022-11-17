package com.foodies.mealplanner.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.viewmodel.MealProfileViewModel;

public class MealProfileFragment extends Fragment {

    private MealProfileViewModel mViewModel;
    private View mealProfileFragmentView;
    private EditText mealNameTxt, mealDescriptionTxt, mealIngredientTxt, mealPriceTxt;
    private Spinner mealTypeSpinner;
    private Meal meal = new Meal();
    private String[] mealTypeList;
    private Button okButton, cancelButton;
    private MealRepository db = new MealRepository();

    public static MealProfileFragment newInstance() {
        return new MealProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mealProfileFragmentView = inflater.inflate(R.layout.fragment_meal_profile, container, false);

        //Get fields
        mealNameTxt = mealProfileFragmentView.findViewById(R.id.mealNameEditText);
        mealDescriptionTxt = mealProfileFragmentView.findViewById(R.id.mealDescEditText);
        mealIngredientTxt = mealProfileFragmentView.findViewById(R.id.mealIngrEditText);
        mealTypeSpinner = mealProfileFragmentView.findViewById(R.id.mealType);
        mealPriceTxt = mealProfileFragmentView.findViewById(R.id.mealPriceEditText);
        mealTypeList = getResources().getStringArray(R.array.mealTypeList);
        ArrayAdapter<String> adapterMealType = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, mealTypeList);
        mealTypeSpinner.setAdapter(adapterMealType);
        okButton = mealProfileFragmentView.findViewById(R.id.okButtonMealAdd);
        cancelButton = mealProfileFragmentView.findViewById(R.id.cancelButtonMealAdd);

        okButton.setOnClickListener((mealProfileFragmentView)->{
            meal.setMealName(mealNameTxt.getText().toString());
            meal.setMealDescription(mealDescriptionTxt.getText().toString());
            meal.setMealIngredients(mealIngredientTxt.getText().toString());
            meal.setMealType(mealTypeSpinner.getSelectedItem().toString());
            meal.setMealPrice(Double.valueOf(mealPriceTxt.getText().toString()));
            meal.setMealStatus("Active");

            db.addMeal(meal, getActivity());

        });


        return mealProfileFragmentView;
    }

}
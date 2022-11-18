package com.foodies.mealplanner.fragment;

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
import com.foodies.mealplanner.validations.FieldValidator;

public class MealAddFragment extends Fragment {

    public static final String ACTIVE = "Active";
    private View mealProfileFragmentView;
    private EditText mealNameTxt, mealDescriptionTxt, mealIngredientTxt, mealPriceTxt;
    private Spinner mealTypeSpinner;
    private Meal meal = new Meal();
    private String[] mealTypeList;
    private Button okButton, cancelButton;
    private MealRepository db = new MealRepository();
    private static final String REQUIRED_ERROR = "Required";
    private final FieldValidator fieldValidator = new FieldValidator();

    public static MealAddFragment newInstance() {
        return new MealAddFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mealProfileFragmentView = inflater.inflate(R.layout.fragment_meal_add, container, false);

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

            //check first if all valid
            if(checkAllFields()) {
                meal.setMealName(mealNameTxt.getText().toString());
                meal.setMealDescription(mealDescriptionTxt.getText().toString());
                meal.setMealIngredients(mealIngredientTxt.getText().toString());
                meal.setMealType(mealTypeSpinner.getSelectedItem().toString());
                meal.setMealPrice(Double.valueOf(mealPriceTxt.getText().toString()));
                meal.setMealStatus(ACTIVE);

                db.addMeal(meal, getActivity());
                getParentFragmentManager().popBackStackImmediate();
            }
        });


        return mealProfileFragmentView;
    }

    /**
     * Check all required fields if value is present
     * Check also if inputs are valid
     *
     * @return boolean, true if all valid
     */
    private boolean checkAllFields() {

        boolean allValid = true;
        errorReset();

        if (fieldValidator.validateFieldIfEmpty(mealNameTxt.length())) {
            mealNameTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(mealDescriptionTxt.length())) {
            mealDescriptionTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(mealIngredientTxt.length())) {
            mealIngredientTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(mealPriceTxt.length())) {
            mealPriceTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Reset error messages on field
     */
    private void errorReset() {
        mealNameTxt.setError(null);
        mealDescriptionTxt.setError(null);
        mealIngredientTxt.setError(null);
        mealPriceTxt.setError(null);
    }

}
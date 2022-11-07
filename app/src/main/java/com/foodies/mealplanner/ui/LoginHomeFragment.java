package com.foodies.mealplanner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.validations.FieldValidator;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Main page of Meal Planner
 * Contains the login or signup
 */
public class LoginHomeFragment extends Fragment {

    private final DatabaseHelper db = new DatabaseHelper();
    private Button loginBtn;
    private View homeLoginView;
    private final User user = new User();
    private TextInputLayout email, password;
    private FieldValidator fieldValidator = new FieldValidator();

    public LoginHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeLoginView = inflater.inflate(R.layout.fragment_login_home, container, false);
        loginBtn = homeLoginView.findViewById(R.id.loginUserProfileBtn);
        email = homeLoginView.findViewById(R.id.emailLogin);
        password = homeLoginView.findViewById(R.id.passwordLogin);

        loginBtn.setOnClickListener((personalDetailView) -> {

            //todo: Add validation for email and password then add data to viewmodel

            if(checkAllFields()){

                UserProfileFragment userFrag = new UserProfileFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(PersonalDetailsFragment.TAG);
                transaction.replace(R.id.loginHomeFrame, userFrag);

                transaction.commit();
            }
        });

        return homeLoginView;
    }
    /**
     * Check all required fields if value is present
     *
     * @return boolean, true if all valid
     */
    private boolean checkAllFields() {
        if (fieldValidator.validateFieldIfEmpty(email)) return false;
        return !fieldValidator.validateFieldIfEmpty(password);
    }


}
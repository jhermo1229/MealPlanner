package com.foodies.mealplanner.ui;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.SharedViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Main page of Meal Planner
 * Contains the login or signup
 */
public class LoginHomeFragment extends Fragment {

    private final DatabaseHelper db = new DatabaseHelper();
    private final FieldValidator fieldValidator = new FieldValidator();
    List<User> userList = new ArrayList<>();
    private Button loginBtn;
    private View homeLoginView;
    private CheckBox passwordChk;
    private TextInputLayout email, password;
    private SharedViewModel sharedViewModel;

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
        passwordChk = homeLoginView.findViewById(R.id.passwordLoginCheckbox);

        passwordChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    password.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    password.getEditText().setTransformationMethod(null);
                }
            }
        });

        loginBtn.setOnClickListener((personalDetailView) -> {

            if (checkAllFields()) {
                User userParam = new User();
                userParam.setEmail(Objects.requireNonNull(email.getEditText()).getText().toString());
                userParam.setPassword(Objects.requireNonNull(password.getEditText()).getText().toString());

                db.readData(user -> {
                    userList.add(user);
                    Log.d("RECALL", userList.get(0).getEmail());

                    if (!userList.isEmpty()) {

                        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

                        sharedViewModel.setSelectedItem(userList.get(0));

                        UserProfileFragment userFrag = new UserProfileFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.addToBackStack(PersonalDetailsFragment.TAG);
                        transaction.replace(R.id.loginHomeFrame, userFrag);

                        transaction.commit();
                    } else {
                        email.setError("Email and password does not match");
                        password.setError("Email and password does not match");
                    }
                }, userParam);


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



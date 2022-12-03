package com.foodies.mealplanner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.activity.AdminActivity;
import com.foodies.mealplanner.activity.SignupActivity;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.util.AppUtils;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;
import com.foodies.mealplanner.viewmodel.SignupViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Main page of Meal Planner
 * Contains the login or signup
 *
 * @author herje
 */
public class LoginHomeFragment extends Fragment {

    public static final String REQUIRED = "Required";
    public static final String PLEASE_CHECK_PASSWORD = "Please check password";
    public static final String EMAIL_DOES_NOT_EXIST = "Email does not exist";
    public static final String INCORRECT_EMAIL_FORMAT = "Incorrect Email Format";
    public static String TAG = LoginHomeFragment.class.getName();
    private final UserRepository db = new UserRepository();
    private final FieldValidator fieldValidator = new FieldValidator();
    private final AppUtils appUtils = new AppUtils();
    private Button loginBtn;
    private View homeLoginView;
    private CheckBox passwordChk;
    private TextInputLayout email, password;
    private SignupViewModel signupViewModel;
    private AdminProfileViewModel adminViewModel;

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

        //Show password if checked
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

                db.getUser(user -> {


                    if (user != null) {
                        String passwordDecode = appUtils.decodeBase64(user.getPassword());
                        if (checkEmailAndPasswordMatch(passwordDecode, userParam.getPassword())) {

                            if (user.getUserType().equals("C")) {
                                signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

                                signupViewModel.setSelectedItem(user);

                                CustomerProfileFragment userFrag = new CustomerProfileFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.addToBackStack(PersonalDetailsFragment.TAG);
                                transaction.replace(R.id.loginHomeFrame, userFrag);

                                transaction.commit();
                            } else if (user.getUserType().equals("A")) {
//                                adminViewModel = new ViewModelProvider(requireActivity()).get(AdminProfileViewModel.class);
//                                adminViewModel.setSelectedItem(user);

                                Intent intent = new Intent(getActivity(), AdminActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);


                            }
                        } else {
                            password.setError(PLEASE_CHECK_PASSWORD);
                        }
                    } else {
                        email.setError(EMAIL_DOES_NOT_EXIST);
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

        boolean allValid = true;
        errorReset();

        if (fieldValidator.validateFieldIfEmpty(email.getEditText().length())) {
            email.setError(REQUIRED);
            allValid = false;
        }
        if (fieldValidator.validateFieldIfEmpty(password.getEditText().length())) {
            password.setError(REQUIRED);
            allValid = false;
        }

        if (!fieldValidator.isValidEmail(email.getEditText().getText().toString())) {
            email.setError(INCORRECT_EMAIL_FORMAT);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Reset error message
     */
    private void errorReset() {
        email.setError(null);
        password.setError(null);
    }

    /**
     * Check if email and password match
     *
     * @return boolean, true if valid
     */
    private boolean checkEmailAndPasswordMatch(String password, String paramPassword) {

        return password.equals(paramPassword);
    }
}



package com.foodies.mealplanner.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Address;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserDetails;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.util.AppUtils;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.SignupViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Signing up fragment for adding customer personal details.
 * @author herje
 * @version 1
 */
public class PersonalDetailsFragment extends Fragment {

    public static final String EMAIL_ALREADY_EXISTING = "Email already existing";
    public static final String INVALID_LENGTH = "Invalid length";
    public static final String INCORRECT_EMAIL_FORMAT = "Incorrect Email Format";
    public static final int TEN = 10;
    public static final String REQUIRED_ERROR = "Required";
    public static String TAG = PersonalDetailsFragment.class.getName();
    private final UserDetails userDetails = new UserDetails();
    private final User user = new User();
    private final Address address = new Address();
    private final FieldValidator fieldValidator = new FieldValidator();
    private final UserRepository db = new UserRepository();
    private EditText  firstName, lastName, houseNumber, street, city, postalCode,
            phoneNumber, email, password;
    private CheckBox passwordChk;
    private View personalDetailsView;
    private String[] provinceList;
    private Button nextBtn;
    private SignupViewModel signupViewModel;
    private final AppUtils appUtils = new AppUtils();

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        personalDetailsView = inflater.inflate(R.layout.fragment_personal_details, container, false);
        //Button for transferring to next fragment
        nextBtn = personalDetailsView.findViewById(R.id.personalNextButton);
        //Province Spinner
        Spinner provinceSpinner = personalDetailsView.findViewById(R.id.province_spinner);
        provinceList = getResources().getStringArray(R.array.province_canada);

        //Set adapter of spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, provinceList);
        provinceSpinner.setAdapter(adapter);

        //Get the value from XML counterpart
        firstName = personalDetailsView.findViewById(R.id.firstName);
        lastName = personalDetailsView.findViewById(R.id.lastName);
        houseNumber = personalDetailsView.findViewById(R.id.houseNumber);
        street = personalDetailsView.findViewById(R.id.street);
        city = personalDetailsView.findViewById(R.id.city);
        postalCode = personalDetailsView.findViewById(R.id.postalCode);
        phoneNumber = personalDetailsView.findViewById(R.id.phoneNumber);
        phoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(TEN)});
        email = personalDetailsView.findViewById(R.id.email);
        password = personalDetailsView.findViewById(R.id.password);
        passwordChk = personalDetailsView.findViewById(R.id.passwordCheckbox);

        //Show password or not show
        passwordChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(null);
                }
            }
        });

        nextBtn.setOnClickListener((personalDetailView) -> {
            //check first all required fields

            if (checkAllFields()) {
                //Set sharemodel to share User data to different fragments
                signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

                userDetails.setFirstName(firstName.getText().toString());
                userDetails.setLastName(lastName.getText().toString());
                userDetails.setPhoneNumber(phoneNumber.getText().toString());
                address.setHouseNumber(houseNumber.getText().toString());
                address.setStreet(street.getText().toString());
                address.setCity(city.getText().toString());
                String province = provinceSpinner.getSelectedItem().toString();
                address.setProvince(province);
                address.setPostalCode(postalCode.getText().toString());
                userDetails.setAddress(address);
                user.setUserDetails(userDetails);
                user.setEmail(email.getText().toString());

                //Encrypt password
                String passwordCodeEncrypt = appUtils.encodeBase64(password.getText().toString());
                user.setPassword(passwordCodeEncrypt);;

                //Check if email is already existing in database. Since firebase is asynchronous,
                //it will check only the email if database process is complete.
                List<User> userList = new ArrayList<>();
                db.getUser(userCheck -> {
                    if (userCheck != null) {
                        userList.add(userCheck);
                    }

                    if (userList.isEmpty()) {
                        //Add object to view model
                        signupViewModel.setSelectedItem(user);
                        PaymentDetailsFragment paymentDetailsFragment = new PaymentDetailsFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.personalDetailsFrag, paymentDetailsFragment);
                        transaction.commit();
                    } else {
                        email.setError(EMAIL_ALREADY_EXISTING);
                    }
                }, user);
            }
        });

        return personalDetailsView;
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

        if (fieldValidator.validateFieldIfEmpty(firstName.length())) {
            firstName.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(lastName.length())) {
            lastName.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(houseNumber.length())) {
            houseNumber.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(street.length())) {
            street.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(city.length())) {
            city.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(postalCode.length())) {
            postalCode.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(phoneNumber.length())) {
            phoneNumber.setError(REQUIRED_ERROR);
            allValid = false;
        }

        //Check first if it has a value then check the length. So that error message wont overlap
        if (allValid && fieldValidator.validateIfInputIsLess(TEN, phoneNumber.length())) {
            phoneNumber.setError(INVALID_LENGTH);
            allValid = false;
        }


        if (fieldValidator.validateFieldIfEmpty(email.length())) {
            email.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(password.length())) {
            password.setError(REQUIRED_ERROR);
            allValid = false;
        }

        //Check first if it has a value then check the format. So that error message wont overlap
        if (allValid && !fieldValidator.isValidEmail(email.getText().toString())) {
            email.setError(INCORRECT_EMAIL_FORMAT);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Reset error messages on field
     */
    private void errorReset() {
        firstName.setError(null);
        lastName.setError(null);
        houseNumber.setError(null);
        street.setError(null);
        city.setError(null);
        postalCode.setError(null);
        phoneNumber.setError(null);
        email.setError(null);
        password.setError(null);
    }
}
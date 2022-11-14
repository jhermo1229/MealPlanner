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
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Address;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserDetails;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.SharedViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment class for personal details
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
    private final DatabaseHelper db = new DatabaseHelper();
    private TextInputLayout firstName, lastName, houseNumber, street, city, postalCode,
            phoneNumber, email, password;
    private CheckBox passwordChk;
    private View personalDetailsView;
    private String[] provinceList;
    private Button nextBtn;
    private SharedViewModel sharedViewModel;
    private boolean isFieldChecked = false;

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
        phoneNumber.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(TEN)});
        email = personalDetailsView.findViewById(R.id.email);
        password = personalDetailsView.findViewById(R.id.password);
        passwordChk = personalDetailsView.findViewById(R.id.passwordCheckbox);

        //Show password or not show
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

        nextBtn.setOnClickListener((personalDetailView) -> {
            //check first all required fields
            isFieldChecked = checkAllFields();

            if (isFieldChecked) {
                //Set sharemodel to share User data to different fragments
                sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

                userDetails.setFirstName(firstName.getEditText().getText().toString());
                userDetails.setLastName(lastName.getEditText().getText().toString());
                userDetails.setPhoneNumber(phoneNumber.getEditText().getText().toString());
                address.setHouseNumber(houseNumber.getEditText().getText().toString());
                address.setStreet(street.getEditText().getText().toString());
                address.setCity(city.getEditText().getText().toString());
                String province = provinceSpinner.getSelectedItem().toString();
                address.setProvince(province);
                address.setPostalCode(postalCode.getEditText().getText().toString());
                userDetails.setAddress(address);
                user.setUserDetails(userDetails);
                user.setEmail(email.getEditText().getText().toString());
                user.setPassword(password.getEditText().getText().toString());

                //Check if email is already existing in database. Since firebase is asynchronous,
                //it will check only the email if database process is complete.
                List<User> userList = new ArrayList<>();
                db.readData(userCheck -> {
                    if (userCheck != null) {
                        userList.add(userCheck);
                    }

                    if (userList.isEmpty()) {
                        //Add object to view model
                        sharedViewModel.setSelectedItem(user);
                        MealsDeliveryRateFragment mealsDeliveryFrag = new MealsDeliveryRateFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.addToBackStack(MealsDeliveryRateFragment.TAG);
                        transaction.replace(R.id.signupHomeFrame, mealsDeliveryFrag);
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

        if (fieldValidator.validateFieldIfEmpty(firstName.getEditText().length())) {
            firstName.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(lastName.getEditText().length())) {
            lastName.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(houseNumber.getEditText().length())) {
            houseNumber.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(street.getEditText().length())) {
            street.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(city.getEditText().length())) {
            city.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(postalCode.getEditText().length())) {
            postalCode.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(phoneNumber.getEditText().length())) {
            phoneNumber.setError(REQUIRED_ERROR);
            allValid = false;
        }

        //Check first if it has a value then check the length. So that error message wont overlap
        if (allValid && fieldValidator.validateIfInputIsLess(TEN, phoneNumber.getEditText().length())) {
            phoneNumber.setError(INVALID_LENGTH);
            allValid = false;
        }


        if (fieldValidator.validateFieldIfEmpty(email.getEditText().length())) {
            email.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(password.getEditText().length())) {
            password.setError(REQUIRED_ERROR);
            allValid = false;
        }

        //Check first if it has a value then check the format. So that error message wont overlap
        if (allValid && !fieldValidator.isValidEmail(email.getEditText().getText().toString())) {
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
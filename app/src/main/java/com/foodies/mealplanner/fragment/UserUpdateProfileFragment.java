package com.foodies.mealplanner.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Address;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserDetails;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.UserUpdateViewModel;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Fragment for updating users profile by the admin
 */
public class UserUpdateProfileFragment extends Fragment {

    private static final String REQUIRED_ERROR = "Required";
    private static final String INVALID_LENGTH = "Invalid length";
    private static final int TEN = 10;
    private final UserRepository db = new UserRepository();
    private final User user = new User();
    private final FieldValidator fieldValidator = new FieldValidator();
    private UserUpdateViewModel userUpdateViewModel;
    private View userProfileUpdateView;
    private TextInputLayout firstName, lastName, houseNumber, street, city, postalCode,
            phoneNumber;
    private String[] provinceList;
    private String[] statusList;
    private Button okButton, cancelButton;
    private Spinner provinceSpinner, statusSpinner;
    private boolean isFieldChanged = false;

    public UserUpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userProfileUpdateView = inflater.inflate(R.layout.fragment_user_update_profile, container, false);
        userUpdateViewModel = new ViewModelProvider(requireActivity()).get(UserUpdateViewModel.class);

        //Get fields
        firstName = userProfileUpdateView.findViewById(R.id.firstNameAdminEditProf);
        lastName = userProfileUpdateView.findViewById(R.id.lastNameAdminEditProf);
        houseNumber = userProfileUpdateView.findViewById(R.id.houseNumberAdminEditProf);
        street = userProfileUpdateView.findViewById(R.id.streetAdminEditProf);
        city = userProfileUpdateView.findViewById(R.id.cityAdminEditProf);
        postalCode = userProfileUpdateView.findViewById(R.id.postalCodeAdminEditProf);
        phoneNumber = userProfileUpdateView.findViewById(R.id.phoneNumberAdminEditProf);
        phoneNumber.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        provinceSpinner = userProfileUpdateView.findViewById(R.id.provinceSpinAdminEditProf);
        statusSpinner = userProfileUpdateView.findViewById(R.id.statusAdminEditProf);
        cancelButton = userProfileUpdateView.findViewById(R.id.cancelButton);
        okButton = userProfileUpdateView.findViewById(R.id.okButton);

        //get user set in view model
        User liveUser = userUpdateViewModel.getSelectedItem().getValue();

        //Add user to the fields
        firstName.getEditText().setText(liveUser.getUserDetails().getFirstName());
        lastName.getEditText().setText(liveUser.getUserDetails().getLastName());
        houseNumber.getEditText().setText(liveUser.getUserDetails().getAddress().getHouseNumber());
        street.getEditText().setText(liveUser.getUserDetails().getAddress().getStreet());
        city.getEditText().setText(liveUser.getUserDetails().getAddress().getCity());
        postalCode.getEditText().setText(liveUser.getUserDetails().getAddress().getPostalCode());
        phoneNumber.getEditText().setText(liveUser.getUserDetails().getPhoneNumber());

        //set spinner items
        provinceList = getResources().getStringArray(R.array.province_canada);
        statusList = getResources().getStringArray(R.array.status);

        //Set value of province spinner
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, provinceList);
        provinceSpinner.setAdapter(adapterProvince);
        int spinnerPos = adapterProvince.getPosition(liveUser.getUserDetails().getAddress().getProvince());

        //Set value of status spinner
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, statusList);
        statusSpinner.setAdapter(adapterStatus);
        int spinnerStatusPos = adapterStatus.getPosition(liveUser.getStatus());
        provinceSpinner.setSelection(spinnerPos);
        statusSpinner.setSelection(spinnerStatusPos);

        //Set listeners to the field if a user did a change
        firstName.getEditText().addTextChangedListener(textWatcher());
        lastName.getEditText().addTextChangedListener(textWatcher());
        houseNumber.getEditText().addTextChangedListener(textWatcher());
        street.getEditText().addTextChangedListener(textWatcher());
        city.getEditText().addTextChangedListener(textWatcher());
        postalCode.getEditText().addTextChangedListener(textWatcher());
        phoneNumber.getEditText().addTextChangedListener(textWatcher());
        provinceSpinner.setOnItemSelectedListener(spinnerWatcher(spinnerPos));
        statusSpinner.setOnItemSelectedListener(spinnerWatcher(spinnerStatusPos));

        //if cancel button is clicked, go back to previous fragment
        cancelButton.setOnClickListener((userProfileUpdateView) -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        //If ok button, update selected user
        okButton.setOnClickListener((userProfileUpdateView) -> {

                    //Check first if any field has changed
                    if (!isFieldChanged) {
                        getParentFragmentManager().popBackStackImmediate();
                        //check field validation
                    } else if (checkAllFields()) {

                        //set User details
                        UserDetails userDetails = new UserDetails();
                        user.setEmail(userUpdateViewModel.getSelectedItem().getValue().getEmail());
                        user.setPassword(userUpdateViewModel.getSelectedItem().getValue().getPassword());
                        user.setUserType(userUpdateViewModel.getSelectedItem().getValue().getUserType());
                        user.setUserPaymentDetails(userUpdateViewModel.getSelectedItem().getValue().getUserPaymentDetails());
                        userDetails.setFirstName(firstName.getEditText().getText().toString());
                        userDetails.setLastName(lastName.getEditText().getText().toString());
                        userDetails.setPhoneNumber(phoneNumber.getEditText().getText().toString());

                        //Set address
                        Address address = new Address();
                        address.setHouseNumber(houseNumber.getEditText().getText().toString());
                        address.setStreet(street.getEditText().getText().toString());
                        address.setCity(city.getEditText().getText().toString());
                        address.setProvince(provinceSpinner.getSelectedItem().toString());
                        address.setPostalCode(postalCode.getEditText().getText().toString());

                        userDetails.setAddress(address);

                        user.setUserDetails(userDetails);
                        user.setStatus(statusSpinner.getSelectedItem().toString());

                        //Update user
                        db.updateUser(user, getActivity());
                        getParentFragmentManager().popBackStackImmediate();
                    }
        });

        return userProfileUpdateView;
    }

    /**
     * Check if a change is done on a spinner.
     *
     * @return
     */
    @NonNull
    private AdapterView.OnItemSelectedListener spinnerWatcher(int a) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (a != i) {
                    isFieldChanged = true;
                    Log.d("SPINNER LISTENER", ":::::" + a);
                    Log.d("SPINNER LISTENER", ":::::" + i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    /**
     * Check if a change is done on the text fields.
     */
    @NonNull
    private TextWatcher textWatcher() {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isFieldChanged = true;
                Log.d("TEXT LISTENER", ":::::" + isFieldChanged);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
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

        return allValid;
    }

    /**
     * Reset error messages on field
     */
    private void errorReset() {
        houseNumber.setError(null);
        street.setError(null);
        city.setError(null);
        postalCode.setError(null);
        phoneNumber.setError(null);
    }
}
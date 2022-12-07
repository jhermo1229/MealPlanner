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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Address;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserDetails;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.AdminUserViewModel;

/**
 * Fragment for updating users profile by the admin
 * @author herje
 * @version 1
 */
public class UserViewUpdateFragment extends Fragment {

    private static final String REQUIRED_ERROR = "Required";
    private static final String INVALID_LENGTH = "Invalid length";
    private static final int TEN = 10;
    private final UserRepository db = new UserRepository();
    private final User user = new User();
    private final FieldValidator fieldValidator = new FieldValidator();
    private AdminUserViewModel adminUserViewModel;
    private View userProfileUpdateView;
    private EditText firstName, lastName, houseNumber, street, city, postalCode,
            phoneNumber;
    private String[] provinceList;
    private String[] statusList;
    private Button okButton, cancelButton, updateButton;
    private Spinner provinceSpinner, statusSpinner;
    private boolean isFieldChanged = false;

    public UserViewUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userProfileUpdateView = inflater.inflate(R.layout.fragment_user_view_update, container, false);
        adminUserViewModel = new ViewModelProvider(requireActivity()).get(AdminUserViewModel.class);

        //Get fields
        firstName = userProfileUpdateView.findViewById(R.id.firstNameAdminUpdate);
        lastName = userProfileUpdateView.findViewById(R.id.lastNameAdminUpdate);
        houseNumber = userProfileUpdateView.findViewById(R.id.houseNUmberAdminUpdate);
        street = userProfileUpdateView.findViewById(R.id.streetAdminUpdate);
        city = userProfileUpdateView.findViewById(R.id.cityAdminUpdate);
        postalCode = userProfileUpdateView.findViewById(R.id.postalCodeAdminUpdate);
        phoneNumber = userProfileUpdateView.findViewById(R.id.phoneNumberAdminUpdate);
        phoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        provinceSpinner = userProfileUpdateView.findViewById(R.id.provinceSpinAdminEditProf);
        statusSpinner = userProfileUpdateView.findViewById(R.id.statusAdminEditProf);
        cancelButton = userProfileUpdateView.findViewById(R.id.cancelButtonUserUpdate);
        okButton = userProfileUpdateView.findViewById(R.id.okButtonUserUpdate);
        updateButton = userProfileUpdateView.findViewById(R.id.updateUserButton);

        //get user set in view model
        User liveUser = adminUserViewModel.getSelectedItem().getValue();

        //Add user to the fields
        firstName.setText(liveUser.getUserDetails().getFirstName());
        lastName.setText(liveUser.getUserDetails().getLastName());
        houseNumber.setText(liveUser.getUserDetails().getAddress().getHouseNumber());
        street.setText(liveUser.getUserDetails().getAddress().getStreet());
        city.setText(liveUser.getUserDetails().getAddress().getCity());
        postalCode.setText(liveUser.getUserDetails().getAddress().getPostalCode());
        phoneNumber.setText(liveUser.getUserDetails().getPhoneNumber());

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
        firstName.addTextChangedListener(textWatcher());
        lastName.addTextChangedListener(textWatcher());
        houseNumber.addTextChangedListener(textWatcher());
        street.addTextChangedListener(textWatcher());
        city.addTextChangedListener(textWatcher());
        postalCode.addTextChangedListener(textWatcher());
        phoneNumber.addTextChangedListener(textWatcher());
        provinceSpinner.setOnItemSelectedListener(spinnerWatcher(spinnerPos));
        statusSpinner.setOnItemSelectedListener(spinnerWatcher(spinnerStatusPos));

        setFieldsDisabled();

        updateButton.setOnClickListener((userProfileUpdateView) -> {
            setFieldsEnabled();
            updateButton.setVisibility(View.INVISIBLE);
            okButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        });

        //if cancel button is clicked, go back to previous fragment
        cancelButton.setOnClickListener((userProfileUpdateView) -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        //If ok button, update selected user
        okButton.setOnClickListener((userProfileUpdateView) -> {

                    //Check first if any field has changed
                    if (!isFieldChanged) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No field was updated", Toast.LENGTH_SHORT);
                        toast.show();
                        //check field validation
                    } else if (checkAllFields()) {

                        //set User details
                        UserDetails userDetails = new UserDetails();
                        user.setEmail(adminUserViewModel.getSelectedItem().getValue().getEmail());
                        user.setPassword(adminUserViewModel.getSelectedItem().getValue().getPassword());
                        user.setUserType(adminUserViewModel.getSelectedItem().getValue().getUserType());
                        user.setUserPaymentDetails(adminUserViewModel.getSelectedItem().getValue().getUserPaymentDetails());
                        userDetails.setFirstName(firstName.getText().toString());
                        userDetails.setLastName(lastName.getText().toString());
                        userDetails.setPhoneNumber(phoneNumber.getText().toString());

                        //Set address
                        Address address = new Address();
                        address.setHouseNumber(houseNumber.getText().toString());
                        address.setStreet(street.getText().toString());
                        address.setCity(city.getText().toString());
                        address.setProvince(provinceSpinner.getSelectedItem().toString());
                        address.setPostalCode(postalCode.getText().toString());

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
    private AdapterView.OnItemSelectedListener spinnerWatcher(int spinnerPosition) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerPosition != i) {
                    isFieldChanged = true;
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

    /**
     * Disable field on load
     */
    private void setFieldsDisabled() {

        firstName.setEnabled(false);
        lastName.setEnabled(false);
        houseNumber.setEnabled(false);
        street.setEnabled(false);
        city.setEnabled(false);
        provinceSpinner.setEnabled(false);
        postalCode.setEnabled(false);
        phoneNumber.setEnabled(false);
        statusSpinner.setEnabled(false);
    }

    /**
     * Disable field on load
     */
    private void setFieldsEnabled() {

        firstName.setEnabled(true);
        lastName.setEnabled(true);
        houseNumber.setEnabled(true);
        street.setEnabled(true);
        city.setEnabled(true);
        provinceSpinner.setEnabled(true);
        postalCode.setEnabled(true);
        phoneNumber.setEnabled(true);
        statusSpinner.setEnabled(true);
    }
}
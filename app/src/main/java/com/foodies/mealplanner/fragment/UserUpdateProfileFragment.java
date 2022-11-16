package com.foodies.mealplanner.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Address;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserDetails;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserUpdateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserUpdateProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final DatabaseHelper db = new DatabaseHelper();
    private final User user = new User();
    private AdminProfileViewModel adminProfileViewModel;
    private View userProfileUpdateView;
    private TextInputLayout firstName, lastName, houseNumber, street, city, postalCode,
            phoneNumber, email, password;
    private String[] provinceList;
    private String[] statusList;
    private Button okButton, cancelButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserUpdateProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserUpdateProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserUpdateProfileFragment newInstance(String param1, String param2) {
        UserUpdateProfileFragment fragment = new UserUpdateProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userProfileUpdateView = inflater.inflate(R.layout.fragment_user_update_profile, container, false);
        adminProfileViewModel = new ViewModelProvider(requireActivity()).get(AdminProfileViewModel.class);

        firstName = userProfileUpdateView.findViewById(R.id.firstNameAdminEditProf);
        lastName = userProfileUpdateView.findViewById(R.id.lastNameAdminEditProf);
        houseNumber = userProfileUpdateView.findViewById(R.id.houseNumberAdminEditProf);
        street = userProfileUpdateView.findViewById(R.id.streetAdminEditProf);
        city = userProfileUpdateView.findViewById(R.id.cityAdminEditProf);
        postalCode = userProfileUpdateView.findViewById(R.id.postalCodeAdminEditProf);
        phoneNumber = userProfileUpdateView.findViewById(R.id.phoneNumberAdminEditProf);
        phoneNumber.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        Spinner provinceSpinner = userProfileUpdateView.findViewById(R.id.provinceSpinAdminEditProf);
        Spinner statusSpinner = userProfileUpdateView.findViewById(R.id.statusAdminEditProf);
        cancelButton = userProfileUpdateView.findViewById(R.id.cancelButton);
        okButton = userProfileUpdateView.findViewById(R.id.okButton);

        User liveUser = adminProfileViewModel.getSelectedItem().getValue();
        firstName.getEditText().setText(liveUser.getUserDetails().getFirstName());
        lastName.getEditText().setText(liveUser.getUserDetails().getLastName());
        houseNumber.getEditText().setText(liveUser.getUserDetails().getAddress().getHouseNumber());
        street.getEditText().setText(liveUser.getUserDetails().getAddress().getStreet());
        city.getEditText().setText(liveUser.getUserDetails().getAddress().getCity());
        postalCode.getEditText().setText(liveUser.getUserDetails().getAddress().getPostalCode());
        phoneNumber.getEditText().setText(liveUser.getUserDetails().getPhoneNumber());
        provinceList = getResources().getStringArray(R.array.province_canada);
        statusList = getResources().getStringArray(R.array.status);

        //Set adapter of spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, provinceList);
        provinceSpinner.setAdapter(adapter);
       int spinnerPos = adapter.getPosition(liveUser.getUserDetails().getAddress().getProvince());
       provinceSpinner.setSelection(spinnerPos);

        //Set adapter of spinner
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, statusList);
        statusSpinner.setAdapter(adapterStatus);
        int spinnerStatusPos = adapter.getPosition(liveUser.getStatus());
        statusSpinner.setSelection(spinnerStatusPos);

        cancelButton.setOnClickListener((userProfileUpdateView) -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        okButton.setOnClickListener((userProfileUpdateView) -> {
            UserDetails ud = new UserDetails();
            user.setEmail(adminProfileViewModel.getSelectedItem().getValue().getEmail());
            user.setPassword(adminProfileViewModel.getSelectedItem().getValue().getPassword());
            user.setUserType(adminProfileViewModel.getSelectedItem().getValue().getUserType());
            user.setUserPaymentDetails(adminProfileViewModel.getSelectedItem().getValue().getUserPaymentDetails());
            ud.setFirstName(firstName.getEditText().getText().toString());
            ud.setLastName(lastName.getEditText().getText().toString());
            ud.setPhoneNumber(phoneNumber.getEditText().getText().toString());
            Address address = new Address();

            address.setHouseNumber(houseNumber.getEditText().getText().toString());
            address.setStreet(street.getEditText().getText().toString());
            address.setCity(city.getEditText().getText().toString());
            address.setProvince(provinceSpinner.getSelectedItem().toString());
            address.setPostalCode(postalCode.getEditText().getText().toString());

            ud.setAddress(address);

            user.setUserDetails(ud);
            user.setStatus(statusSpinner.getSelectedItem().toString());

            db.updateUser(user, getActivity());

            getParentFragmentManager().popBackStackImmediate();
        });

        return userProfileUpdateView;
    }
}
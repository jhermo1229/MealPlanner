package com.foodies.mealplanner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.viewmodel.SharedViewModel;
import com.foodies.mealplanner.model.Address;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserDetails;
import com.foodies.mealplanner.util.AppUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

/**
 * Fragment class for personal details
 */
public class PersonalDetailsFragment extends Fragment {

    public static String TAG = PersonalDetailsFragment.class.getName();
    private final AppUtils appUtils = new AppUtils();
    private final UserDetails userDetails = new UserDetails();
    private final User user = new User();
    private final Address address = new Address();
    private TextInputLayout firstName, lastName, houseNumber, street, city, postalCode,
            phoneNumber, email, password;
    private View personalDetailsView;
    private String[] provinceList;
    private Button nextBtn;
    private SharedViewModel sharedViewModel;
    private boolean isFieldChecked = false;


    public PersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        personalDetailsView = inflater.inflate(R.layout.fragment_personal_details, container, false);
        //Province Spinner
        Spinner spinner = personalDetailsView.findViewById(R.id.province_spinner);
        provinceList = getResources().getStringArray(R.array.province_canada);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, provinceList);
        spinner.setAdapter(adapter);

        //Button for transferring to next fragment
        nextBtn = personalDetailsView.findViewById(R.id.nextButton);

        nextBtn.setOnClickListener((personalDetailView) -> {

            //Set sharemodel to share User data to different fragments
            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

            //Get the value from XML counterpart
            firstName = personalDetailsView.findViewById(R.id.firstName);
            lastName = personalDetailsView.findViewById(R.id.lastName);
            houseNumber = personalDetailsView.findViewById(R.id.houseNumber);
            street = personalDetailsView.findViewById(R.id.street);
            city = personalDetailsView.findViewById(R.id.city);

            String province = spinner.getSelectedItem().toString();
            postalCode = personalDetailsView.findViewById(R.id.postalCode);
            phoneNumber = personalDetailsView.findViewById(R.id.phoneNumber);
            email = personalDetailsView.findViewById(R.id.email);
            password = personalDetailsView.findViewById(R.id.password);

            isFieldChecked = checkAllFields();


if(isFieldChecked) {
    userDetails.setFirstName(firstName.getEditText().getText().toString());
    userDetails.setLastName(lastName.getEditText().getText().toString());
    userDetails.setPhoneNumber(phoneNumber.getEditText().getText().toString());

    address.setHouseNumber(houseNumber.getEditText().getText().toString());
    address.setStreet(street.getEditText().getText().toString());
    address.setCity(city.getEditText().getText().toString());
    address.setProvince(province);
    address.setPostalCode(postalCode.getEditText().getText().toString());

    userDetails.setAddress(address);

    user.setUserDetails(userDetails);
    user.setEmail(email.getEditText().getText().toString());

    //Encrypt password first before saving
    String passwordEncode = appUtils.encodeBase64(password.getEditText().getText().toString());
    user.setPassword(passwordEncode);

    sharedViewModel.setSelectedItem(user);

    MealsDeliveryRateFragment mealsDeliveryFrag = new MealsDeliveryRateFragment();
    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
    transaction.addToBackStack(MealsDeliveryRateFragment.TAG);
    transaction.replace(R.id.signupHomeFrame, mealsDeliveryFrag);

    transaction.commit();
}
        });

        return personalDetailsView;
    }

    private boolean checkAllFields(){

        if(email.getEditText().length() == 0){
            email.setError("This field is required");
            return false;
        }
        return true;
    }

    /**
     * Checker for email format
     * @param email
     * @return true if matches
     */
    private boolean isValidEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}
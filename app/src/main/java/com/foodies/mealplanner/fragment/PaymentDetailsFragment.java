package com.foodies.mealplanner.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserPaymentDetails;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.util.AppUtils;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.SignupViewModel;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Fragment class for payment details of user
 */
public class PaymentDetailsFragment extends Fragment {

    public static final String TAG = PaymentDetailsFragment.class.getName();
    public static final String REQUIRED = "Required";
    public static final String INVALID_LENGTH = "Invalid length";
    public static final String CUSTOMER = "C";
    public static final String ACTIVE = "A";
    private final DatabaseHelper db = new DatabaseHelper();
    private final AppUtils appUtils = new AppUtils();
    private final UserPaymentDetails userPaymentDetails = new UserPaymentDetails();
    private final User user = new User();
    private final FieldValidator fieldValidator = new FieldValidator();
    private SignupViewModel signupViewModel;
    private View paymentDetailsView;
    private TextInputLayout nameOnCard, cardNumber, expiryDate, securityCode;
    private Button saveButton;
    private boolean isFieldChecked = false;


    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        paymentDetailsView = inflater.inflate(R.layout.fragment_payment, container, false);
        nameOnCard = paymentDetailsView.findViewById(R.id.nameOnCard);
        cardNumber = paymentDetailsView.findViewById(R.id.cardNumber);
        cardNumber.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        expiryDate = paymentDetailsView.findViewById(R.id.expiryDate);
        expiryDate.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        securityCode = paymentDetailsView.findViewById(R.id.securityCode);
        securityCode.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        saveButton = paymentDetailsView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener((personalDetailView) -> {

            //check first all required fields
            isFieldChecked = checkAllFields();

            if (isFieldChecked) {
                userPaymentDetails.setNameOnCard(nameOnCard.getEditText().getText().toString());
                userPaymentDetails.setCardNumber(Double.valueOf(cardNumber.getEditText().getText().toString()));
                userPaymentDetails.setExpiryDate(Integer.valueOf(expiryDate.getEditText().getText().toString()));
                String securityCodeEncrypt = appUtils.encodeBase64(securityCode.getEditText().getText().toString());
                userPaymentDetails.setSecurityCode(securityCodeEncrypt);

                //Get all values from view model and add payment details
                signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
                user.setEmail(signupViewModel.getSelectedItem().getValue().getEmail());
                user.setPassword(signupViewModel.getSelectedItem().getValue().getPassword());
                user.setUserDetails(signupViewModel.getSelectedItem().getValue().getUserDetails());
                user.setUserMealDetails(signupViewModel.getSelectedItem().getValue().getUserMealDetails());
                user.setUserPaymentDetails(userPaymentDetails);
                user.setUserType(CUSTOMER);
                user.setStatus(ACTIVE);

                //Save all details to database
                db.addCustomerUser(user, getActivity());
            }
        });

        return paymentDetailsView;
    }

    /**
     * Check all required fields if value is present
     *
     * @return boolean, true if all valid
     */
    private boolean checkAllFields() {

        boolean isAllValid = true;
        errorReset();

        if (fieldValidator.validateFieldIfEmpty(nameOnCard.getEditText().length())) {
            nameOnCard.setError(REQUIRED);
            isAllValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(cardNumber.getEditText().length())) {
            cardNumber.setError(REQUIRED);
            isAllValid = false;
        }


        if (isAllValid && fieldValidator.validateIfInputIsLess(16, cardNumber.getEditText().length())) {
            cardNumber.setError(INVALID_LENGTH);
            isAllValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(expiryDate.getEditText().length())) {
            expiryDate.setError(REQUIRED);
            isAllValid = false;
        }


        if (isAllValid && fieldValidator.validateIfInputIsLess(4, expiryDate.getEditText().length())) {
            expiryDate.setError(INVALID_LENGTH);
            isAllValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(securityCode.getEditText().length())) {
            securityCode.setError(REQUIRED);
            isAllValid = false;
        }

        if (isAllValid && fieldValidator.validateIfInputIsLess(3, securityCode.getEditText().length())) {
            securityCode.setError(INVALID_LENGTH);
            isAllValid = false;
        }

        return isAllValid;
    }

    /**
     * Reset error messages
     */
    private void errorReset() {
        nameOnCard.setError(null);
        cardNumber.setError(null);
        expiryDate.setError(null);
        securityCode.setError(null);
    }
}

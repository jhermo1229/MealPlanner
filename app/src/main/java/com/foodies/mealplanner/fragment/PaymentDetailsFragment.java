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
import com.foodies.mealplanner.viewmodel.SharedViewModel;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Fragment class for payment details of user
 */
public class PaymentDetailsFragment extends Fragment {

    public static final String TAG = PaymentDetailsFragment.class.getName();
    private final DatabaseHelper db = new DatabaseHelper();
    private final AppUtils appUtils = new AppUtils();
    private final UserPaymentDetails userPaymentDetails = new UserPaymentDetails();
    private final User user = new User();
    private SharedViewModel sharedViewModel;
    private View paymentDetailsView;
    private TextInputLayout nameOnCard, cardNumber, expiryDate, securityCode;
    private Button saveButton;
    private final FieldValidator fieldValidator = new FieldValidator();
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
        expiryDate = paymentDetailsView.findViewById(R.id.expiryDate);
        expiryDate.getEditText().setFilters(new InputFilter[]{ new InputFilter.LengthFilter(3) });
        securityCode = paymentDetailsView.findViewById(R.id.securityCode);
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

                sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                user.setEmail(sharedViewModel.getSelectedItem().getValue().getEmail());
                user.setPassword(sharedViewModel.getSelectedItem().getValue().getPassword());
                user.setUserDetails(sharedViewModel.getSelectedItem().getValue().getUserDetails());
                user.setUserMealDetails(sharedViewModel.getSelectedItem().getValue().getUserMealDetails());
                user.setUserPaymentDetails(userPaymentDetails);

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

        if (fieldValidator.validateFieldIfEmpty(nameOnCard)) return false;
        if (fieldValidator.validateFieldIfEmpty(cardNumber)) return false;
        if (fieldValidator.validateFieldIfEmpty(expiryDate)) return false;
        return !fieldValidator.validateFieldIfEmpty(securityCode);
    }
}

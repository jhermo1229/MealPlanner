package com.foodies.mealplanner.fragment;

import android.os.Bundle;
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
        securityCode = paymentDetailsView.findViewById(R.id.securityCode);
        saveButton = paymentDetailsView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener((personalDetailView) -> {

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
        });

        return paymentDetailsView;
    }
}

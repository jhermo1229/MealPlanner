package com.foodies.mealplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Fragment class for payment details of user
 */
public class PaymentDetailsFragment extends Fragment {

    public static final String TAG = PaymentDetailsFragment.class.getName();
    private SharedViewModel sharedViewModel;
    private FirebaseFirestore db;
    private View paymentDetailsView;
    private AppUtils appUtils = new AppUtils();
    private TextInputLayout nameOnCard, cardNumber, expiryDate, securityCode;
    private UserPaymentDetails userPaymentDetails = new UserPaymentDetails();
    private Button saveButton;



    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

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
        addDataToFirestore();


    });


        return paymentDetailsView;
    }


    private void addDataToFirestore() {

        // creating a collection reference
        // for our Firebase Firestore database.

        User user = new User();
        CollectionReference dbCourses = db.collection("userCredentials");
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        user.setEmail(sharedViewModel.getSelectedItem().getValue().getEmail());
        user.setPassword(sharedViewModel.getSelectedItem().getValue().getPassword());
        user.setUserDetails(sharedViewModel.getSelectedItem().getValue().getUserDetails());
        user.setUserMealDetails(sharedViewModel.getSelectedItem().getValue().getUserMealDetails());
        user.setUserPaymentDetails(userPaymentDetails);

        // below method is use to add data to Firebase Firestore.
        dbCourses.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                //Toast.makeText(getContext(), "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EditText text = new EditText(getActivity());
                builder.setTitle("Meal Planner").setMessage("Successfully Created Meal Order").setView(text);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int ii) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Call Only, if you wants to clears the activity stack else ignore it.
                        startActivity(i);
                        getActivity().finish();
                    }  });
                builder.create().show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                //Toast.makeText(getContext(), "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

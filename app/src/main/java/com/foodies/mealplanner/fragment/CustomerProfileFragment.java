package com.foodies.mealplanner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Address;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.model.UserDetails;
import com.foodies.mealplanner.model.UserPaymentDetails;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.util.AppUtils;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.CustomerViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * User profile fragment
 */
public class CustomerProfileFragment extends Fragment {

    public static final String TAG = CustomerProfileFragment.class.getName();
    public static final String BLANK = " ";
    public static final int TEN = 10;
    public static final String REQUIRED_ERROR = "Required";
    public static final String INVALID_LENGTH = "Invalid length";
    private final FieldValidator fieldValidator = new FieldValidator();
    private final UserRepository userDb = new UserRepository();
    // instance for firebase storage and StorageReference
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private final AppUtils appUtils = new AppUtils();
    private CustomerViewModel customerViewModel;
    private User user = new User();
    private EditText firstNameDialog, lastNameDialog, houseNumberDialog, streetDialog,
            cityDialog, postalCodeDialog, phoneNumberDialog, oldPasswordDialog, newPasswordDialog,
            confirmNewPasswordDialog, cardNameDialog, cardNumberDialog, expiryDateDialog, cvcDialog;
    private Button imageBtn, updatePersonalBtn, updatePaymentBtn, updateLoginBtn, cancelSubscriptionBtn;
    private Boolean isFieldChanged = false;
    private TextView fullNameView, fullAddressView, phoneView, emailView, cardNameView, cardNumberView, expiryDateView;
    private ImageView imageView, cardTypeView;
    private ActivityResultLauncher<Intent> launchSomeActivity;

    public CustomerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchSomeActivity
                = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                result -> {
                    if (result.getResultCode()
                            == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // do your operation from here....
                        if (data != null
                                && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            Log.d("IMAGE TEST", "TEST: " + selectedImageUri.toString());

                            uploadImage(selectedImageUri);

                        }
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View userProfileView = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
        user = customerViewModel.getSelectedItem().getValue();
        Log.d("<><><><><>2", "Card Number: " + user.getUserPaymentDetails().getCardNumber().toString());
        imageBtn = userProfileView.findViewById(R.id.changePhoto);
        updatePersonalBtn = userProfileView.findViewById(R.id.updatePersonal);
        updateLoginBtn = userProfileView.findViewById(R.id.updateLoginDetails);
        updatePaymentBtn = userProfileView.findViewById(R.id.updatePaymentDetails);
        cancelSubscriptionBtn = userProfileView.findViewById(R.id.cancelSubscription);
        fullNameView = userProfileView.findViewById(R.id.fullnameTxtView);
        fullAddressView = userProfileView.findViewById(R.id.addressTxtView);
        phoneView = userProfileView.findViewById(R.id.phoneNumberTxtView);
        emailView = userProfileView.findViewById(R.id.emailTextView);
        cardNameView = userProfileView.findViewById(R.id.cardNameTextView);
        cardNumberView = userProfileView.findViewById(R.id.cardNumberTextView);
        expiryDateView = userProfileView.findViewById(R.id.expiryDateTextView);
        imageView = userProfileView.findViewById(R.id.userImage);
        cardTypeView = userProfileView.findViewById(R.id.creditCardType);
        return userProfileView;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {

        Log.i("CUSTOMER VIEW", "USER: " + user.getEmail());

        loadImage();
        setFieldValue();

        imageBtn.setOnClickListener((userProfileView) -> {
            imageChooser();
        });

        updatePersonalBtn.setOnClickListener((userProfileView) -> {

            updatePersonalDetailsProcess();
        });

        updateLoginBtn.setOnClickListener((userProfileView) -> {

            updateLoginDetailsProcess();
        });

        updatePaymentBtn.setOnClickListener((userProfileView) -> {

            updatePaymentDetailsProcess();
        });

        cancelSubscriptionBtn.setOnClickListener((userProfileView -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to end subscription?").setTitle("Cancel Subscription");

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Will cancel the subscription. Make status of the user to Inactive.

                    user.setStatus("Inactive");
                    userDb.updateUser(user, getActivity());
                    getActivity().finish();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }));

    }

    private void updatePaymentDetailsProcess() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater loginInflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = loginInflater.inflate(R.layout.update_payment_dialog, null);
        cardNameDialog = dialogView.findViewById(R.id.nameOnCardUpdate);
        cardNumberDialog = dialogView.findViewById(R.id.cardNumberUpdate);
        expiryDateDialog = dialogView.findViewById(R.id.expiryDateUpdate);
        cvcDialog = dialogView.findViewById(R.id.cvcUpdate);
        cardNumberDialog.setText(user.getUserPaymentDetails().getCardNumber().toString());
        cardNameDialog.setText(user.getUserPaymentDetails().getNameOnCard());
        expiryDateDialog.setText(user.getUserPaymentDetails().getExpiryDate().toString());
        cvcDialog.setText(user.getUserPaymentDetails().getSecurityCode());


        builder.setView(dialogView).setNegativeButton(R.string.fui_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do not use this place as we are overriding this button.
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkAllFieldsPayment()) {

                    UserPaymentDetails userPaymentDetails = new UserPaymentDetails();
                    userPaymentDetails.setCardNumber(cardNumberDialog.getText().toString());
                    userPaymentDetails.setNameOnCard(cardNameDialog.getText().toString());
                    userPaymentDetails.setExpiryDate(Integer.valueOf(expiryDateDialog.getText().toString()));
                    userPaymentDetails.setSecurityCode(cvcDialog.getText().toString());

                    user.setUserPaymentDetails(userPaymentDetails);

                    userDb.updateUser(user, getActivity());

                    customerViewModel.setSelectedItem(user);

                    setFieldValue();

                    alert.dismiss();
                }

            }
        });

    }

    private void updateLoginDetailsProcess() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater loginInflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = loginInflater.inflate(R.layout.update_login_dialog, null);
        oldPasswordDialog = dialogView.findViewById(R.id.oldPasswordUpdate);
        newPasswordDialog = dialogView.findViewById(R.id.newPasswordUpdate);
        confirmNewPasswordDialog = dialogView.findViewById(R.id.newPasswordConfirmUpdate);
        CheckBox passwordCheckbox = dialogView.findViewById(R.id.passwordCheckbox);


        //Show password or not show
        passwordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    oldPasswordDialog.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPasswordDialog.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmNewPasswordDialog.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    oldPasswordDialog.setTransformationMethod(null);
                    newPasswordDialog.setTransformationMethod(null);
                    confirmNewPasswordDialog.setTransformationMethod(null);
                }
            }
        });


        builder.setView(dialogView).setNegativeButton(R.string.fui_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do not use this place as we are overriding this button.
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkAllFieldsLogin()) {
                    user.setPassword(appUtils.encodeBase64(newPasswordDialog.getText().toString()));

                    userDb.updateUser(user, getActivity());

                    alert.dismiss();
                }

            }
        });


    }

    /**
     * Process for updating personal details
     */
    private void updatePersonalDetailsProcess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater personalInflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = personalInflater.inflate(R.layout.update_personal_dialog, null);
        firstNameDialog = dialogView.findViewById(R.id.firstNameUpdate);
        lastNameDialog = dialogView.findViewById(R.id.lastNameUpdate);
        houseNumberDialog = dialogView.findViewById(R.id.houseNumberUpdate);
        streetDialog = dialogView.findViewById(R.id.streetUpdate);
        cityDialog = dialogView.findViewById(R.id.cityUpdate);
        postalCodeDialog = dialogView.findViewById(R.id.postalCodeUpdate);
        phoneNumberDialog = dialogView.findViewById(R.id.phoneNumberUpdate);
        Spinner provinceSpinnerDialog = dialogView.findViewById(R.id.provinceSpinnerUpdate);
        String[] province = getResources().getStringArray(R.array.province_canada);

        //Set adapter of spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, province);
        provinceSpinnerDialog.setAdapter(adapter);

        int spinnerPos = adapter.getPosition(user.getUserDetails().getAddress().getProvince());

        provinceSpinnerDialog.setSelection(spinnerPos);

        //Add listener if spinner has been changed.
        provinceSpinnerDialog.setOnItemSelectedListener(spinnerWatcher(spinnerPos));

        firstNameDialog.setText(user.getUserDetails().getFirstName());
        lastNameDialog.setText(user.getUserDetails().getLastName());
        houseNumberDialog.setText(user.getUserDetails().getAddress().getHouseNumber());
        streetDialog.setText(user.getUserDetails().getAddress().getStreet());
        cityDialog.setText(user.getUserDetails().getAddress().getCity());
        postalCodeDialog.setText(user.getUserDetails().getAddress().getPostalCode());
        phoneNumberDialog.setText(user.getUserDetails().getPhoneNumber());

        firstNameDialog.setEnabled(false);
        lastNameDialog.setEnabled(false);

        //Add text change listener if a field has been changed.
        firstNameDialog.addTextChangedListener(textWatcher());
        lastNameDialog.addTextChangedListener(textWatcher());
        houseNumberDialog.addTextChangedListener(textWatcher());
        streetDialog.addTextChangedListener(textWatcher());
        cityDialog.addTextChangedListener(textWatcher());
        postalCodeDialog.addTextChangedListener(textWatcher());
        phoneNumberDialog.addTextChangedListener(textWatcher());

        builder.setView(dialogView).setNegativeButton(R.string.fui_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do not use this place as we are overriding this button.
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFieldChanged) {
                    if (checkAllFieldsPersonal()) {


                        UserDetails userDetails = new UserDetails();
                        userDetails.setFirstName(firstNameDialog.getText().toString());
                        userDetails.setLastName(lastNameDialog.getText().toString());
                        userDetails.setPhoneNumber(phoneNumberDialog.getText().toString());
                        Address address = new Address();
                        address.setHouseNumber(houseNumberDialog.getText().toString());
                        address.setStreet(streetDialog.getText().toString());
                        address.setCity(cityDialog.getText().toString());
                        address.setProvince(provinceSpinnerDialog.getSelectedItem().toString());
                        address.setPostalCode(postalCodeDialog.getText().toString());
                        userDetails.setAddress(address);

                        user.setUserDetails(userDetails);

                        userDb.updateUser(user, getActivity());

                        customerViewModel.setSelectedItem(user);

                        setFieldValue();

                        alert.dismiss();
                    } else {
                        Toast toast = Toast.makeText(alert.getContext(), "No field was updated", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    /**
     * Setting field value to reset value
     */
    private void setFieldValue() {
        fullNameView.setText(user.getUserDetails().getFirstName() + BLANK + user.getUserDetails().getLastName());
        Address add = user.getUserDetails().getAddress();
        fullAddressView.setText(add.getHouseNumber() + BLANK + add.getStreet() + BLANK + add.getCity()
                + BLANK + add.getProvince() + BLANK + add.getPostalCode());
        phoneView.setText(user.getUserDetails().getPhoneNumber());
        emailView.setText(user.getEmail());
        cardNameView.setText(user.getUserPaymentDetails().getNameOnCard());
        Log.d("<>><<><><>", "Card number: " + user.getUserPaymentDetails().getCardNumber().toString());
        cardNumberView.setText(user.getUserPaymentDetails().getCardNumber().toString());
        expiryDateView.setText(user.getUserPaymentDetails().getExpiryDate().toString());

        //update image view for card type
        if (cardNumberView.getText().charAt(0) == '3') {
            cardTypeView.setImageResource(R.drawable.amex_icon);
        } else if (cardNumberView.getText().charAt(0) == '4') {
            cardTypeView.setImageResource(R.drawable.visa_icon);
        } else if (cardNumberView.getText().charAt(0) == '5') {
            cardTypeView.setImageResource(R.drawable.mastercard_icon);
        }
    }

    /**
     * Method for loading image using url
     */
    private void loadImage() {
        StorageReference mRef = storageReference.child(user.getImageUrl());
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            Log.e("Customer Profile", "Error creating file" + e.toString());
        }

        File finalLocalFile = localFile;
        mRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    // UploadImage method
    private void uploadImage(Uri filePath) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/users/"
                                    + user.getEmail());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    loadImage();
                                    Toast
                                            .makeText(getContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /**
     * Check if a change is done on a spinner.
     *
     * @return adapter - spinner adapt to be used.
     */
    @NonNull
    private AdapterView.OnItemSelectedListener spinnerWatcher(int spinnerPosition) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerPosition != i) {
                    Log.i("SPINNER POS", "T/F: " + isFieldChanged);
                    isFieldChanged = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    /**
     * Check all required fields if value is present
     * Check also if inputs are valid
     *
     * @return boolean, true if all valid
     */
    private boolean checkAllFieldsPersonal() {

        boolean allValid = true;
        errorReset();

        if (fieldValidator.validateFieldIfEmpty(firstNameDialog.length())) {
            firstNameDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(lastNameDialog.length())) {
            lastNameDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(houseNumberDialog.length())) {
            houseNumberDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(streetDialog.length())) {
            streetDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(cityDialog.length())) {
            cityDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(postalCodeDialog.length())) {
            postalCodeDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(phoneNumberDialog.length())) {
            phoneNumberDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        //Check first if it has a value then check the length. So that error message wont overlap
        if (allValid && fieldValidator.validateIfInputIsLess(TEN, phoneNumberDialog.length())) {
            phoneNumberDialog.setError(INVALID_LENGTH);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Check all required fields if value is present
     * Check also if inputs are valid
     *
     * @return boolean, true if all valid
     */
    private boolean checkAllFieldsLogin() {

        boolean allValid = true;
        errorResetLogin();

        if (fieldValidator.validateFieldIfEmpty(oldPasswordDialog.length())) {
            oldPasswordDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(newPasswordDialog.length())) {
            newPasswordDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(confirmNewPasswordDialog.length())) {
            confirmNewPasswordDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        String oldPasswordDecode = appUtils.decodeBase64(user.getPassword());
        if (allValid && (!oldPasswordDecode.equals(oldPasswordDialog.getText().toString()))) {
            oldPasswordDialog.setError("Incorrect password");
            allValid = false;
        }

        if (allValid && (!newPasswordDialog.getText().toString()
                .equals(confirmNewPasswordDialog.getText().toString()))) {
            confirmNewPasswordDialog.setError("Password does not match");
            newPasswordDialog.setError("Password does not match");
            allValid = false;
        }

        return allValid;
    }

    /**
     * Check all required fields if value is present
     * Check also if inputs are valid
     *
     * @return boolean, true if all valid
     */
    private boolean checkAllFieldsPayment() {

        boolean allValid = true;
        errorResetPayment();

        if (fieldValidator.validateFieldIfEmpty(cardNumberDialog.length())) {
            cardNumberDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(cardNameDialog.length())) {
            cardNameDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(expiryDateDialog.length())) {
            expiryDateDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(cvcDialog.length())) {
            cvcDialog.setError(REQUIRED_ERROR);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Reset error messages on field
     */
    private void errorReset() {
        firstNameDialog.setError(null);
        lastNameDialog.setError(null);
        houseNumberDialog.setError(null);
        streetDialog.setError(null);
        cityDialog.setError(null);
        postalCodeDialog.setError(null);
        phoneNumberDialog.setError(null);
    }

    private void errorResetLogin() {
        oldPasswordDialog.setError(null);
        newPasswordDialog.setError(null);
        confirmNewPasswordDialog.setError(null);
    }

    private void errorResetPayment() {
        cardNameDialog.setError(null);
        cardNumberDialog.setError(null);
        expiryDateDialog.setError(null);
        cvcDialog.setError(null);
    }
}
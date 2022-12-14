package com.foodies.mealplanner.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.MealViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

/**
 * Meal view of details and update fragment.
 *
 * @author herje
 * @version 1
 */
public class MealViewUpdateFragment extends Fragment {

    public static final String ACTIVE = "Active";
    public static final String NO_FIELD_WAS_UPDATED = "No field was updated";
    public static final int TWO = 2;
    private static final String REQUIRED_ERROR = "Required";
    private final MealRepository mealRepository = new MealRepository();
    private final FieldValidator fieldValidator = new FieldValidator();
    // instance for firebase storage and StorageReference
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private Meal meal = new Meal();
    private View mealUpdateFragmentView;
    private MealViewModel mealViewModel;
    private EditText mealNameTxt, mealDescriptionTxt, mealIngredientTxt, mealPriceTxt;
    private Spinner mealTypeSpinner;
    private Button updateMealButton, okButton, cancelButton, mealImageButton;
    private String[] mealTypeList;
    private boolean isFieldChanged = false;
    private ActivityResultLauncher<Intent> launchSomeActivity;
    private ImageView mealImageView;
    private Boolean isImageChanged = false;

    public MealViewUpdateFragment() {
        // Required empty public constructor
    }

    /**
     * On create, set activity for image.
     *
     * @param savedInstanceState - current instance of fragment
     */
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

                            uploadImage(selectedImageUri);

                        }
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mealUpdateFragmentView = inflater.inflate(R.layout.fragment_meal_view_update, container, false);
        mealViewModel = new ViewModelProvider(requireActivity()).get(MealViewModel.class);
        meal = mealViewModel.getSelectedItem().getValue();
        //Get fields
        mealNameTxt = mealUpdateFragmentView.findViewById(R.id.mealNameUpdateEditText);
        mealDescriptionTxt = mealUpdateFragmentView.findViewById(R.id.mealDescUpdateEditText);
        mealIngredientTxt = mealUpdateFragmentView.findViewById(R.id.mealIngrUpdateEditText);
        mealTypeSpinner = mealUpdateFragmentView.findViewById(R.id.mealTypeUpdate);
        mealPriceTxt = mealUpdateFragmentView.findViewById(R.id.mealPriceUpdateEditText);
        mealTypeList = getResources().getStringArray(R.array.mealTypeList);

        //Adapter Design Pattern
        ArrayAdapter<String> adapterMealType = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, mealTypeList);
        mealTypeSpinner.setAdapter(adapterMealType);
        updateMealButton = mealUpdateFragmentView.findViewById(R.id.updateMealButton);
        okButton = mealUpdateFragmentView.findViewById(R.id.okButtonMealUpdate);
        cancelButton = mealUpdateFragmentView.findViewById(R.id.cancelButtonMealUpdate);
        mealImageButton = mealUpdateFragmentView.findViewById(R.id.changeMealImageButton);
        mealImageView = mealUpdateFragmentView.findViewById(R.id.mealImageUpdate);

        if (meal.getImageUrl() != null) {
            loadImage();
        }

        //Observer Design Pattern
        //choose image in root
        mealImageButton.setOnClickListener((mealUpdateFragmentView) -> {
            imageChooser();
        });

        //Disable fields on initialize
        setFieldDisabled();

        //Observer Design Pattern
        //On click of update button, enables all the field.
        updateMealButton.setOnClickListener(mealUpdateFragmentView -> {

            okButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            updateMealButton.setVisibility(View.INVISIBLE);
            setFieldEnabled();
        });


        //Add meal values to the field
        int spinnerPos = setValuesOnField(adapterMealType, meal);

        //Set listeners to the field if a change has been done
        mealNameTxt.addTextChangedListener(textWatcher());
        mealDescriptionTxt.addTextChangedListener(textWatcher());
        mealIngredientTxt.addTextChangedListener(textWatcher());
        mealPriceTxt.addTextChangedListener(textWatcher());
        mealTypeSpinner.setOnItemSelectedListener(spinnerWatcher(spinnerPos));

        //Observer Design Pattern
        cancelButton.setOnClickListener((mealUpdateFragmentView) -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        //Observer Design Pattern
        //Applies all changes
        okButton.setOnClickListener((mealUpdateFragmentView) -> {

            //Check first if any field has changed
            if (isFieldChanged || isImageChanged) {
                if (checkAllFields()) {
                    meal.setMealName(mealNameTxt.getText().toString());
                    meal.setMealStatus(ACTIVE);
                    meal.setMealDescription(mealDescriptionTxt.getText().toString());
                    meal.setMealIngredients(mealIngredientTxt.getText().toString());

                    //Set number currency and price
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    format.setMaximumFractionDigits(TWO);
                    meal.setMealPrice(mealPriceTxt.getText().toString());
                    meal.setMealType(mealTypeSpinner.getSelectedItem().toString());


                    //update meal
                    mealRepository.updateMeal(meal, getActivity());
                    getParentFragmentManager().popBackStackImmediate();
                }
                //check field validation
            } else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), NO_FIELD_WAS_UPDATED, Toast.LENGTH_SHORT);
                toast.show();

            }

        });

        return mealUpdateFragmentView;
    }

    /**
     * Set initial values on load
     * Builder Design Pattern
     *
     * @param adapterMealType - Spinner adapter with current list.
     * @param meal            - current object
     * @returns the current position of the spinner.
     */
    private int setValuesOnField(ArrayAdapter<String> adapterMealType, Meal meal) {
        mealNameTxt.setText(meal.getMealName());
        mealDescriptionTxt.setText(meal.getMealDescription());
        mealIngredientTxt.setText(meal.getMealIngredients());
        mealPriceTxt.setText(String.valueOf(meal.getMealPrice()));
        mealTypeList = getResources().getStringArray(R.array.mealTypeList);
        int spinnerPos = adapterMealType.getPosition(meal.getMealType());
        mealTypeSpinner.setSelection(spinnerPos);

        return spinnerPos;
    }

    /**
     * Check if a change is done on a spinner.
     * Adapter Design Pattern
     * @return Triggers if spinner is changed
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
     * Observer Design Pattern
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

        if (fieldValidator.validateFieldIfEmpty(mealNameTxt.length())) {
            mealNameTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(mealDescriptionTxt.length())) {
            mealDescriptionTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(mealIngredientTxt.length())) {
            mealIngredientTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(mealPriceTxt.length())) {
            mealPriceTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Reset error messages on field
     */
    private void errorReset() {
        mealNameTxt.setError(null);
        mealDescriptionTxt.setError(null);
        mealIngredientTxt.setError(null);
        mealPriceTxt.setError(null);
    }

    /**
     * Sets all the field disabled.
     */
    private void setFieldDisabled() {
        mealNameTxt.setEnabled(false);
        mealDescriptionTxt.setEnabled(false);
        mealIngredientTxt.setEnabled(false);
        mealPriceTxt.setEnabled(false);
        mealTypeSpinner.setEnabled(false);
        mealImageButton.setEnabled(false);
    }

    /**
     * Sets all the fields enabled.
     */
    private void setFieldEnabled() {
        mealDescriptionTxt.setEnabled(true);
        mealIngredientTxt.setEnabled(true);
        mealPriceTxt.setEnabled(true);
        mealTypeSpinner.setEnabled(true);
        mealImageButton.setEnabled(true);
    }

    /**
     * uploads the image in the cloud.
     *
     * @param filePath - current location of the file in the device.
     */
    private void uploadImage(Uri filePath) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference of meals in the cloud.
            StorageReference ref
                    = storageReference
                    .child(
                            "images/meals/"
                                    + meal.getMealName());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Image uploaded successfully
                                            meal.setImageUrl(uri.toString());

                                            isImageChanged = true;
                                            //loads image in the view
                                            loadImage();
                                        }
                                    });

                                    // Dismiss dialog
                                    progressDialog.dismiss();

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
     * Open the images folder in the root of the device.
     */
    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    /**
     * Method for loading image using url
     */
    private void loadImage() {

        //open source Picasso
        Picasso.get().load(meal.getImageUrl())
                .into(mealImageView);
    }
}
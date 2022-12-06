package com.foodies.mealplanner.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 */
public class MealViewUpdateFragment extends Fragment {

    private static final String REQUIRED_ERROR = "Required";
    private final MealRepository mealRepository = new MealRepository();
    private Meal meal = new Meal();
    private final FieldValidator fieldValidator = new FieldValidator();
    // instance for firebase storage and StorageReference
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private View mealUpdateFragmentView;
    private MealViewModel mealViewModel;
    private EditText mealNameTxt, mealDescriptionTxt, mealIngredientTxt, mealPriceTxt;
    private Spinner mealTypeSpinner;
    private Button updateMealButton, okButton, cancelButton, mealImageButton;
    private String[] mealTypeList;
    private boolean isFieldChanged = false;
    private ActivityResultLauncher<Intent> launchSomeActivity;
    private ImageView mealImageView;

    public MealViewUpdateFragment() {
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
        } else {
            Bitmap myLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.fui_ic_twitter_bird_white_24dp);
            mealImageView.setImageBitmap(myLogo);
        }

        mealImageButton.setOnClickListener((mealUpdateFragmentView) -> {
            imageChooser();
        });



        setFieldDisabled();

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

        cancelButton.setOnClickListener((mealUpdateFragmentView) -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        okButton.setOnClickListener((mealUpdateFragmentView) -> {

            //Check first if any field has changed
            if (!isFieldChanged) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No field was updated", Toast.LENGTH_SHORT);
                toast.show();
                //check field validation
            } else if (checkAllFields()) {
                meal.setMealName(mealNameTxt.getText().toString());
                meal.setMealStatus("Active");
                meal.setMealDescription(mealDescriptionTxt.getText().toString());
                meal.setMealIngredients(mealIngredientTxt.getText().toString());

                //Set number currency and price
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMaximumFractionDigits(2);
                meal.setMealPrice(format.format(Double.valueOf(mealPriceTxt.getText().toString())));
                meal.setMealType(mealTypeSpinner.getSelectedItem().toString());


                //update meal
                mealRepository.updateMeal(meal, getActivity());
                getParentFragmentManager().popBackStackImmediate();
            }

        });

        return mealUpdateFragmentView;
    }

    private int setValuesOnField(ArrayAdapter<String> adapterMealType, Meal liveMeal) {
        mealNameTxt.setText(liveMeal.getMealName());
        mealDescriptionTxt.setText(liveMeal.getMealDescription());
        mealIngredientTxt.setText(liveMeal.getMealIngredients());
        mealPriceTxt.setText(String.valueOf(liveMeal.getMealPrice()));
        mealTypeList = getResources().getStringArray(R.array.mealTypeList);
        int spinnerPos = adapterMealType.getPosition(liveMeal.getMealType());
        mealTypeSpinner.setSelection(spinnerPos);

        return spinnerPos;
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

    private void setFieldDisabled() {
        mealNameTxt.setEnabled(false);
        mealDescriptionTxt.setEnabled(false);
        mealIngredientTxt.setEnabled(false);
        mealPriceTxt.setEnabled(false);
        mealTypeSpinner.setEnabled(false);
        mealImageButton.setEnabled(false);
    }

    private void setFieldEnabled() {
        mealDescriptionTxt.setEnabled(true);
        mealIngredientTxt.setEnabled(true);
        mealPriceTxt.setEnabled(true);
        mealTypeSpinner.setEnabled(true);
        mealImageButton.setEnabled(true);
    }

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
                                            // Dismiss dialog
                                            meal.setImageUrl(uri.toString());
                                            mealRepository.updateMeal(meal, getActivity());
                                            loadImage();
                                        }
                                    });


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

        Picasso.get().load(meal.getImageUrl())
                .into(mealImageView);
    }
}
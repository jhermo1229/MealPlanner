package com.foodies.mealplanner.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.validations.FieldValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

/**
 * Meal adder fragment
 * @author herje
 * @version 1
 */
public class MealAddFragment extends Fragment {

    public static final String ACTIVE = "Active";
    private static final String REQUIRED_ERROR = "Required";
    private final FieldValidator fieldValidator = new FieldValidator();
    // instance for firebase storage and StorageReference
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private View mealProfileFragmentView;
    private EditText mealNameTxt, mealDescriptionTxt, mealIngredientTxt, mealPriceTxt;
    private Spinner mealTypeSpinner;
    private final Meal meal = new Meal();
    private String[] mealTypeList;
    private Button okButton, cancelButton, mealImageAddButton;
    private final MealRepository mealRepository = new MealRepository();
    private ImageView imageView;
    private ActivityResultLauncher<Intent> launchSomeActivity;

    public static MealAddFragment newInstance() {
        return new MealAddFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mealProfileFragmentView = inflater.inflate(R.layout.fragment_meal_add, container, false);

        //Get fields
        mealNameTxt = mealProfileFragmentView.findViewById(R.id.mealNameEditText);
        mealDescriptionTxt = mealProfileFragmentView.findViewById(R.id.mealDescEditText);
        mealIngredientTxt = mealProfileFragmentView.findViewById(R.id.mealIngrEditText);
        mealTypeSpinner = mealProfileFragmentView.findViewById(R.id.mealType);
        mealPriceTxt = mealProfileFragmentView.findViewById(R.id.mealPriceEditText);
        mealTypeList = getResources().getStringArray(R.array.mealTypeList);
        ArrayAdapter<String> adapterMealType = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, mealTypeList);
        mealTypeSpinner.setAdapter(adapterMealType);
        okButton = mealProfileFragmentView.findViewById(R.id.okButtonMealAdd);
        cancelButton = mealProfileFragmentView.findViewById(R.id.cancelButtonMealAdd);
        mealImageAddButton = mealProfileFragmentView.findViewById(R.id.mealAddImageButton);
        imageView = mealProfileFragmentView.findViewById(R.id.mealImageAdd);

        //Add image and load on image view
        mealImageAddButton.setOnClickListener((mealProfileFragmentView) -> {
            imageChooser();
        });

        //Saves the new meal in the database
        okButton.setOnClickListener((mealProfileFragmentView) -> {

            //check first if all valid
            if (checkAllFields()) {
                meal.setMealName(mealNameTxt.getText().toString());
                meal.setMealDescription(mealDescriptionTxt.getText().toString());
                meal.setMealIngredients(mealIngredientTxt.getText().toString());
                meal.setMealType(mealTypeSpinner.getSelectedItem().toString());

                //Set currency and number format on price
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMaximumFractionDigits(2);
                meal.setMealPrice(format.format(Double.valueOf(mealPriceTxt.getText().toString())));
                meal.setMealStatus(ACTIVE);

                mealRepository.addMeal(meal, getActivity());
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        //cancels the adding of meal
        cancelButton.setOnClickListener((mealProfileFragmentView) -> {
            getParentFragmentManager().popBackStackImmediate();
        });

        return mealProfileFragmentView;
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
                                            loadImage(uri);
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
    private void loadImage(Uri uri) {

        //open source Picasso
        Picasso.get().load(uri)
                .into(imageView);
    }

}
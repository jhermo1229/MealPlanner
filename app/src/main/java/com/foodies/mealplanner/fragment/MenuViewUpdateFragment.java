package com.foodies.mealplanner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.adapter.MealListViewAdapter;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.MenuViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for viewing and updating menu
 *
 * @author herje
 * @version 1
 */
public class MenuViewUpdateFragment extends Fragment {

    private static final String REQUIRED_ERROR = "Required";
    private final FieldValidator fieldValidator = new FieldValidator();
    private final List meatNameList = new ArrayList<>();
    private final List vegetableNameList = new ArrayList<>();
    private final List bothNameList = new ArrayList<>();
    private final MealRepository mealRepository = new MealRepository();
    private final MenuRepository menuRepository = new MenuRepository();
    // instance for firebase storage and StorageReference
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private Menu menu = new Menu();
    private View menuViewUpdateFragment;
    private Button updateMenuButton, okButton, cancelButton, menuImageButton;
    private EditText menuNameTxt, meatMenuTxt, vegetableMenuTxt, bothMenuTxt;
    private ListView meatListView, vegetableListView, bothListView;
    private MenuViewModel menuViewModel;
    private boolean isFieldChanged = false;
    private ImageView menuImageView;
    private ActivityResultLauncher<Intent> launchSomeActivity;
    private Boolean isImageChanged = false;

    public MenuViewUpdateFragment() {
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
        menuViewUpdateFragment = inflater.inflate(R.layout.fragment_menu_view_update, container, false);
        menuViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        menu = menuViewModel.getSelectedItem().getValue();

        menuNameTxt = menuViewUpdateFragment.findViewById(R.id.menuNameUpdateEditText);
        meatMenuTxt = menuViewUpdateFragment.findViewById(R.id.meatMenuUpdateEditText);
        vegetableMenuTxt = menuViewUpdateFragment.findViewById(R.id.vegetableMenuUpdateEditText);
        bothMenuTxt = menuViewUpdateFragment.findViewById(R.id.bothMenuUpdateEditText);
        okButton = menuViewUpdateFragment.findViewById(R.id.okButtonMenuUpdate);
        cancelButton = menuViewUpdateFragment.findViewById(R.id.cancelButtonMenuUpdate);
        updateMenuButton = menuViewUpdateFragment.findViewById(R.id.updateMenuButton);
        menuImageButton = menuViewUpdateFragment.findViewById(R.id.changeMenuImageButton);

        meatListView = menuViewUpdateFragment.findViewById(R.id.meatListViewUpdate);
        vegetableListView = menuViewUpdateFragment.findViewById(R.id.vegetableListViewUpdate);
        bothListView = menuViewUpdateFragment.findViewById(R.id.bothListViewUpdate);
        menuImageView = menuViewUpdateFragment.findViewById(R.id.menuImageUpdate);

        if (menu.getImageUrl() != null) {
            loadImage();
        }

        //choose image in root
        menuImageButton.setOnClickListener(menuViewUpdateFragment -> {
            imageChooser();
        });

        //Disable fields on initialize
        setFieldDisabled();

        //On click of update button, enables all the field.
        updateMenuButton.setOnClickListener(menuViewUpdateFragment -> {

            okButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            updateMenuButton.setVisibility(View.INVISIBLE);
            setFieldEnabled();

            //Set listeners to the field if a change has been done
            menuNameTxt.addTextChangedListener(textWatcher());
            meatMenuTxt.addTextChangedListener(textWatcher());
            vegetableMenuTxt.addTextChangedListener(textWatcher());
            bothMenuTxt.addTextChangedListener(textWatcher());

            mealRepository.getAllMealType(mealList -> {
                for (Meal meal : mealList) {
                    meatNameList.add(meal.getMealName());
                }

                MealListViewAdapter adapter = new MealListViewAdapter(getContext(), mealList, 0);
                meatListView.setAdapter(adapter);

                //Set header of list
                TextView textView = new TextView(getContext());
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setText("Meat List");

                meatListView.addHeaderView(textView, null, false);

                meatListView.setClickable(true);
                meatListView.setNestedScrollingEnabled(true);
                meatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //Need to subtract since we added a header
                        Meal meal = mealList.get(i - 1);

                        new AlertDialog.Builder(getContext())
                                .setTitle(meal.getMealName())
                                .setMessage("Description: " + meal.getMealDescription() +
                                        "\nIngredients: " + meal.getMealIngredients() +
                                        "\nPrice: CAD " + meal.getMealPrice())

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        meatMenuTxt.setText(meal.getMealName());
                                        MenuViewUpdateFragment.this.menu.setMeatMeal(meal);
                                    }
                                })
                                .show();

                    }
                });

            }, "Meat");

            //Vegetable
            mealRepository.getAllMealType(vegetableMealList -> {

                MealListViewAdapter adapter = new MealListViewAdapter(getContext(), vegetableMealList, 0);
                vegetableListView.setAdapter(adapter);

                //Set header of list
                TextView textView = new TextView(getContext());
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setText("Vegetable List");

                vegetableListView.addHeaderView(textView, null, false);

                vegetableListView.setClickable(true);
                vegetableListView.setNestedScrollingEnabled(true);
                vegetableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {

                        //Need to subtract since we added a header
                        Meal meal = vegetableMealList.get(j - 1);

                        new AlertDialog.Builder(getContext())
                                .setTitle(meal.getMealName())
                                .setMessage("Description: " + meal.getMealDescription() +
                                        "\nIngredients: " + meal.getMealIngredients() +
                                        "\nPrice: CAD " + meal.getMealPrice())

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        vegetableMenuTxt.setText(meal.getMealName());
                                        menu.setVegetableMeal(meal);
                                    }
                                })
                                .show();

                    }
                });

            }, "Vegetable");


            //Both Meat and Vegetable
            mealRepository.getAllMealType(bothMealList -> {

                MealListViewAdapter adapter = new MealListViewAdapter(getContext(), bothMealList, 0);
                bothListView.setAdapter(adapter);

                //Set header of list
                TextView textView = new TextView(getContext());
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setText("Meat And Vegetable List");

                bothListView.addHeaderView(textView, null, false);

                bothListView.setClickable(true);
                bothListView.setNestedScrollingEnabled(true);
                bothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int k, long l) {

                        //Need to subtract since we added a header
                        Meal meal = bothMealList.get(k - 1);

                        new AlertDialog.Builder(getContext())
                                .setTitle(meal.getMealName())
                                .setMessage("Description: " + meal.getMealDescription() +
                                        "\nIngredients: " + meal.getMealIngredients() +
                                        "\nPrice: CAD " + meal.getMealPrice())

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        bothMenuTxt.setText(meal.getMealName());
                                        menu.setBothMeal(meal);
                                    }
                                })
                                .show();

                    }
                });

            }, "Both");

            okButton.setOnClickListener((menuAddFragmentView) -> {

                if (isFieldChanged || isImageChanged) {
                    if (checkAllFields()) {
                        menu.setMenuName(menuNameTxt.getText().toString());
                        menuRepository.addMenu(menu, getActivity());
                        getParentFragmentManager().popBackStackImmediate();
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No field was updated", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            cancelButton.setOnClickListener((menuAddFragmentView) -> {

                getParentFragmentManager().popBackStackImmediate();
            });
        });


        //Set initial values
        menuNameTxt.setText(menu.getMenuName());
        menu.setMeatMeal(menu.getMeatMeal());
        menu.setVegetableMeal(menu.getVegetableMeal());
        menu.setBothMeal(menu.getBothMeal());
        meatMenuTxt.setText(menu.getMeatMeal().getMealName());
        vegetableMenuTxt.setText(menu.getVegetableMeal().getMealName());
        bothMenuTxt.setText(menu.getBothMeal().getMealName());

        return menuViewUpdateFragment;
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

        if (fieldValidator.validateFieldIfEmpty(menuNameTxt.length())) {
            menuNameTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(meatMenuTxt.length())) {
            meatMenuTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(vegetableMenuTxt.length())) {
            vegetableMenuTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(bothMenuTxt.length())) {
            bothMenuTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Reset error messages on field
     */
    private void errorReset() {
        meatMenuTxt.setError(null);
        menuNameTxt.setError(null);
        vegetableMenuTxt.setError(null);
        bothMenuTxt.setError(null);
    }

    private void setFieldDisabled() {
        menuNameTxt.setEnabled(false);
        meatMenuTxt.setEnabled(false);
        vegetableMenuTxt.setEnabled(false);
        bothMenuTxt.setEnabled(false);
        meatListView.setEnabled(false);
        vegetableListView.setEnabled(false);
        bothListView.setEnabled(false);
        menuImageButton.setEnabled(false);
    }

    private void setFieldEnabled() {
        menuNameTxt.setEnabled(true);
        meatMenuTxt.setEnabled(true);
        vegetableMenuTxt.setEnabled(true);
        bothMenuTxt.setEnabled(true);
        meatListView.setEnabled(true);
        vegetableListView.setEnabled(true);
        bothListView.setEnabled(true);
        menuImageButton.setEnabled(true);
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
                            "images/menu/"
                                    + menu.getMenuName());

            Log.d("IMAGE", " " + menu.getMenuName());

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
                                            menu.setImageUrl(uri.toString());
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
        Picasso.get().load(menu.getImageUrl())
                .into(menuImageView);
    }

}
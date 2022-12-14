package com.foodies.mealplanner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.adapter.MealListViewAdapter;
import com.foodies.mealplanner.adapter.MenuListViewAdapter;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.validations.FieldValidator;
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
 * Fragment for adding a menu
 * @author herje
 * @version 1
 */
public class MenuAddFragment extends Fragment {

    public static final String TAG = MenuAddFragment.class.getName();
    public static final String VEGETABLE_LIST = "Vegetable List";
    public static final String VEGETABLE = "Vegetable";
    public static final String MEAT_AND_VEGETABLE_LIST = "Meat And Vegetable List";
    public static final String BOTH = "Both";
    private View menuAddFragmentView;
    private Button okButton, cancelButton, menuImageAddButton;
    private EditText menuNameTxt, meatMenuTxt, vegetableMenuTxt, bothMenuTxt;
    // instance for firebase storage and StorageReference
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private MealRepository mealDb = new MealRepository();
    private MenuRepository menuDb = new MenuRepository();
    private ListView meatListView, vegetableListView, bothListView;
    private Menu menu = new Menu();
    private static final String REQUIRED_ERROR = "Required";
    private final FieldValidator fieldValidator = new FieldValidator();
    private ImageView imageView;
    private ActivityResultLauncher<Intent> launchSomeActivity;

    public MenuAddFragment() {
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
        menuAddFragmentView = inflater.inflate(R.layout.fragment_menu_add, container, false);
        menuNameTxt = menuAddFragmentView.findViewById(R.id.menuNameEditText);
        meatMenuTxt = menuAddFragmentView.findViewById(R.id.meatMenuEditText);
        vegetableMenuTxt = menuAddFragmentView.findViewById(R.id.vegetableMenuEditText);
        bothMenuTxt = menuAddFragmentView.findViewById(R.id.bothMenuEditText);
        okButton = menuAddFragmentView.findViewById(R.id.okButtonMenuAdd);
        cancelButton = menuAddFragmentView.findViewById(R.id.cancelButtonMenuAdd);
        menuImageAddButton = menuAddFragmentView.findViewById(R.id.menuAddImageButton);
        imageView = menuAddFragmentView.findViewById(R.id.menuImageAdd);

        meatListView = menuAddFragmentView.findViewById(R.id.meatListView);
        vegetableListView = menuAddFragmentView.findViewById(R.id.vegetableListView);
        bothListView = menuAddFragmentView.findViewById(R.id.bothListView);

        //Observer Design Pattern
        menuImageAddButton.setOnClickListener(menuAddFragmentView -> {
            imageChooser();
        });

        mealDb.getAllMealType(mealList -> {

            //Adapter Design Pattern
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
                    Meal meal = mealList.get(i-1);

                    //Builder Design Pattern
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
                                    menu.setMeatMeal(meal);
                                }
                            })
                            .show();

                }
            });

        }, "Meat");

        //Vegetable
        mealDb.getAllMealType(vegetableMealList -> {

            //Adapter Design Pattern
            MealListViewAdapter adapter = new MealListViewAdapter(getContext(), vegetableMealList, 0);
            vegetableListView.setAdapter(adapter);

            //Set header of list
            TextView textView = new TextView(getContext());
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(VEGETABLE_LIST);

           vegetableListView.addHeaderView(textView, null, false);

            vegetableListView.setClickable(true);
            vegetableListView.setNestedScrollingEnabled(true);
            vegetableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {

                    //Need to subtract since we added a header
                    Meal meal = vegetableMealList.get(j-1);

                    //Builder Design Pattern
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

        }, VEGETABLE);


        //Both Meat and Vegetable
        mealDb.getAllMealType(bothMealList -> {

            //Adapter Design Pattern
            MealListViewAdapter adapter = new MealListViewAdapter(getContext(), bothMealList, 0);
            bothListView.setAdapter(adapter);

            //Set header of list
            TextView textView = new TextView(getContext());
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(MEAT_AND_VEGETABLE_LIST);

            bothListView.addHeaderView(textView, null, false);

            bothListView.setClickable(true);
            bothListView.setNestedScrollingEnabled(true);
            bothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int k, long l) {

                    //Need to subtract since we added a header
                    Meal meal = bothMealList.get(k-1);

                    //Builder Design Pattern
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

        }, BOTH);

        //Observer Design Pattern
        okButton.setOnClickListener((menuAddFragmentView) ->{
            if(checkAllFields()) {
                menu.setMenuName(menuNameTxt.getText().toString());
                menuDb.addMenu(menu, getActivity());
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        //Observer Design Pattern
        cancelButton.setOnClickListener((menuAddFragmentView)->{

            if(menu.getImageUrl() != null){
                deleteImage();
            }

            getParentFragmentManager().popBackStackImmediate();
        });

        return menuAddFragmentView;
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

    /**
     * If object was not saved, delete the uploaded image in cloud.
     */
    private void deleteImage() {
        StorageReference photoRef = storage.getReferenceFromUrl(menu.getImageUrl());
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "onFailure: did not delete file");
            }
        });
    }
}
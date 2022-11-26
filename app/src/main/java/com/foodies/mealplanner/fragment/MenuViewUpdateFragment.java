package com.foodies.mealplanner.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.validations.FieldValidator;
import com.foodies.mealplanner.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for viewing and updating menu
 */
public class MenuViewUpdateFragment extends Fragment {

    private static final String REQUIRED_ERROR = "Required";
    private final FieldValidator fieldValidator = new FieldValidator();
    private View menuViewUpdateFragment;
    private Button updateMenuButton, okButton, cancelButton;
    private EditText menuNameTxt, meatMenuTxt, vegetableMenuTxt, bothMenuTxt;
    private final List meatNameList = new ArrayList<>();
    private final List vegetableNameList = new ArrayList<>();
    private final List bothNameList = new ArrayList<>();
    private final Menu menu = new Menu();
    private final MealRepository mealDb = new MealRepository();
    private final MenuRepository menuDb = new MenuRepository();
    private ListView meatListView, vegetableListView, bothListView;
    private MenuViewModel menuViewModel;
    private boolean isFieldChanged = false;

    public MenuViewUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        menuViewUpdateFragment = inflater.inflate(R.layout.fragment_menu_view_update, container, false);
        menuViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        menuNameTxt = menuViewUpdateFragment.findViewById(R.id.menuNameUpdateEditText);
        meatMenuTxt = menuViewUpdateFragment.findViewById(R.id.meatMenuUpdateEditText);
        vegetableMenuTxt = menuViewUpdateFragment.findViewById(R.id.vegetableMenuUpdateEditText);
        bothMenuTxt = menuViewUpdateFragment.findViewById(R.id.bothMenuUpdateEditText);
        okButton = menuViewUpdateFragment.findViewById(R.id.okButtonMenuUpdate);
        cancelButton = menuViewUpdateFragment.findViewById(R.id.cancelButtonMenuUpdate);
        updateMenuButton = menuViewUpdateFragment.findViewById(R.id.updateMenuButton);

        meatListView = menuViewUpdateFragment.findViewById(R.id.meatListViewUpdate);
        vegetableListView = menuViewUpdateFragment.findViewById(R.id.vegetableListViewUpdate);
        bothListView = menuViewUpdateFragment.findViewById(R.id.bothListViewUpdate);

        updateMenuButton.setText("Update");
        setFieldDisabled();


        updateMenuButton.setOnClickListener(mealUpdateFragmentView -> {

            okButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            updateMenuButton.setVisibility(View.INVISIBLE);
            setFieldEnabled();

            //Set listeners to the field if a change has been done
            menuNameTxt.addTextChangedListener(textWatcher());
            meatMenuTxt.addTextChangedListener(textWatcher());
            vegetableMenuTxt.addTextChangedListener(textWatcher());
            bothMenuTxt.addTextChangedListener(textWatcher());

            mealDb.getAllMealType(mealList -> {
                Log.d("MEAL LIST FRAG", "HERE " + mealList.size());
                for (Meal meal : mealList) {
                    meatNameList.add(meal.getMealName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.meal_listview, R.id.mealView, meatNameList);
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
                                        menu.setMeatMeal(meal);
                                    }
                                })
                                .show();

                    }
                });

            }, "Meat");

            //Vegetable
            mealDb.getAllMealType(vegetableMealList -> {
                Log.d("VEGGIE MEAL LIST FRAG", "HERE " + vegetableMealList.size());
                for (Meal meal : vegetableMealList) {
                    vegetableNameList.add(meal.getMealName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.meal_listview, R.id.mealView, vegetableNameList);
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
            mealDb.getAllMealType(bothMealList -> {
                Log.d("BOTH MEAL LIST FRAG", "HERE " + bothMealList.size());
                for (Meal meal : bothMealList) {
                    bothNameList.add(meal.getMealName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.meal_listview, R.id.mealView, bothNameList);
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

                if (isFieldChanged) {
                    if (checkAllFields()) {
                        menu.setMenuName(menuNameTxt.getText().toString());
                        menuDb.addMenu(menu, getActivity());
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


        Menu liveMenu = menuViewModel.getSelectedItem().getValue();


        menuNameTxt.setText(liveMenu.getMenuName());
        meatMenuTxt.setText(liveMenu.getMeatMeal().getMealName());
        vegetableMenuTxt.setText(liveMenu.getVegetableMeal().getMealName());
        bothMenuTxt.setText(liveMenu.getBothMeal().getMealName());

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
    }

    private void setFieldEnabled() {
        menuNameTxt.setEnabled(true);
        meatMenuTxt.setEnabled(true);
        vegetableMenuTxt.setEnabled(true);
        bothMenuTxt.setEnabled(true);
        meatListView.setEnabled(true);
        vegetableListView.setEnabled(true);
        bothListView.setEnabled(true);
    }

}
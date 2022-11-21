package com.foodies.mealplanner.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for adding a menu
 */
public class MenuAddFragment extends Fragment {

    public static final String TAG = MenuAddFragment.class.getName();
    private View menuAddFragmentView;
    private Button addMeat, addVegetable, addBoth, okButton, cancelButton;
    private EditText menuName, meatMenu, vegetableMenu, bothMenu;


    private MealRepository mealDb = new MealRepository();
    private MenuRepository menuDb = new MenuRepository();
    private ListView meatListView, vegetableListView, bothListView;
    private List meatNameList = new ArrayList<>();
    private List vegetableNameList = new ArrayList<>();
    private List bothNameList = new ArrayList<>();
    private Menu menu = new Menu();



    public MenuAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        menuAddFragmentView = inflater.inflate(R.layout.fragment_menu_add, container, false);
        menuName = menuAddFragmentView.findViewById(R.id.menuNameEditText);
        meatMenu = menuAddFragmentView.findViewById(R.id.meatMenuEditText);
        vegetableMenu = menuAddFragmentView.findViewById(R.id.vegetableMenuEditText);
        bothMenu = menuAddFragmentView.findViewById(R.id.bothMenuEditText);
        okButton = menuAddFragmentView.findViewById(R.id.okButtonMenuAdd);
        cancelButton = menuAddFragmentView.findViewById(R.id.cancelButtonMenuAdd);

        meatListView = menuAddFragmentView.findViewById(R.id.meatListView);
        vegetableListView = menuAddFragmentView.findViewById(R.id.vegetableListView);
        bothListView = menuAddFragmentView.findViewById(R.id.bothListView);

        mealDb.getAllMealType(mealList -> {
            Log.d("MEAL LIST FRAG", "HERE " + mealList.size());
               for (Meal meal : mealList) {
                meatNameList.add(meal.getMealName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.meals_listview, R.id.mealView, meatNameList);
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

                    new AlertDialog.Builder(getContext())
                            .setTitle(meal.getMealName())
                            .setMessage("Description: " + meal.getMealDescription() +
                                    "\nIngredients: " + meal.getMealIngredients() +
                                    "\nPrice: CAD " + meal.getMealPrice())

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    meatMenu.setText(meal.getMealName());
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

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.meals_listview, R.id.mealView, vegetableNameList);
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
                    Meal meal = vegetableMealList.get(j-1);

                    new AlertDialog.Builder(getContext())
                            .setTitle(meal.getMealName())
                            .setMessage("Description: " + meal.getMealDescription() +
                                    "\nIngredients: " + meal.getMealIngredients() +
                                    "\nPrice: CAD " + meal.getMealPrice())

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    vegetableMenu.setText(meal.getMealName());
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

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.meals_listview, R.id.mealView, bothNameList);
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
                    Meal meal = bothMealList.get(k-1);

                    new AlertDialog.Builder(getContext())
                            .setTitle(meal.getMealName())
                            .setMessage("Description: " + meal.getMealDescription() +
                                    "\nIngredients: " + meal.getMealIngredients() +
                                    "\nPrice: CAD " + meal.getMealPrice())

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bothMenu.setText(meal.getMealName());
                                    menu.setBothMeal(meal);
                                }
                            })
                            .show();

                }
            });

        }, "Both");

        okButton.setOnClickListener((menuAddFragmentView) ->{
            menu.setMenuName(menuName.getText().toString());


            menuDb.addMenu(menu, getActivity());
        });

        cancelButton.setOnClickListener((menuAddFragmentView)->{

            getParentFragmentManager().popBackStackImmediate();
        });

        return menuAddFragmentView;
    }
}
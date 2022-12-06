package com.foodies.mealplanner.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.adapter.MealListViewAdapter;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.viewmodel.MealViewModel;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Meal list fragment
 *
 * @author herje
 * @version 1
 */
public class MealListFragment extends Fragment {

    public static final String TAG = MealListFragment.class.getName();
    private final MealRepository db = new MealRepository();
    private View mealListFragmentView;
    private EditText searchFilter;
    private Button addMealButton;
    private Spinner sortSpinner;
    private ListView mealListView;
    private String[] sortArray;
    private MealViewModel mViewModel;

    public MealListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mealListFragmentView = inflater.inflate(R.layout.fragment_meal_list, container, false);

        //Set viewmodel
        mViewModel = new ViewModelProvider(requireActivity()).get(MealViewModel.class);

        //get fields
        searchFilter = mealListFragmentView.findViewById(R.id.searchFilterMenus);
        searchFilter.getText().clear();
        mealListView = mealListFragmentView.findViewById(R.id.mealListView);
        sortSpinner = mealListFragmentView.findViewById(R.id.sortingSpinnerMeals);
        addMealButton = mealListFragmentView.findViewById(R.id.addMealButton);
        sortArray = getResources().getStringArray(R.array.sort);

        //Set adapter of spinner
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, sortArray);
        sortSpinner.setAdapter(sortAdapter);

        //if button is clicked, go to add new meal fragment
        addMealButton.setOnClickListener((mealListFragmentView) -> {

            MealAddFragment mealAddFragment = new MealAddFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(AdminProfileFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, mealAddFragment);

            transaction.commit();
        });

        db.getAllMeals(mealList -> {

            //Spinner sorter (A-Z, Z-A)
            //Sorting will be done inside the custom adapter of listview
            //Image is loaded in the custom adapter of listview
            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    searchFilter.getText().clear();
                    if (i == 1) {
                        MealListViewAdapter adapter = new MealListViewAdapter(getActivity(), mealList, i);
                        mealListView.setAdapter(adapter);
                        textChangeListener(adapter);
                        sortSpinner.setSelection(0);
                    } else if (i == 2) {
                        MealListViewAdapter adapter = new MealListViewAdapter(getActivity(), mealList, i);
                        mealListView.setAdapter(adapter);
                        textChangeListener(adapter);
                        sortSpinner.setSelection(0);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    sortSpinner.setSelection(0);
                    return;
                }

            });
            MealListViewAdapter customAdapter = new MealListViewAdapter(getActivity(), mealList, 0);
            mealListView.setAdapter(customAdapter);
            textChangeListener(customAdapter);

            mealListView.setClickable(true);

            //Listview click listener
            mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Object obj = adapterView.getAdapter().getItem(i);
                    Meal meal = (Meal) obj;

                    mViewModel.setSelectedItem(meal);
                    MealViewUpdateFragment mealViewUpdateFragment = new MealViewUpdateFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(MealListFragment.TAG);
                    transaction.replace(R.id.adminProfileFrame, mealViewUpdateFragment);
                    transaction.commit();

                }
            });
        });
        return mealListFragmentView;
    }


    /**
     * Listener for text change in search filter
     *
     * @param adapter
     */
    private void textChangeListener(MealListViewAdapter adapter) {
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
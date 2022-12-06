package com.foodies.mealplanner.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.adapter.SpinnerAdapter;
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.MealDetailSpinner;
import com.foodies.mealplanner.repository.MealRepository;
import com.foodies.mealplanner.viewmodel.MealViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Meal list fragment
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
    private List<Bitmap> bitmapList = new ArrayList<>();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    Comparator<MealDetailSpinner> compareByName = (MealDetailSpinner md, MealDetailSpinner md2) -> md.getMealName().compareTo(md2.getMealName());


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
        mealListView = mealListFragmentView.findViewById(R.id.mealListView);
        sortSpinner = mealListFragmentView.findViewById(R.id.sortingSpinnerMeals);
        addMealButton = mealListFragmentView.findViewById(R.id.addMealButton);
        sortArray = getResources().getStringArray(R.array.sort);

        ArrayList<String> mealNameList = new ArrayList<>();
        List<MealDetailSpinner> mealDetailList = new ArrayList<>();


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

//        List<String> mealNameList2 = new ArrayList<>();
        List<String> imageUrlList = new ArrayList<>();
        db.getAllMeals(mealList -> {
            Log.d("MEAL LIST FRAG", "SIZE " + mealList.size());
            for (Meal meal : mealList) {
                imageUrlList.add(meal.getImageUrl());
                mealNameList.add(meal.getMealName());

                MealDetailSpinner md = new MealDetailSpinner();
                md.setMealName(meal.getMealName());
                md.setImageUrl(meal.getImageUrl());

                mealDetailList.add(md);
            }
            Log.d("MEAL LIST", "NUMBER OF IMAGE " + imageUrlList.size());


            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 1) {
//                        Collections.sort(mealDetailList, compareByName);
//                        Collections.sort(mealList, Comparator.comparing(o -> o.getMealName().toLowerCase()));
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.meal_listview, R.id.mealView, mealNameList);
                        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), mealNameList, imageUrlList, mealDetailList, mealList, i);
                        mealListView.setAdapter(adapter);
                        textChangeListener(adapter);
                        sortSpinner.setSelection(0);
                    } else if (i == 2) {
//                        Collections.sort(mealDetailList, compareByName.reversed());
//                        Collections.sort(mealList, (o1, o2) -> o2.getMealName().toLowerCase()
//                                .compareTo(o1.getMealName().toLowerCase()));

//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.meal_listview, R.id.mealView, mealNameList);
                        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), mealNameList, imageUrlList, mealDetailList, mealList, i);
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
            SpinnerAdapter customAdapter = new SpinnerAdapter(getActivity(), mealNameList, imageUrlList, mealDetailList, mealList, 0);
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.meal_listview, R.id.mealView, mealNameList);
              mealListView.setAdapter(customAdapter);
            textChangeListener(customAdapter);


//            mealListView.setAdapter(customAdapter);
            mealListView.setClickable(true);
            mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Meal meal = mealList.get(i);
                    Log.d("SIZE 2", " " + adapterView.getAdapter().getItem(0) + " " + i);
                    Object obj = adapterView.getAdapter().getItem(i);
                    Log.d("OBJECT", " " + obj);

                    Meal meal = (Meal) obj;

                    mViewModel.setSelectedItem(meal);
                    MealViewUpdateFragment mealViewUpdateFragment = new MealViewUpdateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("View", "MealListView");
                    mealViewUpdateFragment.setArguments(bundle);
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
    private void textChangeListener(SpinnerAdapter adapter) {
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
//
//    /**
//     * Method for loading image using url
//     */
//    private void loadImage(String url) {
//        StorageReference mRef = storageReference.child(url);
//        Log.d("Customer Profile", "IMAGE URL: " + url);
//        File localFile = null;
//        try {
//            localFile = File.createTempFile("images", "jpg");
//        } catch (IOException e) {
//            Log.e("Customer Profile", "Error creating file" + e.toString());
//        }
//
//        File finalLocalFile = localFile;
//        Log.d("Customer Profile", "IMAGE URL ABSOLUTE: " + finalLocalFile.getAbsolutePath());
//
//                Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
//                bitmapList.add(bitmap);
//
//
//    }
}
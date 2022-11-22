package com.foodies.mealplanner.fragment;

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
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.viewmodel.MealViewModel;
import com.foodies.mealplanner.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Fragment for menu list
 */
public class MenuListFragment extends Fragment {

    public static final String TAG = MenuListFragment.class.getName();
    private View menuListFragmentView;
    private Button addMenuButton;
    private final MenuRepository db = new MenuRepository();
    private ArrayList<String> menuNameList = new ArrayList<>();
    private Spinner sortSpinner;
    private ListView menuListView;
    private EditText searchFilter;
    private String[] sortArray;
    private MenuViewModel mViewModel;

    public MenuListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        menuListFragmentView = inflater.inflate(R.layout.fragment_menu_list, container, false);
        addMenuButton = menuListFragmentView.findViewById(R.id.addMenuButton);
        sortSpinner = menuListFragmentView.findViewById(R.id.sortingSpinnerMenu);
        menuListView = menuListFragmentView.findViewById(R.id.menuListView);
        searchFilter = menuListFragmentView.findViewById(R.id.searchFilterMenus);
        sortArray = getResources().getStringArray(R.array.sort);

        //Set viewmodel
        mViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        //Set adapter of spinner
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, sortArray);
        sortSpinner.setAdapter(sortAdapter);

        //Go to adding of menu
        addMenuButton.setOnClickListener((fragmentMenuListView) -> {

            MenuAddFragment menuAddFragment = new MenuAddFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, menuAddFragment);

            transaction.commit();
        });

        //Create view of the menu list
        db.getAllMenus(menuList -> {
            Log.d("MENU LIST FRAG", "SIZE: " + menuList.size());
            menuNameList = new ArrayList();
            for (Menu menu : menuList) {
                menuNameList.add(menu.getMenuName());

            }

            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 1) {
                        Collections.sort(menuNameList, Comparator.comparing(String::toLowerCase));
                        Collections.sort(menuList, Comparator.comparing(o -> o.getMenuName().toLowerCase()));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.menu_listview, R.id.menuView, menuNameList);
                        menuListView.setAdapter(adapter);
                        textChangeListener(adapter);
                        sortSpinner.setSelection(0);
                    } else if (i == 2) {
                        Collections.sort(menuNameList, (o1, o2) -> o2.toLowerCase()
                                .compareTo(o1.toLowerCase()));
                        Collections.sort(menuList, (o1, o2) -> o2.getMenuName().toLowerCase()
                                .compareTo(o1.getMenuName().toLowerCase()));

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.menu_listview, R.id.menuView, menuNameList);
                        menuListView.setAdapter(adapter);
                        textChangeListener(adapter);
                        sortSpinner.setSelection(0);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    sortSpinner.setSelection(0);
                    return;
                }

            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.menu_listview, R.id.menuView, menuNameList);
            menuListView.setAdapter(adapter);
            textChangeListener(adapter);

            menuListView.setClickable(true);
            menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Menu menu = menuList.get(i);
                    mViewModel.setSelectedItem(menu);
                    MenuViewUpdateFragment menuViewUpdateFragment = new MenuViewUpdateFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(MenuListFragment.TAG);
                    transaction.replace(R.id.loginHomeFrame, menuViewUpdateFragment);
                    transaction.commit();

                }
            });

        });

        return menuListFragmentView;
    }

    /**
     * Listener for text change in search filter
     * @param adapter
     */
    private void textChangeListener(ArrayAdapter<String> adapter) {
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
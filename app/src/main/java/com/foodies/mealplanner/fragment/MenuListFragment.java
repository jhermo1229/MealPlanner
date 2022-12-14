package com.foodies.mealplanner.fragment;

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
import com.foodies.mealplanner.adapter.MenuListViewAdapter;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.viewmodel.MenuViewModel;

/**
 * Fragment for menu list
 *
 * @author herje
 * @version 1
 */
public class MenuListFragment extends Fragment {

    public static final String TAG = MenuListFragment.class.getName();
    private final MenuRepository menuRepository = new MenuRepository();
    private View menuListFragmentView;
    private Button addMenuButton;
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
        searchFilter.getText().clear();
        sortArray = getResources().getStringArray(R.array.sort);

        //Set viewmodel
        mViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        //Set adapter of spinner
        //Adapter Design Pattern
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, sortArray);
        sortSpinner.setAdapter(sortAdapter);

        //Observer Design Pattern
        //Go to adding of menu
        addMenuButton.setOnClickListener((fragmentMenuListView) -> {

            MenuAddFragment menuAddFragment = new MenuAddFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, menuAddFragment);

            transaction.commit();
        });

        //Create view of the menu list
        menuRepository.getAllMenus(menuList -> {

            //Observer Design Pattern
            //Spinner sorter (A-Z, Z-A)
            //Sorting will be done inside the custom adapter of listview
            //Image is loaded in the custom adapter of listview
            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    searchFilter.getText().clear();
                    MenuListViewAdapter adapter = new MenuListViewAdapter(getContext(), menuList, i);
                    menuListView.setAdapter(adapter);
                    textChangeListener(adapter);
                    sortSpinner.setSelection(0);
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    sortSpinner.setSelection(0);
                    return;
                }

            });

            //Adapter Design Pattern
            MenuListViewAdapter adapter = new MenuListViewAdapter(getContext(), menuList, 0);
            menuListView.setAdapter(adapter);
            textChangeListener(adapter);

            menuListView.setClickable(true);

            //Observer Design Pattern
            menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Menu menu = (Menu) adapterView.getAdapter().getItem(i);
                    searchFilter.getText().clear();
                    mViewModel.setSelectedItem(menu);
                    MenuViewUpdateFragment menuViewUpdateFragment = new MenuViewUpdateFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(MenuListFragment.TAG);
                    transaction.replace(R.id.adminProfileFrame, menuViewUpdateFragment);
                    transaction.commit();

                }
            });

        });

        return menuListFragmentView;
    }

    /**
     * Listener for text change in search filter
     * Observer Design Pattern
     * @param adapter
     */
    private void textChangeListener(MenuListViewAdapter adapter) {
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
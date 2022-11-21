package com.foodies.mealplanner.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.foodies.mealplanner.R;

/**
 * Fragment for menu list
 */
public class MenuListFragment extends Fragment {

    public static final String TAG = MenuListFragment.class.getName();
    private View fragmentMenuListView;
    private Button addMenuButton;

    public MenuListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMenuListView = inflater.inflate(R.layout.fragment_menu_list, container, false);
        addMenuButton = fragmentMenuListView.findViewById(R.id.addMenuButton);

        addMenuButton.setOnClickListener((fragmentMenuListView)->{

            MenuAddFragment menuAddFragment = new MenuAddFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, menuAddFragment);

            transaction.commit();
        });

        return fragmentMenuListView;
    }
}
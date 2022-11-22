package com.foodies.mealplanner.fragment;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;

public class AdminProfileFragment extends Fragment {

    public static final String TAG = AdminProfileFragment.class.getName();
    private AdminProfileViewModel mViewModel;
    private View adminProfileFragmentView;
    private Button usersButton, mealsButton, menusButton, emailButton;

    public static AdminProfileFragment newInstance() {
        return new AdminProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        adminProfileFragmentView = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        usersButton = adminProfileFragmentView.findViewById(R.id.usersBtn);
        usersButton.setOnClickListener((adminProfileFragmentView)->{
            UsersListFragment userListFrag = new UsersListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(UsersListFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, userListFrag);

            transaction.commit();
            });

        mealsButton = adminProfileFragmentView.findViewById(R.id.mealsBtn);
        mealsButton.setOnClickListener((adminProfileFragmentView)->{
            MealListFragment mealListFragment = new MealListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(AdminProfileFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, mealListFragment);

            transaction.commit();

        });

        menusButton = adminProfileFragmentView.findViewById(R.id.menuBtn);
        menusButton.setOnClickListener((adminProfileFragmentView) ->{

            MenuListFragment menuListFragment = new MenuListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, menuListFragment);

            transaction.commit();
        });

        emailButton = adminProfileFragmentView.findViewById(R.id.emailBtn);
        emailButton.setOnClickListener((adminProfileFragmentView) ->{

            EmailMenuFragment emailMenuFragment = new EmailMenuFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, emailMenuFragment);

            transaction.commit();
        });


        return adminProfileFragmentView;
    }


}
package com.foodies.mealplanner.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.foodies.mealplanner.R;

/**
 * Main page of Meal Planner
 * Contains the login or signup
 */
public class LoginHomeFragment extends Fragment {

    private Button loginBtn;
    private View homeView;

    public LoginHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_login_home, container, false);
        loginBtn = homeView.findViewById(R.id.loginUserProfileBtn);

        loginBtn.setOnClickListener((personalDetailView) -> {

            UserProfileFragment userFrag = new UserProfileFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(PersonalDetailsFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, userFrag);

            transaction.commit();
        });

        return homeView;
    }

}
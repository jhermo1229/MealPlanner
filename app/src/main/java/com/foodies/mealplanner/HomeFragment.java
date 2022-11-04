package com.foodies.mealplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Main page of Meal Planner
 * Contains the login or signup
 */
public class HomeFragment extends Fragment {

    private Button signupBtn, loginBtn;
    private View homeView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);
//        signupBtn = homeView.findViewById(R.id.signupBtn);
//
//        signupBtn.setOnClickListener((personalDetailView) -> {
//
//            PersonalDetailsFragment personalFrag = new PersonalDetailsFragment();
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.addToBackStack(PersonalDetailsFragment.TAG);
//            transaction.replace(R.id.homeFrame, personalFrag);
//
//            transaction.commit();
//        });

        return homeView;
    }

}
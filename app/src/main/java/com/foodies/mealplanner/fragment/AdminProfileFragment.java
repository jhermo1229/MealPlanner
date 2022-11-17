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

    private AdminProfileViewModel mViewModel;
    private View adminProfileFragmentView;
    private Button usersButton;

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


        return adminProfileFragmentView;
    }


}
package com.foodies.mealplanner.fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.DatabaseHelper;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;
import com.foodies.mealplanner.viewmodel.SignupViewModel;

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


        adminProfileFragmentView = inflater.inflate(R.layout.admin_profile_fragment, container, false);
        usersButton = adminProfileFragmentView.findViewById(R.id.usersBtn);
        usersButton.setOnClickListener((adminProfileFragmentView)->{
            UsersListFragment adminFrag = new UsersListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(UsersListFragment.TAG);
            transaction.replace(R.id.loginHomeFrame, adminFrag);

            transaction.commit();
            });


        return adminProfileFragmentView;
    }


}
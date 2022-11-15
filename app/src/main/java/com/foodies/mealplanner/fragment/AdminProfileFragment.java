package com.foodies.mealplanner.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;

public class AdminProfileFragment extends Fragment {

    private AdminProfileViewModel mViewModel;

    public static AdminProfileFragment newInstance() {
        return new AdminProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_profile_fragment, container, false);
    }


}
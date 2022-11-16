package com.foodies.mealplanner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.fragment.LoginHomeFragment;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;
import com.foodies.mealplanner.viewmodel.SignupViewModel;

public class LoginActivity extends AppCompatActivity {

    private AdminProfileViewModel adminProfileViewModel;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adminProfileViewModel = new ViewModelProvider(this).get(AdminProfileViewModel.class);
        Log.i("Login Activity", "Login View Model Initialized");
        adminProfileViewModel.setSelectedItem(user);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace the frame layout with new fragment
        Log.d("LoginActivity", "transition to fragment");
        transaction.replace(R.id.loginHomeFrame, new LoginHomeFragment());
        transaction.commit();
    }
}
package com.foodies.mealplanner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.fragment.LoginHomeFragment;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;
import com.foodies.mealplanner.viewmodel.SignupViewModel;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace the frame layout with new fragment
        Log.d("LoginActivity", "transition to fragment");
        transaction.replace(R.id.loginHomeFrame, new LoginHomeFragment());
        transaction.commit();
    }
}
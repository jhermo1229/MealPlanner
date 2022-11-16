package com.foodies.mealplanner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.fragment.LoginHomeFragment;

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
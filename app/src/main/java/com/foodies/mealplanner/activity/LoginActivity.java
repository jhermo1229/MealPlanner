package com.foodies.mealplanner.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.fragment.LoginHomeFragment;

/**
 * Login Activity - Login page
 * @author herje
 * @version 1
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace the frame layout with new fragment
        transaction.replace(R.id.loginHomeFrame, new LoginHomeFragment());
        transaction.commit();
    }
}
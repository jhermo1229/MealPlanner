package com.foodies.mealplanner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.foodies.mealplanner.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace the frame layout with new fragment
        Log.d("LoginActivity", "transition to fragment");
        transaction.replace(R.id.loginHomeFrame, new UserProfileFragment());
        transaction.commit();
    }
}
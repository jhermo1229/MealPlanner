package com.foodies.mealplanner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.foodies.mealplanner.R;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace the frame layout with new fragment
        transaction.replace(R.id.signupHomeFrame, new PersonalDetailsFragment());
        transaction.commit();
    }
}
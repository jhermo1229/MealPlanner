package com.foodies.mealplanner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.fragment.PersonalDetailsFragment;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.viewmodel.SignupViewModel;

public class SignupActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initialize view model once
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        Log.i("Signup Activity", "Signup View Model Initialized");
        signupViewModel.setSelectedItem(user);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace the frame layout with new fragment
        transaction.replace(R.id.signupHomeFrame, new PersonalDetailsFragment());
        transaction.commit();
    }
}
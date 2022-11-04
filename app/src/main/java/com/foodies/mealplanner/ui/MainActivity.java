package com.foodies.mealplanner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.viewmodel.SharedViewModel;
import com.foodies.mealplanner.model.User;

public class MainActivity extends AppCompatActivity {

    private FrameLayout homeFrame;
    private SharedViewModel sharedViewModel;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize view model once
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        Log.i("Main Activity", "View Model Initialized");
        sharedViewModel.setSelectedItem(user);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//        //Created a fragment which will be used as home page
//        homeFrame = findViewById(R.id.homeFrame);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.homeFrame, new HomeFragment());
//        transaction.commit();
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
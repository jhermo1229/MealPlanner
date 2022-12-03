package com.foodies.mealplanner.activity;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.fragment.CustomerProfileFragment;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.viewmodel.CustomerViewModel;

/**
 * Main activity for customer.
 * @author herje
 * @version 1
 */
public class CustomerActivity extends AppCompatActivity {

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        //get passed object from login activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
        CustomerViewModel customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        customerViewModel.setSelectedItem(user);

        //Create menu item for logout
        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_layout, menu);
                MenuItem item = menu.getItem(0);
                SpannableString s = new SpannableString("(" + user.getUserDetails().getFirstName() + ") logout");
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(), R.color.blue_logo)), 0, s.length(), 0);
                s.setSpan(new AbsoluteSizeSpan(12, true), 0, s.length(), SPAN_INCLUSIVE_INCLUSIVE);
                item.setTitle(s);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.action_logout) {
                    logout();
                }

                return false;
            }
        });

        CustomerProfileFragment userFrag = new CustomerProfileFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.customerProfileFrame, userFrag);

        transaction.commit();
    }

    /**
     * Logout functionality
     * Removes all activity and start the very first activity.
     */
    private void logout() {

        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }
}
package com.foodies.mealplanner.fragment;

import androidx.annotation.RequiresApi;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.repository.EmailRepository;
import com.foodies.mealplanner.util.AppUtils;
import com.foodies.mealplanner.util.EmailUtil;
import com.foodies.mealplanner.viewmodel.AdminProfileViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class AdminProfileFragment extends Fragment {

    public static final String TAG = AdminProfileFragment.class.getName();
    private View adminProfileFragmentView;
    private Button usersButton, mealsButton, menusButton, emailButton;
    private EmailRepository emailRepo = new EmailRepository();
    private AppUtils appUtils = new AppUtils();
    private EmailUtil emailUtil = new EmailUtil();

    public static AdminProfileFragment newInstance() {
        return new AdminProfileFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        adminProfileFragmentView = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        LocalDate friday = LocalDate.now();

        String date = appUtils.dateFormatter(friday);

        Log.i("DATE", "" + date);

        emailRepo.getEmail(email -> {

        if(email != null){

            AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
            builder.setTitle("Email Alert");

            StringBuilder recipients = new StringBuilder();
            for(String emailAdd : email.getMealPlanWeek().getEmailAddressList()){
                recipients.append(emailAdd + ";");
            }

            builder.setMessage("Recipients: " + recipients.toString() +
                    "\n\nMonday Menu: " + email.getMealPlanWeek().getMondayMenu().getMenuName() +
                    "\nWednesday Menu: " + email.getMealPlanWeek().getWednesdayMenu().getMenuName() +
                    "\nFriday Menu: " + email.getMealPlanWeek().getFridayMenu().getMenuName());



            builder.setPositiveButton(R.string.sendEmail, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Will cancel the subscription. Make status of the user to Inactive.

//                    user.setStatus("Inactive");
//                    userDb.updateUser(user,getActivity());
                    emailUtil.sendEmail(getContext(), email.getMealPlanWeek());
                    emailRepo.updateEmail(email.getDeliveryDate(), true);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }

        }, date);

        usersButton = adminProfileFragmentView.findViewById(R.id.usersBtn);
        usersButton.setOnClickListener((adminProfileFragmentView)->{
            UsersListFragment userListFrag = new UsersListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(UsersListFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, userListFrag);

            transaction.commit();
            });

        mealsButton = adminProfileFragmentView.findViewById(R.id.mealsBtn);
        mealsButton.setOnClickListener((adminProfileFragmentView)->{
            MealListFragment mealListFragment = new MealListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(AdminProfileFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, mealListFragment);

            transaction.commit();

        });

        menusButton = adminProfileFragmentView.findViewById(R.id.menuBtn);
        menusButton.setOnClickListener((adminProfileFragmentView) ->{

            MenuListFragment menuListFragment = new MenuListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, menuListFragment);

            transaction.commit();
        });

        emailButton = adminProfileFragmentView.findViewById(R.id.emailBtn);
        emailButton.setOnClickListener((adminProfileFragmentView) ->{

            EmailMenuFragment emailMenuFragment = new EmailMenuFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, emailMenuFragment);

            transaction.commit();
        });

        extracted();


        return adminProfileFragmentView;
    }

    private void extracted() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

                // Add option Menu Here
                MenuItem item = menu.findItem(R.id.action_logout);
                item.setVisible(true);



            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                return false;

            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


}
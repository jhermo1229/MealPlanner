package com.foodies.mealplanner.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.repository.EmailRepository;
import com.foodies.mealplanner.util.CommonUtils;
import com.foodies.mealplanner.util.EmailComposingUtil;

import java.time.LocalDate;

/**
 * Admin Profile fragment
 *
 *
 * @author herje
 * @version 1
 */
public class AdminProfileFragment extends Fragment {

    public static final String TAG = AdminProfileFragment.class.getName();
    public static final String EMAIL_ALERT = "Email Alert";
    public static final String DELIMITER = ";";
    private final EmailRepository emailRepo = new EmailRepository();
    private final CommonUtils commonUtils = new CommonUtils();
    private final EmailComposingUtil emailComposingUtil = new EmailComposingUtil();
    private View adminProfileFragmentView;
    private Button usersButton, mealsButton, menusButton, emailButton;

    public static AdminProfileFragment newInstance() {
        return new AdminProfileFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        adminProfileFragmentView = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        //Current config to send the email (date today) for demo purposes.
        LocalDate localDate = LocalDate.now();
        String paramDate = commonUtils.dateFormatter(localDate);

        //Will check in the system if there is a pending email to be sent.
        emailRepo.getEmail(email -> {

            if (email != null) {

                //Builder design pattern
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(EMAIL_ALERT);

                StringBuilder recipients = new StringBuilder();
                for (String emailAdd : email.getMealPlanWeek().getEmailAddressList()) {
                    recipients.append(emailAdd + DELIMITER);
                }

                builder.setMessage("Recipients: " + recipients.toString() +
                        "\n\nMonday Menu: " + email.getMealPlanWeek().getMondayMenu().getMenuName() +
                        "\nWednesday Menu: " + email.getMealPlanWeek().getWednesdayMenu().getMenuName() +
                        "\nFriday Menu: " + email.getMealPlanWeek().getFridayMenu().getMenuName());

                builder.setPositiveButton(R.string.sendEmail, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        emailComposingUtil.sendEmail(getContext(), email.getMealPlanWeek());
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
        }, paramDate);

        //User fragments
        usersButton = adminProfileFragmentView.findViewById(R.id.usersBtn);

        //Observer Design Pattern
        usersButton.setOnClickListener((adminProfileFragmentView) -> {
            UsersListFragment userListFrag = new UsersListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(UsersListFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, userListFrag);

            transaction.commit();
        });

        //Meals fragments
        mealsButton = adminProfileFragmentView.findViewById(R.id.mealsBtn);

        //Observer Design Pattern
        mealsButton.setOnClickListener((adminProfileFragmentView) -> {
            MealListFragment mealListFragment = new MealListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(AdminProfileFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, mealListFragment);

            transaction.commit();

        });

        //Menus fragments
        menusButton = adminProfileFragmentView.findViewById(R.id.menuBtn);

        //Observer Design Pattern
        menusButton.setOnClickListener((adminProfileFragmentView) -> {

            MenuListFragment menuListFragment = new MenuListFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, menuListFragment);

            transaction.commit();
        });

        //Emails fragments
        emailButton = adminProfileFragmentView.findViewById(R.id.emailBtn);

        //Observer Design Pattern
        emailButton.setOnClickListener((adminProfileFragmentView) -> {

            EmailMenuFragment emailMenuFragment = new EmailMenuFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(MenuListFragment.TAG);
            transaction.replace(R.id.adminProfileFrame, emailMenuFragment);

            transaction.commit();
        });

        return adminProfileFragmentView;
    }

}
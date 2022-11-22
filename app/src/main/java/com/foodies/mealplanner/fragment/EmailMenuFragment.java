package com.foodies.mealplanner.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.util.AppUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for sending email.
 */
public class EmailMenuFragment extends Fragment {

    private View emailMenuFragment;
    private TextView mondayTxt, wednesdayTxt, fridayTxt;
    private final AppUtils appUtils = new AppUtils();
    private ListView menuListView;
    private final UserRepository userDb = new UserRepository();
    private MenuRepository menuDb = new MenuRepository();
    private StringBuilder emailAddress = new StringBuilder();
    private EditText emailEditTxt;
    private List<String> menuNameList = new ArrayList<>();


    public EmailMenuFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        emailMenuFragment = inflater.inflate(R.layout.fragment_email_menu, container, false);
        mondayTxt = emailMenuFragment.findViewById(R.id.mondayTxtView);
        wednesdayTxt = emailMenuFragment.findViewById(R.id.wednesdayTxtView);
        fridayTxt = emailMenuFragment.findViewById(R.id.fridayTxtView);
        menuListView = emailMenuFragment.findViewById(R.id.emailMenuListView);
        emailEditTxt = emailMenuFragment.findViewById(R.id.emailEditText);

        LocalDate monday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate wednesday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        LocalDate friday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        String mondayFormat = appUtils.dateFormatter(monday);
        String wednesdayFormat = appUtils.dateFormatter(wednesday);
        String fridayFormat = appUtils.dateFormatter(friday);

        mondayTxt.setText(mondayFormat);
        wednesdayTxt.setText(wednesdayFormat);
        fridayTxt.setText(fridayFormat);

        userDb.getAllUsers(userList -> {
            for (User user : userList) {

                emailAddress.append(user.getEmail());
                emailAddress.append(";");
                Log.d("USERS EMAIL", emailAddress.toString());
            }

            //Lock the edit text but scrollable
            emailEditTxt.setText(emailAddress);
            emailEditTxt.setEnabled(true);
            emailEditTxt.setKeyListener(null);
            emailEditTxt.setCursorVisible(false);
            emailEditTxt.setFocusable(false);

        });

        menuDb.getAllMenus(menuList ->{

            Log.d("MENU LIST FRAG", "SIZE: " + menuList.size());
            menuNameList = new ArrayList();
            for (Menu menu : menuList) {
                menuNameList.add(menu.getMenuName());

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.menu_listview, R.id.menuView, menuNameList);
            menuListView.setAdapter(adapter);

        });



        return emailMenuFragment;
    }
}
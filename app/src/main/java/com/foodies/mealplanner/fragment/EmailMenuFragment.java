package com.foodies.mealplanner.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.util.AppUtils;
import com.foodies.mealplanner.util.EmailUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

/**
 * Fragment for sending email.
 */
public class EmailMenuFragment extends Fragment {

    public static final String PICK_A_MENU = "Pick a menu";
    private final AppUtils appUtils = new AppUtils();
    private final UserRepository userDb = new UserRepository();
    private final MenuRepository menuDb = new MenuRepository();
    private final StringBuilder emailAddress = new StringBuilder();
    private View emailMenuFragment;
    private TextView mondayTxt, wednesdayTxt, fridayTxt;
    private EditText emailEditTxt, mondayMenuTxt, wednesdayMenuTxt, fridayMenuTxt, notesTxt;
    private ArrayList<String> menuNameList = new ArrayList<>();
    private Button mondayBtn, wednesdayBtn, fridayBtn, sendBtn;


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
        emailEditTxt = emailMenuFragment.findViewById(R.id.emailEditText);
        mondayBtn = emailMenuFragment.findViewById(R.id.mondayBtn);
        wednesdayBtn = emailMenuFragment.findViewById(R.id.wednesdayBtn);
        fridayBtn = emailMenuFragment.findViewById(R.id.fridayBtn);
        mondayMenuTxt = emailMenuFragment.findViewById(R.id.mondayMenuTxt);
        wednesdayMenuTxt = emailMenuFragment.findViewById(R.id.wednesdayMenuTxt);
        fridayMenuTxt = emailMenuFragment.findViewById(R.id.fridayMenuTxt);
        notesTxt = emailMenuFragment.findViewById(R.id.notesTxt);
        sendBtn = emailMenuFragment.findViewById(R.id.sendEmailButton);

        LocalDate monday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate wednesday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        LocalDate friday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        String mondayFormat = appUtils.dateFormatter(monday);
        String wednesdayFormat = appUtils.dateFormatter(wednesday);
        String fridayFormat = appUtils.dateFormatter(friday);

        mondayTxt.setText(mondayFormat);
        wednesdayTxt.setText(wednesdayFormat);
        fridayTxt.setText(fridayFormat);

        userDb.getAllUserCustomerActive(userList -> {
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

        menuDb.getAllMenus(menuList -> {

            Log.d("MENU LIST FRAG", "SIZE: " + menuList.size());
            menuNameList = new ArrayList();
            for (Menu menu : menuList) {
                menuNameList.add(menu.getMenuName());
            }


            mondayBtn.setOnClickListener((emailMenuFragmentMon) -> {

                alertDialog(mondayMenuTxt);
            });

            wednesdayBtn.setOnClickListener((emailMenuFragmentWed) -> {

                alertDialog(wednesdayMenuTxt);

            });

            fridayBtn.setOnClickListener((emailMenuFragmentFri) -> {

                alertDialog(fridayMenuTxt);

            });
        });

        sendBtn.setOnClickListener((emailMenuFragment) ->{

            sendEmail(getContext());
        });

        return emailMenuFragment;
    }

    /**
     * Set alert dialog to show the menu to choose from
     * @param editText
     */
    private void alertDialog(EditText editText) {

        //Set alert dialog custom textview
        TextView textView = new TextView(getContext());
        textView.setText(PICK_A_MENU);
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(Color.LTGRAY);
        textView.setTextColor(Color.WHITE);

        new AlertDialog.Builder(getContext())
                .setItems(menuNameList.toArray(new String[menuNameList.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        editText.setText(menuNameList.get(i));
                    }
                }).setCustomTitle(textView).show();
    }


    private void sendEmail(Context context) {
        String email = "jbhermo@yahoo.com";
        String subject = "Test123";
        String message = "Success!";
        EmailUtil sm = new EmailUtil(context, email, subject, message);
        Log.d(">>>>>>>>>>>>", "TEST EMAIL: " + email);
        sm.execute();
    }
}
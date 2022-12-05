package com.foodies.mealplanner.fragment;

import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.foodies.mealplanner.R;
import com.foodies.mealplanner.model.Email;
import com.foodies.mealplanner.model.MealPlanWeek;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.model.User;
import com.foodies.mealplanner.repository.EmailRepository;
import com.foodies.mealplanner.repository.MenuRepository;
import com.foodies.mealplanner.repository.UserRepository;
import com.foodies.mealplanner.util.AppUtils;
import com.foodies.mealplanner.util.EmailUtil;
import com.foodies.mealplanner.validations.FieldValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment for sending email.
 */
public class EmailMenuFragment extends Fragment {

    public static final String PICK_A_MENU = "Pick a menu";
    private static final String REQUIRED_ERROR = "Required";
    private final AppUtils appUtils = new AppUtils();
    private final UserRepository userDb = new UserRepository();
    private final MenuRepository menuDb = new MenuRepository();
    private final HashMap<String, Menu> menuMap = new HashMap<>();
    private final FieldValidator fieldValidator = new FieldValidator();
    private final EmailUtil emailUtil = new EmailUtil();
    private View emailMenuFragment;
    private TextView mondayTxt, wednesdayTxt, fridayTxt;
    private EditText emailEditTxt, mondayMenuTxt, wednesdayMenuTxt, fridayMenuTxt, notesTxt;
    private ArrayList<String> menuNameList = new ArrayList<>();
    private Button mondayBtn, wednesdayBtn, fridayBtn, sendBtn, saveBtn;
    private Menu mondayMenu, wednesdayMenu, fridayMenu;
    private final EmailRepository emailRepository = new EmailRepository();


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
        saveBtn = emailMenuFragment.findViewById(R.id.saveEmailButton);

        List<String> emailAddressList = new ArrayList<>();

        //get all users
        userDb.getAllUserCustomerActive(userList -> {
            for (User user : userList) {
                emailAddressList.add(user.getEmail());

            }
            Log.d("USERS EMAIL", "SIZE: " + emailAddressList.size());

            //Lock the edit text but scrollable
            if (emailAddressList.size() == 0) {
                emailEditTxt.setText("No emails available");
            } else {
                emailEditTxt.setText(emailAddressList.toString().replaceAll("[\\[\\]]", ""));
            }

            emailEditTxt.setEnabled(true);
            emailEditTxt.setKeyListener(null);
            emailEditTxt.setCursorVisible(false);
            emailEditTxt.setFocusable(false);

        });

        //get all menus
        menuDb.getAllMenus(menuList -> {

            Log.d("MENU LIST FRAG", "SIZE: " + menuList.size());
            menuNameList = new ArrayList();

            for (Menu menu : menuList) {
                menuNameList.add(menu.getMenuName());
                menuMap.put(menu.getMenuName(), menu);
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

        sendBtn.setOnClickListener((emailMenuFragment) -> {
            if (checkAllFields()) {

                MealPlanWeek mealPlanWeek = getMealPlanWeek(emailAddressList);

                //Build Email and send
                emailUtil.sendEmail(getContext(), mealPlanWeek);
                saveEmail(mealPlanWeek, true);

                getParentFragmentManager().popBackStackImmediate();
            }
        });

        saveBtn.setOnClickListener((emailMenuFragment) -> {
            if (checkAllFields()) {
                saveEmail(getMealPlanWeek(emailAddressList), false);
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        return emailMenuFragment;
    }

    /**
     * Compose the meal plan for the week
     *
     * @param emailAddressList - email address to be sent.
     * @return mealPlanWeek - composed meal plan
     */
    @NonNull
    private MealPlanWeek getMealPlanWeek(List<String> emailAddressList) {
        mondayMenu = menuMap.get(mondayMenuTxt.getText().toString());
        wednesdayMenu = menuMap.get(wednesdayMenuTxt.getText().toString());
        fridayMenu = menuMap.get(fridayMenuTxt.getText().toString());
        MealPlanWeek mealPlanWeek = new MealPlanWeek();
        mealPlanWeek.setEmailAddressList(emailAddressList);
        mealPlanWeek.setMondayMenu(mondayMenu);
        mealPlanWeek.setWednesdayMenu(wednesdayMenu);
        mealPlanWeek.setFridayMenu(fridayMenu);
        mealPlanWeek.setNotes(notesTxt.getText().toString());
        return mealPlanWeek;
    }

    /**
     * Set alert dialog to show the menu to choose from
     *
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


    /**
     * Saved the email and to be sent on a later date
     *
     * @param mealPlan - composed meal plan for the week
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveEmail(MealPlanWeek mealPlan, Boolean isSent) {
//
//        LocalDate saturday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
//        String saturdayDate = appUtils.dateFormatter(saturday);

        LocalDate saturday = LocalDate.now();
        String saturdayDate = appUtils.dateFormatter(saturday);

        Email email = new Email();
        email.setMealPlanWeek(mealPlan);
        email.setDeliveryDate(saturdayDate);
        email.setSent(isSent);

        emailRepository.saveEmail(email, getActivity());

    }

    /**
     * Check all required fields if value is present
     * Check also if inputs are valid
     *
     * @return boolean, true if all valid
     */
    private boolean checkAllFields() {

        boolean allValid = true;
        errorReset();

        if (fieldValidator.validateFieldIfEmpty(mondayMenuTxt.length())) {
            mondayMenuTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(wednesdayMenuTxt.length())) {
            wednesdayMenuTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        if (fieldValidator.validateFieldIfEmpty(fridayMenuTxt.length())) {
            fridayMenuTxt.setError(REQUIRED_ERROR);
            allValid = false;
        }

        return allValid;
    }

    /**
     * Reset error messages on field
     */
    private void errorReset() {
        mondayMenuTxt.setError(null);
        wednesdayMenuTxt.setError(null);
        fridayMenuTxt.setError(null);
    }

}
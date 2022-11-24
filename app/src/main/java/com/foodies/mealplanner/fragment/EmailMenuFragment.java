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
import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.MealPlanWeek;
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
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Fragment for sending email.
 */
public class EmailMenuFragment extends Fragment {

    public static final String PICK_A_MENU = "Pick a menu";
    public static final String MEAL_PLANNER_PLAN_OF_THE_WEEK = "Meal Planner plan of the week";
    private final AppUtils appUtils = new AppUtils();
    private final UserRepository userDb = new UserRepository();
    private final MenuRepository menuDb = new MenuRepository();
    private final HashMap<String, Menu> menuMap = new HashMap<>();
    private View emailMenuFragment;
    private TextView mondayTxt, wednesdayTxt, fridayTxt;
    private EditText emailEditTxt, mondayMenuTxt, wednesdayMenuTxt, fridayMenuTxt, notesTxt;
    private ArrayList<String> menuNameList = new ArrayList<>();
    private Button mondayBtn, wednesdayBtn, fridayBtn, sendBtn;
    private Menu mondayMenu, wednesdayMenu, fridayMenu;
    private String mondayDate, wednesdayDate, fridayDate;


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

        //Set the next dates. This email should be sent on a saturday or sunday to get the correct date.
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate wednesday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        LocalDate friday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        mondayDate = appUtils.dateFormatter(monday);
        wednesdayDate = appUtils.dateFormatter(wednesday);
        fridayDate = appUtils.dateFormatter(friday);

        mondayTxt.setText(mondayDate);
        wednesdayTxt.setText(wednesdayDate);
        fridayTxt.setText(fridayDate);
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

            mondayMenu = menuMap.get(mondayMenuTxt.getText().toString());
            wednesdayMenu = menuMap.get(wednesdayMenuTxt.getText().toString());
            fridayMenu = menuMap.get(fridayMenuTxt.getText().toString());
            MealPlanWeek mealPlanWeek = new MealPlanWeek();
            mealPlanWeek.setEmailAddressList(emailAddressList);
            mealPlanWeek.setMondayMenu(mondayMenu);
            mealPlanWeek.setWednesdayMenu(wednesdayMenu);
            mealPlanWeek.setFridayMenu(fridayMenu);
            mealPlanWeek.setNotes(notesTxt.getText().toString());

            //Send email
            sendEmail(getContext(), mealPlanWeek);

            getParentFragmentManager().popBackStackImmediate();
        });

        return emailMenuFragment;
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
     * Sends the email
     *
     * @param context
     * @param mealPlan
     */
    private void sendEmail(Context context, MealPlanWeek mealPlan) {

        List<String> emailList = new ArrayList<>();
        emailList.addAll(mealPlan.getEmailAddressList());

        //Compose the message
        String composedMessage = composeMessageHTML(mealPlan);

        //add all the email address
        InternetAddress[] emailAdd = new InternetAddress[emailList.size()];

        for (int i = 0; i < emailList.size(); i++) {
            try {
                emailAdd[i] = new InternetAddress(emailList.get(i));
            } catch (AddressException e) {
                Log.e("Email address list error ", e.toString());
            }
        }
        String subject = MEAL_PLANNER_PLAN_OF_THE_WEEK;
        String message = composedMessage;
        EmailUtil emailUtil = new EmailUtil(context, emailAdd, subject, message);
        emailUtil.execute();
    }

    /**
     * Compose message html style
     *
     * @param mealPLan
     * @return
     */
    private String composeMessageHTML(MealPlanWeek mealPLan) {
        StringBuilder messageFormat = new StringBuilder();

        messageFormat.append("<h2>Hello MealPlanners! This is the menu for the week:</h2>");
        messageFormat.append(buildMealMessage(mealPLan.getMondayMenu(), "Monday", mondayDate));
        messageFormat.append(buildMealMessage(mealPLan.getWednesdayMenu(), "Wednesday", wednesdayDate));
        messageFormat.append(buildMealMessage(mealPLan.getFridayMenu(), "Friday", fridayDate));
        messageFormat.append(mealPLan.getNotes());
        messageFormat.append(" Thank you!<br><br>" + "Regards,<br><br>" + "Meal Planner Team");
        return messageFormat.toString();
    }

    /**
     * Compose meals of message body
     *
     * @param menu
     * @param day
     * @param date
     * @return
     */
    private StringBuilder buildMealMessage(Menu menu, String day, String date) {
        StringBuilder messageFormat = new StringBuilder();

        messageFormat.append("<h4 style= \"color: green;\"> <b>" + day + "(" + date + "): "
                + menu.getMenuName() + "</h4></b>");
        messageFormat.append(buildMealMessage(menu.getMeatMeal()));
        messageFormat.append(buildMealMessage(menu.getVegetableMeal()));
        messageFormat.append(buildMealMessage(menu.getBothMeal()));

        return messageFormat;
    }

    /**
     * Compose meal details of message body
     *
     * @param meal
     * @return
     */
    private StringBuilder buildMealMessage(Meal meal) {

        StringBuilder messageFormat = new StringBuilder();
        messageFormat.append("<b>&nbsp&nbsp&nbsp&nbsp" + meal.getMealType() + ": "
                + meal.getMealName() + "</b>");
        messageFormat.append("<br><p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp Ingredients: "
                + meal.getMealIngredients() + "</p>");
        messageFormat.append("<p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  Description: "
                + meal.getMealDescription() + "</p>");
        messageFormat.append("<p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  Price: CAD "
                + meal.getMealPrice() + "</p>" + "<br>");

        return messageFormat;
    }
}
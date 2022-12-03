package com.foodies.mealplanner.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.foodies.mealplanner.model.Meal;
import com.foodies.mealplanner.model.MealPlanWeek;
import com.foodies.mealplanner.model.Menu;
import com.foodies.mealplanner.repository.EmailRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailUtil {

    public static final String MEAL_PLANNER_PLAN_OF_THE_WEEK = "Meal Planner plan of the week";
    private String mondayDate, wednesdayDate, fridayDate;
    private AppUtils appUtils = new AppUtils();
    private EmailRepository emailRepository = new EmailRepository();

    /**
     * Sends the email
     *
     * @param context
     * @param mealPlan
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendEmail(Context context, MealPlanWeek mealPlan) {

        List<String> emailList = new ArrayList<>();
        emailList.addAll(mealPlan.getEmailAddressList());

        //Compose the message
        String composedMessage = composeEmailBody(mealPlan);

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
        EmailSendingUtil emailSendingUtil = new EmailSendingUtil(context, emailAdd, subject, message);

        try {
            emailSendingUtil.execute();
        }catch(Exception e){
            Log.e("Email Util", "Error " + e.toString());
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public String composeEmailBody(MealPlanWeek mealPlan) {

        //Set the next dates. This email should be sent on a saturday or sunday to get the correct date.
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate wednesday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        LocalDate friday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        mondayDate = appUtils.dateFormatter(monday);
        wednesdayDate = appUtils.dateFormatter(wednesday);
        fridayDate = appUtils.dateFormatter(friday);
        String composedMessage = composeMessageHTML(mealPlan);
        return composedMessage;
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
        messageFormat.append(buildMenuMessage(menu.getMeatMeal()));
        messageFormat.append(buildMenuMessage(menu.getVegetableMeal()));
        messageFormat.append(buildMenuMessage(menu.getBothMeal()));

        return messageFormat;
    }

    /**
     * Compose menu details of message body
     *
     * @param meal
     * @return
     */
    private StringBuilder buildMenuMessage(Meal meal) {

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

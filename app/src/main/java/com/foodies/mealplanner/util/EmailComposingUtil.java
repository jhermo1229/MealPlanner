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

/**
 * Email utility for composing the message.
 * @author herje
 * @version 1
 */
public class EmailComposingUtil {

    public static final String MEAL_PLANNER_PLAN_OF_THE_WEEK = "Meal Planner plan of the week";
    private String mondayDate, wednesdayDate, fridayDate;
    private AppUtils appUtils = new AppUtils();

    /**
     * Sends the email
     *
     * @param context - current context of the view.
     * @param mealPlan - meal plan for the week.
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


    /**
     * Composing the email body
     * @param mealPlan - meal plan for the week
     * @return the composed message
     */
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
     * Composing the email body
     * @param mealPLan - meal plan for the week
     * @return the composed message
     */
    private String composeMessageHTML(MealPlanWeek mealPLan) {
        StringBuilder messageFormat = new StringBuilder();

        messageFormat.append("<h2>Hello MealPlanners! This is the menu for the week:</h2>");
        messageFormat.append(buildMealMessage(mealPLan.getMondayMenu(), "Monday", mondayDate));
        messageFormat.append(buildMealMessage(mealPLan.getWednesdayMenu(), "Wednesday", wednesdayDate));
        messageFormat.append(buildMealMessage(mealPLan.getFridayMenu(), "Friday", fridayDate));
        messageFormat.append(mealPLan.getNotes() + "<br><br>");
        messageFormat.append(" Thank you!<br><br>" + "Regards,<br><br>" + "Meal Planner Team");
        return messageFormat.toString();
    }

    /**
     * Composing the email body
     * @param menu - menu chosen.
     * @param day - day of the menu.
     * @param date - date of the current day (Month day, Year) format
     * @return the composed message
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
     * Composing the email body
     * @param meal - meal inside the menu.
     * @return the composed message
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

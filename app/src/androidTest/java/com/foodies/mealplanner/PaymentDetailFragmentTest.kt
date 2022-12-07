package com.foodies.mealplanner

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.foodies.mealplanner.activity.MainActivity
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class PaymentDetailFragmentTest {

    /**
     * Initialized activity
     */
    private fun init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Perform click on the button

        onView(ViewMatchers.withId(R.id.signupBtn)).perform(ViewActions.click())
        initFields()

    }

    /**
     * Initialized some fields
     */
    private fun initFields() {

        onView(ViewMatchers.withId(R.id.firstName))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("John"))
        onView(ViewMatchers.withId(R.id.lastName))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("Test"))
        onView(ViewMatchers.withId(R.id.houseNumber))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("111"))
        onView(ViewMatchers.withId(R.id.street))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("United"))
        onView(ViewMatchers.withId(R.id.city))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("Waterloo"))
        onView(ViewMatchers.withId(R.id.postalCode))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("N2J876"))
        onView(ViewMatchers.withId(R.id.email))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("test@yahoo.com"))
        onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("aaa"))
        onView(ViewMatchers.withId(R.id.personalNextButton)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.scrollTo(),
            ViewActions.click())
    }
}
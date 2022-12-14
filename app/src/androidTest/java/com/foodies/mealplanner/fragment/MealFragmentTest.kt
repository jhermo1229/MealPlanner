package com.foodies.mealplanner.fragment

import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.foodies.mealplanner.R
import com.foodies.mealplanner.activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Automated UI test for meal functions in admin profile page
 * @author herje
 * @version 1
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class MealFragmentTest {

    /**
     * Initialized activity
     */
    private fun init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Perform click on the button
        onView(withId(R.id.loginBtn)).perform(ViewActions.click())

        onView(withId(R.id.emailLogin)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.typeText("admin@gmail.com")
        )

        onView(withId(R.id.passwordLogin)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.typeText("admin")
        )

        onView(withId(R.id.loginUserProfileBtn)).perform(
            ViewActions.closeSoftKeyboard(), ViewActions.click())

        //let it load before starting the test
        SystemClock.sleep(1000)

        onView(withId(R.id.mealsBtn)).perform(ViewActions.click())

    }

    /**
     * Test if the fragment shown is the user list page
     */
    @Test
    fun test_navigation_login_admin_meals_list(){

        init()
        onView(withId(R.id.mealsFragment)).check(matches(isDisplayed()))
    }
}
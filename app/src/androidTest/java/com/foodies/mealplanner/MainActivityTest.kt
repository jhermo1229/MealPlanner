package com.foodies.mealplanner

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.foodies.mealplanner.activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {


    @Test
    fun test_isActivityLaunched() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Test to see if main activity is displayed
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_logo_signupAndLoginButton() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Test if logo is visible
        onView(withId(R.id.mainLogo)).check(matches(isDisplayed()))

        //Test if signup button is visible
        onView(withId(R.id.signupBtn)).check(matches(isDisplayed()))

        //Test if login button is visible
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navigate_signupActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Perform click on the button
        onView(withId(R.id.signupBtn)).perform(click());

        //Check if signup activity was launched
        onView(withId(R.id.signup)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navigate_loginActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Perform click on the button
        onView(withId(R.id.loginBtn)).perform(click());

        //Check if signup activity was launched
        onView(withId(R.id.login)).check(matches(isDisplayed()))
    }
}
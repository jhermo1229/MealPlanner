package com.foodies.mealplanner

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.foodies.mealplanner.activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class PersonalDetailFragmentTest {


    /**
     * Initialized activity
     */
    private fun init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Perform click on the button
        onView(withId(R.id.signupBtn)).perform(ViewActions.click())
    }


    @Test
    fun test_isPersonalDetailsFragmentLaunched() {

        init()
        //Test if fragment is launched
        onView(withId(R.id.personalDetailsFrag)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isFieldVisible() {

        init()
        //Test all fields is displayed
        onView(withId(R.id.firstName)).check(matches(isDisplayed()))
        onView(withId(R.id.lastName)).check(matches(isDisplayed()))
        onView(withId(R.id.houseNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.street)).check(matches(isDisplayed()))
        onView(withId(R.id.city)).check(matches(isDisplayed()))
        onView(withId(R.id.province_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.postalCode)).check(matches(isDisplayed()))
        onView(withId(R.id.phoneNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.loginCredentialsTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.email)).check(matches(isDisplayed()))
        onView(withId(R.id.password)).check(matches(isDisplayed()))

    }
}
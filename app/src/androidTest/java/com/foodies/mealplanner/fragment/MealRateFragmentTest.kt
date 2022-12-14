package com.foodies.mealplanner.fragment

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.foodies.mealplanner.R
import com.foodies.mealplanner.activity.MainActivity
import org.junit.Test

//@RunWith(AndroidJUnit4ClassRunner::class)
class MealRateFragmentTest {

    /**
     * Initialized activity
     */
    private fun init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)


        //Perform click on the button
        onView(withId(R.id.signupBtn)).perform(ViewActions.click())
//        onView(withId(R.id.firstNameEditText)).perform(ViewActions.typeText("TestName"))
//        onView(withId(R.id.lastNameEditText)).perform(ViewActions.typeText("TestLastName"))
        onView(withId(R.id.personalNextButton)).perform(ViewActions.click())
    }


//    @Test
    fun test_isPersonalDetailsFragmentLaunched() {

        init()
        //Test if fragment is launched/Navigation Test
        onView(withId(R.id.mealDeliveryRate)).check(matches(isDisplayed()))
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
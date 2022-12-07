package com.foodies.mealplanner

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.foodies.mealplanner.activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Espresso testing for the personal fragment
 * @author herje
 * @version 1
 */
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

    /**
     * Initialized some fields
     */
    private fun initFields() {

        onView(withId(R.id.firstName)).perform(ViewActions.closeSoftKeyboard(),ViewActions.typeText("John"))
        onView(withId(R.id.lastName)).perform(ViewActions.closeSoftKeyboard(),ViewActions.typeText("Test"))
        onView(withId(R.id.houseNumber)).perform(ViewActions.closeSoftKeyboard(),ViewActions.typeText("111"))
        onView(withId(R.id.street)).perform(ViewActions.closeSoftKeyboard(),ViewActions.typeText("United"))
        onView(withId(R.id.city)).perform(ViewActions.closeSoftKeyboard(),ViewActions.typeText("Waterloo"))
        onView(withId(R.id.postalCode)).perform(ViewActions.closeSoftKeyboard(),ViewActions.typeText("N2J876"))
    }


    /**
     * Test if fragment is launched/Navigation Test
     */
    @Test
    fun test_isPersonalDetailsFragmentLaunched() {

        init()
        onView(withId(R.id.personalDetailsFrag)).check(matches(isDisplayed()))
    }


    /**
     * Test all fields is displayed
     */
    @Test
    fun test_isFieldVisible() {

        init()
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

    /**
     * Test if required fields will show error when blank
     */
    @Test
    fun test_should_show_error(){
        init()
        onView(withId(R.id.personalNextButton)).perform(ViewActions.scrollTo(), ViewActions.click())
        onView(withId(R.id.firstName)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.lastName)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.houseNumber)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.street)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.city)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.postalCode)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.phoneNumber)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.email)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.password)).check(matches(hasErrorText("Required")))
    }

    /**
     * Test input in phone number field. The letters should not  be accepted. Only numeric will be accepted.
     */
    @Test
    fun test_if_phone_number_accepts_numeric_only(){
        init()

        //Type letter with numbers. Should only accept numbers.
        onView(withId(R.id.phoneNumber)).perform(ViewActions.typeText("AAA111")).check(matches(
            withText("111")))
    }

    /**
     * Test input in phone number field. Should throw an error if less than 10 digits is inputted.
     */
    @Test
    fun test_phone_number_length_less_than_10() {
        init()
        initFields()
        onView(withId(R.id.phoneNumber)).perform(ViewActions.typeText("12345678"))
        onView(withId(R.id.email)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(), ViewActions.typeText("admin@gmail.com"))
        onView(withId(R.id.password)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(),  ViewActions.typeText("aaaa"))
        onView(withId(R.id.personalNextButton)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(),  ViewActions.click())
        onView(withId(R.id.phoneNumber)).check(matches(hasErrorText("Invalid Length")))

        //Test if field will only accept 10 even if user try to input more than 10
        onView(withId(R.id.phoneNumber)).perform(ViewActions.typeText("12345678901112")).check(matches(
            withText("1234567890")))
        onView(withId(R.id.personalNextButton)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(),  ViewActions.click())
    }

    /**
     * Test input in phone number field. Should only accept 10 digits.
     */
    @Test
    fun test_phone_number_length_more_than_10() {
        init()
        initFields()
        //Test if field will only accept 10 even if user try to input more than 10
        onView(withId(R.id.phoneNumber)).perform(ViewActions.typeText("12345678901112")).check(matches(
            withText("1234567890")))

        onView(withId(R.id.email)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(), ViewActions.typeText("admin@gmail.com"))
        onView(withId(R.id.password)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(),  ViewActions.typeText("aaaa"))

        onView(withId(R.id.personalNextButton)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(),  ViewActions.click())
    }

    /**
     * Test input in email field. If email is already existing should throw an error
     */
    @Test
    fun test_email_format_incorrect() {
        init()
        initFields()
        onView(withId(R.id.phoneNumber)).perform(ViewActions.typeText("1234567890"))

        onView(withId(R.id.email)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(), ViewActions.typeText("admin@toronto"))
        onView(withId(R.id.password)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(),  ViewActions.typeText("aaaa"))
        onView(withId(R.id.personalNextButton)).perform(ViewActions.closeSoftKeyboard(), ViewActions.scrollTo(),  ViewActions.click())
        onView(withId(R.id.email)).check(matches(hasErrorText("Incorrect Email Format")))
    }
}
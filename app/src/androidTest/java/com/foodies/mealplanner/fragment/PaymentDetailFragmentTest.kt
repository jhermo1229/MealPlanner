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
 * Autoomated UI testing for payment screen
 * @author herje
 * @version 1
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PaymentDetailFragmentTest {

    /**
     * Initialized activity
     */
    private fun init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Perform click on the button

        onView(withId(R.id.signupBtn)).perform(ViewActions.click())
        initFields()

    }

    /**
     * Initialized some fields
     */
    private fun initFields() {

        onView(withId(R.id.firstName))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("John"))
        onView(withId(R.id.lastName))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("Test"))
        onView(withId(R.id.houseNumber))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("111"))
        onView(withId(R.id.street))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("United"))
        onView(withId(R.id.city))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("Waterloo"))
        onView(withId(R.id.postalCode))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("N2J876"))
        onView(withId(R.id.phoneNumber))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("1111111111"))
        onView(withId(R.id.email))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("test@yahoo.com"))
        onView(withId(R.id.password))
            .perform(ViewActions.closeSoftKeyboard(), ViewActions.typeText("aaa"))
        onView(withId(R.id.personalNextButton)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.scrollTo(),
            ViewActions.click()
        )
        //let it load before starting the test
        SystemClock.sleep(1000)
    }

    /**
     * Test if the correct fragment is displayed
     */
    @Test
    fun test_navigation_payment_fragment() {
        init()
        onView(withId(R.id.paymentFragment)).check(matches(isDisplayed()))
    }

    /**
     * Test if required error message is shown
     */
    @Test
    fun test_should_show_error() {
        init()
        onView(withId(R.id.saveButton)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click())

        onView(withId(R.id.nameOnCard)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.cardNumber)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.expiryDate)).check(matches(hasErrorText("Required")))
        onView(withId(R.id.securityCode)).check(matches(hasErrorText("Required")))
    }

    /**
     * Test input in phone number field. The letters should not  be accepted. Only numeric will be accepted.
     */
    @Test
    fun test_if_card_number_accepts_numeric_only() {
        init()
        onView(withId(R.id.saveButton)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click())
        //Type letter with numbers. Should only accept numbers.
        onView(withId(R.id.cardNumber)).perform(ViewActions.typeText("AAA111")).check(
            matches(
                withText("111")
            )
        )
    }
}
package com.foodies.mealplanner.fragment

import android.os.SystemClock
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.foodies.mealplanner.R
import com.foodies.mealplanner.activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Automated UI test for customer profile page
 * @author herje
 * @version 1
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class CustomerProfileFragmentTest {

    /**
     * Initialized activity
     */
    private fun init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Perform click on the button
        onView(withId(R.id.loginBtn)).perform(ViewActions.click())

        onView(withId(R.id.emailLogin)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.typeText("jbhermo@yahoo.com")
        )

        onView(withId(R.id.passwordLogin)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.typeText("jj")
        )

        onView(withId(R.id.loginUserProfileBtn)).perform(
            ViewActions.closeSoftKeyboard(), ViewActions.click())

        //let it load before starting the test
        SystemClock.sleep(1000)

    }

    /**
     * Test if the fragment shown is the customer page
     */
    @Test
    fun test_navigation_login_customer(){

        init()
        onView(withId(R.id.customer_profile)).check(matches(isDisplayed()))
    }

    /**
     * Test buttons on the fragment if showing update page
     */
    @Test
    fun test_navigation_login_customer_update_buttons(){

        init()

        //update personal details test
        onView(withId(R.id.updatePersonal)).perform(
            ViewActions.closeSoftKeyboard(), ViewActions.click())
        onView(withId(R.id.updatePersonalDialog)).check(matches(isDisplayed()))

        //Access dialog box and click the cancel button
        onView(withText("Cancel"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(ViewActions.click());

        //update password test
        onView(withId(R.id.updateLoginDetails)).perform(
            ViewActions.closeSoftKeyboard(), ViewActions.click())
        onView(withId(R.id.updateLoginDialog)).check(matches(isDisplayed()))

        //Access dialog box and click the cancel button
        onView(withText("Cancel"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(ViewActions.click());

        //update payment test
        onView(withId(R.id.updatePaymentDetails)).perform(ViewActions.scrollTo(),
            ViewActions.closeSoftKeyboard(), ViewActions.click())
        onView(withId(R.id.updatePaymentDialog)).check(matches(isDisplayed()))
    }

}

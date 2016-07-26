package com.dtlim.bantaystocks;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dtlim.bantaystocks.home.view.HomeActivity;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by dale on 7/11/16.
 */
@RunWith(AndroidJUnit4.class)
public class HomeActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<HomeActivity> activityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void validateToolbarTitle() {
        onView(allOf(withText("BantayStocks"), withParent(withId(R.id.bantaystocks_toolbar)))).check(matches(isDisplayed()));
    }

    @Test
    public void validateShowNoSubscribedView() {
        onView(withId(R.id.bantaystocks_main_fab)).perform(click());
        onView(withText("2GO")).perform(click());
        onView(withText("TEL")).perform(click());
        onView(withText("MER")).perform(click());
        pressBack();
        onView(withId(R.id.bantaystocks_home_layout_not_subscribed)).check(matches(isDisplayed()));
    }

    @Test
    public void validateSubscribeButtonAction() {
        onView(withId(R.id.bantaystocks_main_fab)).perform(click());
        onView(withText("2GO")).perform(click());
        onView(withText("TEL")).perform(click());
        onView(withText("MER")).perform(click());
        pressBack();
        onView(withId(R.id.bantaystocks_home_button_subscribe)).perform(click());
        onView(withText("2GO")).perform(click());
        onView(withText("TEL")).perform(click());
        onView(withText("MER")).perform(click());
        pressBack();

        onView(withText("2GO")).check(matches(isDisplayed()));
        onView(withText("TEL")).check(matches(isDisplayed()));
        onView(withText("MER")).check(matches(isDisplayed()));
    }

    @Test
    public void validateFloatingActionButtonAction() {
        onView(withId(R.id.bantaystocks_main_fab)).perform(click());
        onView(withText("ABA")).perform(click());
        pressBack();
        onView(withId(R.id.bantaystocks_main_recyclerview)).perform(
                scrollTo(hasDescendant(withText("ABA"))));
        onView(withText("ABA")).check(matches(isDisplayed()));
    }

    @After
    public void afterEachTest() {
        ((BantayStocksApplication) activityTestRule.getActivity().getApplication()).initialize();
    }
}

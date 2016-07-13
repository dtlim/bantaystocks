package com.dtlim.bantaystocks;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dtlim.bantaystocks.home.view.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by dale on 7/11/16.
 */
@RunWith(AndroidJUnit4.class)
public class HomeActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<HomeActivity> activityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void validateFloatingActionButton() {
        onView(withId(R.id.bantaystocks_main_fab)).perform(click());
        onView(withText("2GO")).perform(click());
        pressBack();
        onView(withId(R.id.bantaystocks_main_recyclerview)).perform(
                scrollTo(hasDescendant(withText("2GO"))));
        onView(withText("2GO")).check(matches(isDisplayed()));
    }
}

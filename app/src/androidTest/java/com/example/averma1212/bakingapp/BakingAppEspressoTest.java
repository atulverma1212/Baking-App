package com.example.averma1212.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by HP on 25-12-2017.
 *
 * Complete UI Testing
 */

@RunWith(AndroidJUnit4.class)
public class BakingAppEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource(){
        idlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void complete_UI_testing() {
        //Check Data of RecyclerView
        onView(ViewMatchers.withId(R.id.rv_mainActivity))
                .perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(ViewMatchers.withId(R.id.rv_mainActivity))
                .perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("Brownies"))));
        onView(ViewMatchers.withId(R.id.rv_mainActivity))
                .perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(ViewMatchers.withId(R.id.rv_mainActivity))
                .perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("Cheesecake"))));

        // First, scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.rv_mainActivity))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));


        // Match the text in an item below the fold and check that it's displayed.
        onView((withId(R.id.recipe_ingredients))).check(matches(withText("Recipe Ingredients")));
        onView(ViewMatchers.withId(R.id.rv_stepsName)).perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(hasDescendant(withText("Step 1 : Recipe Introduction"))));
        onView(ViewMatchers.withId(R.id.rv_stepsName)).perform(RecyclerViewActions.scrollToPosition(1))
                .check(matches(hasDescendant(withText("Step 2 : Starting prep"))));
        onView(ViewMatchers.withId(R.id.rv_stepsName)).perform(RecyclerViewActions.scrollToPosition(2))
                .check(matches(hasDescendant(withText("Step 3 : Prep the cookie crust."))));

        //Checking stepsActivity to detailActivity UI flow
        onView(ViewMatchers.withId(R.id.rv_stepsName))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView((withId(R.id.detail_topic))).check(matches(withText("Recipe Introduction")));
        onView((withId(R.id.detail_desc))).check(matches(withText("Recipe Introduction")));



    }

    @Test
    public void checkIngredients() {

        // MAinActivity to Ingredients Fragment
        onView(ViewMatchers.withId(R.id.rv_mainActivity))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.ingredients_card_view)).perform(click());
        onView((withId(R.id.detail_topic))).check(matches(withText("Recipe Ingredients")));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null)
            Espresso.unregisterIdlingResources(idlingResource);
    }

}

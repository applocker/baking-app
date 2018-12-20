package com.dappslocker.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
    @Rule
    public final IntentsTestRule<RecipeActivity> mActivityTestRule =
            new IntentsTestRule<>(RecipeActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        // stub all external Intents
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register( mIdlingResource);
    }
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void RecyclerViewThatHoldsListOfRecipesIsDisplayed() {
        onView(ViewMatchers.withId(R.id.recycler_view_recipe)).check(matches(isDisplayed()));
    }

    @Test
    public void FirstItemofTheListHasRecpeTextDisplayed() {
        // First, scroll to the position.
        final int FIRST_ITEM = 0;
        onView(ViewMatchers.withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.scrollToPosition(FIRST_ITEM));

        // Check that the item has the special text.
        String recipePositionZero = "Nutella Pie";
        onView(withText(recipePositionZero)).check(matches(isDisplayed()));
    }

    @Test
    public void WhenAnItemIsClickedThenTheActivityIsLaunched() {
        // First, scroll to the position.
        final String RECIPE_ID = "recipe_id";
        final int FIRST_ITEM = 0;
        final int RECIPE_ID_AT_POS_0 = 1;
        Matcher intent = allOf(hasExtra(RECIPE_ID,RECIPE_ID_AT_POS_0));
        onView(ViewMatchers.withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_ITEM,click()));

        // Check that the intent was launched.
        //noinspection unchecked
        intended(intent);

    }

}


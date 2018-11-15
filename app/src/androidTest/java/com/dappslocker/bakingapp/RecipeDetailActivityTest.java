package com.dappslocker.bakingapp;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {
    private static final String RECIPE_NAME = "recipe_title";
    private static final String RECIPE_ID = "recipe_id";
    Intent intent;
    int recipeId;


    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class,true,false);

    @Before
    public void intialize() {
        intent = new Intent();
        recipeId = 1;
    }
    @After
    public void reset() {
        intent =null;
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
        mActivityTestRule = null;
        mIdlingResource = null;
    }

    @Test
    public void RecyclerViewThatHoldsListOfRecipesIsDisplayed() {
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(RECIPE_NAME, "Nutella Pie");
        mActivityTestRule.launchActivity(intent);
        //register the idling resources
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register( mIdlingResource);

        onView(ViewMatchers.withId(R.id.recycler_view_recipe_detail)).check(matches(isDisplayed()));
    }

    @Test
    public void FirstItemofTheListHasRecpeTextDisplayed() {
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(RECIPE_NAME, "Nutella Pie");
        mActivityTestRule.launchActivity(intent);
        //register the idling resources
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register( mIdlingResource);

        // First, scroll to the position.
        final int FIRST_ITEM = 0;
        onView(ViewMatchers.withId(R.id.recycler_view_recipe_detail))
                .perform(RecyclerViewActions.scrollToPosition(FIRST_ITEM));

        // Check that the item has the special text.
        String recipePositionZero = "Ingridients";
        onView(withText(recipePositionZero)).check(matches(isDisplayed()));
    }
}

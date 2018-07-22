package com.example.android.cookbook;

import android.content.Intent;
import android.provider.Settings;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.cookbook.globaldata.Global_Data;
import com.example.android.cookbook.models.IngredientsModel;
import com.example.android.cookbook.models.RecipeEntityModel;
import com.example.android.cookbook.models.VideosModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by raghvendra on 20/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class IdlingResourceMainActivity  {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;


    @Before
    public void registerIdlingResource() {


        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);

    }

    @Test
    public  void IdlingResourceTest(){

        onView(ViewMatchers.withId(R.id.recipeRecycle)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.item_list)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.player)).check(matches(isDisplayed()));

    }




    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }


}

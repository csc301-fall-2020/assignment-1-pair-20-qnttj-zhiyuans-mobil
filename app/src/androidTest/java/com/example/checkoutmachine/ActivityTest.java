package com.example.checkoutmachine;

import android.app.Activity;
import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.checkoutmachine.Activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainactivity = new ActivityTestRule<>(MainActivity.class);
    public Activity myactivity = mainactivity.getActivity();

    @Test
    public void testAddtoStore(){
        onView(withId(R.id.addtoStoreButton)).perform(click());
        onView(withId(R.id.confButton)).perform(click());
        onView(withId(R.id.Back1Button)).perform(click());
    }

    @Test
    public void testStart(){
        onView(withId(R.id.startButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.removeButton)).perform(click());
        onView(withId(R.id.topayButton)).perform(click());
    }

    @Test
    public void testPay(){
        onView(withId(R.id.startButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.removeButton)).perform(click());
        onView(withId(R.id.topayButton)).perform(click());
        onView(withId(R.id.confirm3)).perform(click());
        onView(withId(R.id.back3Button)).perform(click());
    }

    @Test
    public void testDelete(){
        onView(withId(R.id.deleteButton)).perform(click());
        onView(withId(R.id.confirmButton)).perform(click());
        onView(withId(R.id.deleteAllButton)).perform(click());
        onView(withId(R.id.Back2Button)).perform(click());
    }

    public void testMenu(){
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.back4Button)).perform(click());
    }}

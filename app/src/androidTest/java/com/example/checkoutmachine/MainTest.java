package com.example.checkoutmachine;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.checkoutmachine.Activity.AddtoStoreActivity;
import com.example.checkoutmachine.Activity.CheckoutActivity;
import com.example.checkoutmachine.Activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;
import org.w3c.dom.Text;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import androidx.test.core.app.ApplicationProvider;

public class MainTest {
    @Rule
    public ActivityTestRule<AddtoStoreActivity> addtoStoreActivity = new ActivityTestRule<>(AddtoStoreActivity.class);
    public Activity myactivity = addtoStoreActivity.getActivity();
    public ActivityTestRule<CheckoutActivity> checkoutActivity = new ActivityTestRule<>(CheckoutActivity.class);
    public Activity checkout = checkoutActivity.getActivity();

    public static ViewAction setTextInTextView(final String value) {
        return new ViewAction() {
            @SuppressWarnings("unchecked")
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TextView.class));
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText(value);
            }

            @Override
            public String getDescription() {
                return "replace text";
            }
        };
    }

    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    @Test
    public void Addtest() {
        Context context = ApplicationProvider.getApplicationContext();
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Potato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("1.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(context);
        assert db.getName("10").equals("Potato");
        assert db.getPrice("10") == 1.00;
    }

    @Test
    public void UpdateTest() {
        Context context = ApplicationProvider.getApplicationContext();
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Potato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("1.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Potato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(context);
        assert db.getName("10").equals("Potato");
        assertEquals(db.getName("10"), "Potato");
    }

    @Test
    public void CheckoutTest() {
        Context context = ApplicationProvider.getApplicationContext();
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Potato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("1.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Tomato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Back1Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.startButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        String s = getText(ViewMatchers.withId(R.id.total));
        assertEquals(s, "1.0");
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.total));
        assertEquals(s, "3.0");
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.removeButton)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.total));
        assertEquals(s, "1.0");
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.removeButton)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.total));
        assertEquals(s, "0.0");
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.removeButton)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.total));
        assertEquals(s, "0.0");
    }

    @Test
    public void DeleteTest() {
        Context context = ApplicationProvider.getApplicationContext();
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Potato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("1.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Tomato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Back1Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.deleteButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Back2Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.startButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        String s = getText(ViewMatchers.withId(R.id.total));
        assertEquals(s, "");
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.total));
        assertEquals(s, "");
    }

    @Test
    public void MenuTest() {
        Context context = ApplicationProvider.getApplicationContext();
        onView(ViewMatchers.withId(R.id.Back1Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.deleteButton)).perform(click());
        onView(ViewMatchers.withId(R.id.deleteAllButton)).perform(click());
        onView(ViewMatchers.withId(R.id.Back2Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.addtoStoreButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Potato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("1.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Tomato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Banana"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("12"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Apple"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("13"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Orange"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("14"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Grape"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("15"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Back1Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.MenuButton)).perform(ViewActions.click());
        String s = getText(ViewMatchers.withId(R.id.sMenu));
        String s1 = "10 Potato 1" + "\n" + "11 Tomato 2" + "\n" + "12 Banana 2" + "\n" + "13 Apple 2"
                + "\n" + "14 Orange 2" + "\n" + "15 Grape 2" + "\n";
        assertEquals(s, s1);

        onView(ViewMatchers.withId(R.id.back4Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.deleteButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("12"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("13"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.Back2Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.MenuButton)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.sMenu));
        s1 = "14 Orange 2" + "\n" + "15 Grape 2" + "\n";
        assertEquals(s, s1);

        onView(ViewMatchers.withId(R.id.back4Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.deleteButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("14"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.Barcodedelete)).perform(setTextInTextView("15"));
        onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.Back2Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.MenuButton)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.sMenu));
        s1 = "";
        assertEquals(s, s1);
    }

    @Test
    public void paycheck(){
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Potato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("1.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Tomato"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Banana"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("12"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Apple"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("13"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Orange"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("14"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.NameInput)).perform(setTextInTextView("Grape"));
        onView(ViewMatchers.withId(R.id.barCodeInput)).perform(setTextInTextView("15"));
        onView(ViewMatchers.withId(R.id.priceInput)).perform(setTextInTextView("2.00"));
        onView(ViewMatchers.withId(R.id.confButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.Back1Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.startButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.topayButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.tax)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.discount)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.confirm3)).perform(ViewActions.click());
        String s = getText(ViewMatchers.withId(R.id.paytotal));
        assertEquals(s, "Your total is 4.95");

        onView(ViewMatchers.withId(R.id.back3Button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("11"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("12"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("13"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("14"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.customerbarcodeinput)).perform(setTextInTextView("15"));
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.topayButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.tax)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.discount)).perform(setTextInTextView("10"));
        onView(ViewMatchers.withId(R.id.confirm3)).perform(ViewActions.click());
        s = getText(ViewMatchers.withId(R.id.paytotal));
        assertEquals(s, "Your total is 14.85");
    }
}

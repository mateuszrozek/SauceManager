package pl.rozekm.saucemanager.test;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.frontend.activities.MainActivity;
import pl.rozekm.saucemanager.frontend.activities.ReminderCrudActivity;
import pl.rozekm.saucemanager.frontend.activities.TransactionCrudActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class TransactionCrudActivityTest {

    @Rule
    public ActivityTestRule<TransactionCrudActivity> activityTestRule = new ActivityTestRule<>(TransactionCrudActivity.class);
    private TransactionCrudActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = activityTestRule.getActivity();
    }

    @Test
    public void testLaunch() {
//        View view = activity.findViewById(R.id.textViewOperationCrud);
//        assertNotNull(view);
        onView(withId(R.id.textViewOperationCrud)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }
}
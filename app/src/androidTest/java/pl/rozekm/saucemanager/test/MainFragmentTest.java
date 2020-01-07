package pl.rozekm.saucemanager.test;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.frontend.activities.MainActivity;
import pl.rozekm.saucemanager.frontend.fragments.MainFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainFragmentTest {

    private MainActivity activity = null;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activity = activityTestRule.getActivity();
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testLaunch() {
        View view = activity.findViewById(R.id.lastOperationsTextView);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }
}
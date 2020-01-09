package pl.rozekm.saucemanager.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.frontend.activities.MainActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4ClassRunner.class)
public class OperationCrudTest {

    private final static String TITLE = "Obuwie sportowe";
    private final static String AMOUNT = "150";
    private final static String CATEGORY = "Sport";


    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void addOperation() {
        //scroll to button and check if  is displayed
        onView(withId(R.id.addTransactionImageButton))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        //click 'add' button
        onView(withId(R.id.addTransactionImageButton))
                .perform(click());
        //type in title
        onView(withId(R.id.textInputEditTextOperations))
                .perform(typeText(TITLE));
        //type in amount
        onView(withId(R.id.textInputEditTextOperationsAmount))
                .perform(click(), typeText(AMOUNT));
        //choose category
        onView(withId(R.id.spinnerCategory))
                .perform(click());
        //close keyboard
        onData(allOf(is(instanceOf(String.class)), is(CATEGORY)))
                .perform(click(), closeSoftKeyboard());
        //check if category is chosen
        onView(withId(R.id.spinnerCategory))
                .check(matches(withSpinnerText(containsString(CATEGORY))));
        //click 'add' button
        onView(withId(R.id.buttonUpdateAddReminder))
                .perform(click());
        //scroll to recyclerview
        onView(withId(R.id.transactionsRecyclerView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        //check if operation is added
        onView(withId(R.id.transactionsRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(hasDescendant(hasDescendant(withText(TITLE)))));

    }
}
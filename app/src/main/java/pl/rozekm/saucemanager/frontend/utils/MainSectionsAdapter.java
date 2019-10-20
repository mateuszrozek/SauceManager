package pl.rozekm.saucemanager.frontend.utils;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.frontend.fragments.ForecastFragment;
import pl.rozekm.saucemanager.frontend.fragments.RemindersFragment;
import pl.rozekm.saucemanager.frontend.fragments.TransactionsFragment;

public class MainSectionsAdapter extends FragmentPagerAdapter
{
    private static final int TRANSACTIONS = 0;
    private static final int REMINDERS = 1;
    private static final int FORECAST = 2;


    private static final int[] TABS = new int[]{TRANSACTIONS, REMINDERS, FORECAST};

    private Context mContext;

    public MainSectionsAdapter(final Context context, final FragmentManager fm)
    {
        super(fm);
        mContext = context.getApplicationContext();
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (TABS[position])
        {
            case TRANSACTIONS:
                return TransactionsFragment.newInstance();
            case REMINDERS:
                return RemindersFragment.newInstance();
            case FORECAST:
                return ForecastFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (TABS[position])
        {
            case TRANSACTIONS:
                return mContext.getResources().getString(R.string.transactions);
            case REMINDERS:
                return mContext.getResources().getString(R.string.reminders);
            case FORECAST:
                return mContext.getResources().getString(R.string.forecast);
        }
        return null;
    }
}

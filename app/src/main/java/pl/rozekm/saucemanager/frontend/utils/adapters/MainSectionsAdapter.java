package pl.rozekm.saucemanager.frontend.utils.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.frontend.fragments.ForecastFragment;
import pl.rozekm.saucemanager.frontend.fragments.MainFragment;
import pl.rozekm.saucemanager.frontend.fragments.OperationsFragment;
import pl.rozekm.saucemanager.frontend.fragments.RemindersFragment;
import pl.rozekm.saucemanager.frontend.fragments.StatisticsFragment;

public class MainSectionsAdapter extends FragmentPagerAdapter {

    private static final int MAIN = 0;
    private static final int TRANSACTIONS = 1;
    private static final int STATISTICS = 2;
    private static final int REMINDERS = 3;
    private static final int FORECAST = 4;

    private static final int[] TABS = new int[]{MAIN, TRANSACTIONS, STATISTICS, REMINDERS, FORECAST};

    private Context mContext;

    public MainSectionsAdapter(final Context context, final FragmentManager fm) {
        super(fm);
        mContext = context.getApplicationContext();
    }

    @Override
    public Fragment getItem(int position) {
        switch (TABS[position]) {
            case MAIN:
                return MainFragment.newInstance();
            case TRANSACTIONS:
                return OperationsFragment.newInstance();
            case STATISTICS:
                return StatisticsFragment.newInstance();
            case REMINDERS:
                return RemindersFragment.newInstance();
            case FORECAST:
                return ForecastFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (TABS[position]) {
            case MAIN:
                return mContext.getResources().getString(R.string.main);
            case TRANSACTIONS:
                return mContext.getResources().getString(R.string.transactions);
            case STATISTICS:
                return mContext.getResources().getString(R.string.statistics);
            case REMINDERS:
                return mContext.getResources().getString(R.string.reminders);
            case FORECAST:
                return mContext.getResources().getString(R.string.forecast);
        }
        return null;
    }
}

package pl.rozekm.saucemanager.frontend.activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.databinding.ActivityMainBinding;
import pl.rozekm.saucemanager.frontend.utils.adapters.MainSectionsAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);
        binding.setManager(getSupportFragmentManager());
        ButterKnife.bind(this);
    }

    @BindingAdapter({"bind:handler"})
    public static void bindViewPagerAdapter(final ViewPager view, final MainActivity activity) {
        final MainSectionsAdapter adapter = new MainSectionsAdapter(view.getContext(), activity.getSupportFragmentManager());
        view.setAdapter(adapter);
    }

    @BindingAdapter({"bind:pager"})
    public static void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView) {
        view.setupWithViewPager(pagerView, true);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Wyjście")
                .setMessage("Czy na pewno chcesz opuścić program?")
                .setPositiveButton("Tak", (dialog, which) -> finish())
                .setNegativeButton("Nie", null)
                .show();
    }
}

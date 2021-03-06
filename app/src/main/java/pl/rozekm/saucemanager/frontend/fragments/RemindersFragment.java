package pl.rozekm.saucemanager.frontend.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Reminder;
import pl.rozekm.saucemanager.backend.database.viewmodels.RemindersViewModel;
import pl.rozekm.saucemanager.databinding.RemindersFragmentBinding;
import pl.rozekm.saucemanager.frontend.activities.ReminderCrudActivity;
import pl.rozekm.saucemanager.frontend.utils.AlarmReceiver;
import pl.rozekm.saucemanager.frontend.utils.adapters.RemindersAdapter;


public class RemindersFragment extends Fragment {

    private RemindersViewModel remindersViewModel;
    private RecyclerView remindersRecyclerView;

    private RemindersAdapter remindersAdapter;

    private PendingIntent pendingIntent;

    private static RemindersFragment inst;

    @BindView(R.id.addReminderImageButton)
    Button addReminderImageButton;

    public static RemindersFragment newInstance() {
        return new RemindersFragment();
    }

    public static RemindersFragment instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remindersViewModel = ViewModelProviders.of(this).get(RemindersViewModel.class);
        remindersAdapter = new RemindersAdapter(getContext());
        getReminders();
    }

    private void getReminders() {
        remindersViewModel.getAllReminders().observe(RemindersFragment.this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                remindersAdapter.setReminders(reminders);
                remindersAdapter.notifyDataSetChanged();
                createReminders(reminders);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RemindersFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.reminders_fragment, container, false);
        View view = binding.getRoot();

        ButterKnife.bind(this, view);

        remindersRecyclerView = view.findViewById(R.id.remindersRecyclerView);
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        remindersRecyclerView.setHasFixedSize(true);
        remindersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        remindersRecyclerView.setAdapter(remindersAdapter);

        addReminderImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReminderCrudActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void createReminders(List<Reminder> reminders) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        for (Reminder reminder : reminders) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, reminder.getDate().getMonthValue());
            calendar.set(Calendar.DAY_OF_MONTH, reminder.getDate().getDayOfMonth());
            calendar.set(Calendar.HOUR_OF_DAY, reminder.getDate().getHour());
            calendar.set(Calendar.MINUTE, reminder.getDate().getMinute());
            Intent myIntent = new Intent(getContext(), AlarmReceiver.class);
            myIntent.putExtra("title", reminder.getTitle());
            final int id = reminder.getId().intValue();

            pendingIntent = PendingIntent.getBroadcast(getContext(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            switch (reminder.getFrequency()) {
                case DAILY:
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    break;
                case WEEKLY:
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                    break;
                case MONTHLY:
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getDuration(), pendingIntent);
                    break;
                case YEARLY:
                    setAlarmYearly(calendar, alarmManager);
                    break;
                case MINUTELY:
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10 * 1000, 1000 * 60, pendingIntent);
                    break;
            }
            if (!reminder.getEnabled()) {
                alarmManager.cancel(pendingIntent);
            }
        }
        if (reminders.size() == 0) {
            pendingIntent = PendingIntent.getBroadcast(getContext(), 200, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            alarmManager.cancel(pendingIntent);
        }
    }

    private void setAlarmYearly(Calendar calendar, AlarmManager alarmManager) {
        long calInMillis = calendar.getTimeInMillis();
        LocalDateTime now = LocalDateTime.now();
        long nowInMilli = now.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        if (nowInMilli < calInMillis) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            now.plusYears(1);
            calendar.set(Calendar.YEAR, now.getYear());
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private long getDuration() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        currentMonth++;
        if (currentMonth > Calendar.DECEMBER) {
            currentMonth = Calendar.JANUARY;
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        }
        cal.set(Calendar.MONTH, currentMonth);
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead
        return (thenTime); //this is what you set as trigger point time i.e one month after
    }
}

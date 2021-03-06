package pl.rozekm.saucemanager.frontend.utils.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Reminder;
import pl.rozekm.saucemanager.backend.database.viewmodels.RemindersViewModel;
import pl.rozekm.saucemanager.frontend.activities.MainActivity;
import pl.rozekm.saucemanager.frontend.activities.ReminderCrudActivity;
import pl.rozekm.saucemanager.frontend.utils.FrequenciesConverter;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder> {
    private List<Reminder> reminders;
    private Context context;

    private RemindersViewModel remindersViewModel;

    private FrequenciesConverter converter;

    public static class RemindersViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        public CardView cardView;
        public TextView title;
        public TextView date;
        public TextView frequency;
        public CheckBox enabled;

        public RemindersViewHolder(CardView v, Context context) {
            super(v);
            cardView = v;
            title = v.findViewById(R.id.reminder_item_title);
            date = v.findViewById(R.id.reminder_item_date);
            frequency = v.findViewById(R.id.reminder_item_frequency);
            enabled = v.findViewById(R.id.reminder_check_box);
            this.context = context;
        }

        public Context getContext() {
            return context;
        }
    }

    public RemindersAdapter(Context context) {
        this.context = context;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    @Override
    public RemindersAdapter.RemindersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_item, parent, false);
        RemindersViewHolder vh = new RemindersViewHolder(v, parent.getContext());
        return vh;
    }

    @Override
    public void onBindViewHolder(RemindersViewHolder holder, int position) {

        converter = new FrequenciesConverter();

        Reminder reminder = reminders.get(position);
        holder.title.setText(reminder.getTitle());
        holder.date.setText(reminder.getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        holder.frequency.setText(converter.enumToString(reminder.getFrequency()));
        holder.enabled.setChecked(reminder.getEnabled());

        remindersViewModel = ViewModelProviders.of((MainActivity) holder.getContext()).get(RemindersViewModel.class);

        holder.enabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reminder.setEnabled(!reminder.getEnabled());
            remindersViewModel.update(reminder);
            Toast.makeText(context, setToastText(reminder.getEnabled()), Toast.LENGTH_SHORT).show();
        });

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReminderCrudActivity.class);
            intent.putExtra("reminder", reminder);
            context.startActivity(intent);
        });
    }

    private String setToastText(boolean enabled) {
        if (enabled) return "Przypomnienie aktywne";
        else return "Przypomnienie wyłączone";
    }

    @Override
    public int getItemCount() {
        if (reminders != null)
            return reminders.size();
        else return 0;
    }
}


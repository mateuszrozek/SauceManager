package pl.rozekm.saucemanager.frontend.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Reminder;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder> {
    private List<Reminder> reminders;
    Context context;

    public static class RemindersViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView title;
        public TextView date;
        public TextView frequency;


        public RemindersViewHolder(CardView v) {
            super(v);
            cardView = v;
            title = v.findViewById(R.id.reminder_item_title);
            date = v.findViewById(R.id.reminder_item_date);
            frequency = v.findViewById(R.id.reminder_item_frequency);
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
        RemindersViewHolder vh = new RemindersViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RemindersViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.title.setText(reminder.getTitle());
        holder.date.setText(reminder.getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        holder.frequency.setText(reminder.getFrequency().toString());
    }

    @Override
    public int getItemCount() {
        if (reminders != null)
            return reminders.size();
        else return 0;
    }
}


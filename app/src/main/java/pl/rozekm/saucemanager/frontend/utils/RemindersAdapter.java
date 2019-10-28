package pl.rozekm.saucemanager.frontend.utils;

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

    public RemindersAdapter(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public RemindersAdapter() {

    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    @Override
    public RemindersAdapter.RemindersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_item, parent, false);
        RemindersViewHolder vh = new RemindersViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RemindersViewHolder holder, int position) {

        holder.title.setText(reminders.get(position).getTitle());
//        holder.date.setText(reminders.get(position).getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
//        holder.frequency.setText(reminders.get(position).getFrequency().toString());
        holder.date.setText("DATE");
        holder.frequency.setText("FREQUENCY");
    }

    @Override
    public int getItemCount() {
        if (reminders != null)
            return reminders.size();
        else return 0;
    }
}


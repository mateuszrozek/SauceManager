package pl.rozekm.saucemanager.frontend.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.rozekm.saucemanager.backend.database.model.Reminder;
import pl.rozekm.saucemanager.backend.database.repos.ReminderRepository;

public class RemindersViewModel extends AndroidViewModel {
    private ReminderRepository reminderRepository;

    private LiveData<List<Reminder>> allReminders;

    public RemindersViewModel(@NonNull Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
        allReminders = reminderRepository.getAllReminders();
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }

    public void insert(Reminder reminder) {
        reminderRepository.insertReminder(reminder);
    }
}

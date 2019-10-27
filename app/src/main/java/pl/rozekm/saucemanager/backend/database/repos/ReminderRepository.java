package pl.rozekm.saucemanager.backend.database.repos;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.rozekm.saucemanager.backend.database.daos.ReminderDao;
import pl.rozekm.saucemanager.backend.database.databases.TransactionRoomDatabase;
import pl.rozekm.saucemanager.backend.database.model.Reminder;

public class ReminderRepository {

    private ReminderDao reminderDao;

    private LiveData<List<Reminder>> allReminders;


    public ReminderRepository(Application application) {
        TransactionRoomDatabase database = TransactionRoomDatabase.getDatabase(application);
        reminderDao = database.reminderDao();
        allReminders = reminderDao.getAllReminders();
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }

    public void insertReminder(Reminder reminder) {
        new insertAsyncTask(reminderDao).execute(reminder);
    }

    private static class insertAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao reminderDao;

        insertAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            reminderDao.insertReminder(params[0]);
            return null;
        }
    }
}

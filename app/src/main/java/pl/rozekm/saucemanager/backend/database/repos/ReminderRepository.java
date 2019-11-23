package pl.rozekm.saucemanager.backend.database.repos;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.rozekm.saucemanager.backend.database.daos.ReminderDao;
import pl.rozekm.saucemanager.backend.database.databases.SauceManagerRoomDatabase;
import pl.rozekm.saucemanager.backend.database.model.Reminder;

public class ReminderRepository {

    private ReminderDao reminderDao;

    private LiveData<List<Reminder>> allReminders;


    public ReminderRepository(Application application) {
        SauceManagerRoomDatabase database = SauceManagerRoomDatabase.getDatabase(application);
        reminderDao = database.reminderDao();
        allReminders = reminderDao.getAllReminders();
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }

    public void insertReminder(Reminder reminder) {
        new InsertAsyncTask(reminderDao).execute(reminder);
    }

    public void deleteReminder(Reminder reminder) {
        new DeleteAsyncTask(reminderDao).execute(reminder);
    }

    public void updateReminder(Reminder reminder) {
        new UpdateAsyncTask(reminderDao).execute(reminder);
    }

    private static class InsertAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao reminderDao;

        InsertAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            reminderDao.insertReminder(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao reminderDao;

        DeleteAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            reminderDao.deleteReminder(params[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao reminderDao;

        UpdateAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            reminderDao.updateReminder(params[0]);
            return null;
        }
    }
}

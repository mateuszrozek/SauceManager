package pl.rozekm.saucemanager.backend.database.databases;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import pl.rozekm.saucemanager.backend.database.daos.ReminderDao;
import pl.rozekm.saucemanager.backend.database.daos.TransactionDao;
import pl.rozekm.saucemanager.backend.database.model.Reminder;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.utils.generators.TransactionsGenerator;

@Database(entities = {Transaction.class, Reminder.class}, version = 3, exportSchema = false)
public abstract class TransactionRoomDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    public abstract ReminderDao reminderDao();

    private static TransactionRoomDatabase INSTANCE;

    public synchronized static TransactionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TransactionRoomDatabase.class,
                    "transaction_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDatabaseCallback)
//                            .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDatabaseAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {

        private final TransactionDao transactionDao;
        private final ReminderDao reminderDao;

        TransactionsGenerator generator = new TransactionsGenerator(6);
        List<Transaction> transactions = generator.getTransactions();

        List<Reminder> reminders = Arrays.asList(
                new Reminder("Gaz"),
                new Reminder("WODA"),
                new Reminder("PODATKI")
        );

        PopulateDatabaseAsync(TransactionRoomDatabase database) {
            transactionDao = database.transactionDao();
            reminderDao = database.reminderDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionDao.deleteAllTransactions();
            reminderDao.deleteAllReminders();
            transactions.forEach(transactionDao::insertTransaction);
            reminders.forEach(reminderDao::insertReminder);
            return null;
        }
    }
}

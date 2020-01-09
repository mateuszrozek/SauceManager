package pl.rozekm.saucemanager.backend.database.databases;

import android.content.Context;
import android.os.AsyncTask;

import java.time.LocalDateTime;
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
import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;
import pl.rozekm.saucemanager.backend.utils.generators.TransactionsGenerator;

@Database(entities = {Transaction.class, Reminder.class}, version = 4, exportSchema = false)
public abstract class SauceManagerRoomDatabase extends RoomDatabase {
    public abstract TransactionDao transactionDao();
    public abstract ReminderDao reminderDao();
    private static SauceManagerRoomDatabase instance;
    public static synchronized SauceManagerRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SauceManagerRoomDatabase.class,
                    "sauceManager_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            new PopulateDatabaseAsync(instance).execute();
            super.onOpen(db);
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabaseAsync(instance).execute();
        }
    };

    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {

        private final TransactionDao transactionDao;
        private final ReminderDao reminderDao;

        TransactionsGenerator generator = new TransactionsGenerator(6);
        List<Transaction> transactions = generator.getTransactions();

//        List<Reminder> reminders = Arrays.asList(
//                new Reminder("Gaz co minutę", Frequency.MONTHLY, LocalDateTime.of(2019, 11, 11, 12, 15, 0), true),
//                new Reminder("WODA", Frequency.DAILY, LocalDateTime.of(2019, 11, 11, 21, 37, 0), false),
//                new Reminder("PODATKI za 30 sekund", Frequency.MONTHLY, LocalDateTime.of(2019, 11, 12, 21, 37, 0), true),
//                new Reminder("Nowe przypomnienie", Frequency.MONTHLY, LocalDateTime.of(2019, 11, 21, 20, 20, 0), true)
//        );

        PopulateDatabaseAsync(SauceManagerRoomDatabase database) {
            transactionDao = database.transactionDao();
            reminderDao = database.reminderDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionDao.deleteAllTransactions();
            reminderDao.deleteAllReminders();
            transactions.forEach(transactionDao::insertTransaction);
//            reminders.forEach(reminderDao::insertReminder);
            return null;
        }
    }
}

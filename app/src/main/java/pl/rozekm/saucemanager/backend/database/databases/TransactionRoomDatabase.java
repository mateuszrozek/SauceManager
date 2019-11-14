package pl.rozekm.saucemanager.backend.database.databases;

import android.content.Context;
import android.os.AsyncTask;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

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
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            new PopulateDatabaseAsync(INSTANCE).execute();
            super.onOpen(db);
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabaseAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {

        private final TransactionDao transactionDao;
        private final ReminderDao reminderDao;

        TransactionsGenerator generator = new TransactionsGenerator(6);
        List<Transaction> transactions = generator.getTransactions();

//        List<Transaction> transactions = Arrays.asList(
//                new Transaction(LocalDateTime.now(), 1.1, "Zakupy nr 1", TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(LocalDateTime.now(), 2.2, "Zakupy nr 2", TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(LocalDateTime.now(), 3.3, "Zakupy nr 3", TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(LocalDateTime.now(), 4.4, "Zakupy nr 4", TransactionCategory.OTHER, TransactionType.OUTCOME)
//        );


        List<Reminder> reminders = Arrays.asList(
                new Reminder("Gaz co minutę", Frequency.MONTHLY, LocalDateTime.of(2019, 11, 11, 12, 15, 00), true),
                new Reminder("WODA", Frequency.DAILY, LocalDateTime.of(2019, 11, 11, 21, 37, 00), false),
                new Reminder("PODATKI za 30 sekund", Frequency.MONTHLY, LocalDateTime.of(2019, 11, 12, 21, 37, 00), true)
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

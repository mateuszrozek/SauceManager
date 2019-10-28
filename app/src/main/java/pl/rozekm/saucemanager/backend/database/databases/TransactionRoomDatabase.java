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


        List<Transaction> transactions = Arrays.asList(
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 1)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 2)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 3)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 4)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 5)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 6)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 7)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 8)),
                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 9))
        );

        List<Reminder> reminders = Arrays.asList(
                new Reminder("Gaz"),
                new Reminder("WODA"),
                new Reminder("PODATKI")
//                new Reminder("Gaz", Frequency.MONTHLY, LocalDateTime.of(2019, 10, 21, 19, 9)),
//                new Reminder("Woda", Frequency.MONTHLY, LocalDateTime.of(2019, 7, 1, 0, 0)),
//                new Reminder("Podatki", Frequency.YEARLY, LocalDateTime.of(2019, 3, 12, 4, 9))
        );

//        List<Transaction> transactions = Arrays.asList(
//                new Transaction(25.7, TransactionCategory.SALARY, TransactionType.INCOME),
//                new Transaction(4.3, TransactionCategory.INVESTMENT, TransactionType.INCOME),
//                new Transaction(12.6, TransactionCategory.SAVINGS, TransactionType.INCOME),
//                new Transaction(14.2, TransactionCategory.OTHER, TransactionType.INCOME),
//                new Transaction(22.7, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(11.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(17.2, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(25.7, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(32.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(34.2, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(9.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(19.2, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(12.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(25.7, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(4.3, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(14.2, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(22.7, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(11.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(1.1, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(2.2, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(3.3, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(4.4, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(5.5, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(6.6, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(7.7, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(8.8, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(9.9, TransactionCategory.FOOD, TransactionType.OUTCOME)
//        );

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

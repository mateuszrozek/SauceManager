package pl.rozekm.saucemanager.backend.database.databases;

import android.content.Context;
import android.os.AsyncTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import pl.rozekm.saucemanager.backend.database.daos.ReminderDao;
import pl.rozekm.saucemanager.backend.database.daos.TransactionDao;
import pl.rozekm.saucemanager.backend.database.model.Reminder;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;

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

        List<Transaction> transactions = generateRandoms();

        private List<Transaction> generateRandoms() {
            ArrayList<Transaction> result = new ArrayList<>();

            LocalDateTime localDateTime_1 = LocalDateTime.of(2019, 8, 1, 19, 1);
            LocalDateTime localDateTime_2 = LocalDateTime.of(2019, 9, 1, 19, 1);
            LocalDateTime localDateTime_3 = LocalDateTime.of(2019, 10, 1, 19, 1);

            Random random = new Random();

            for (int i = 0; i < 3; i++) {
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.CLOTHES));
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.ENTERTAINMENT));
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.FOOD));
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.HEALTH));
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.HOUSE));
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.SPORT));
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.TRANSPORT));
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.OTHER));
            }
            for (int i = 0; i < 3; i++) {
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.CLOTHES));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.ENTERTAINMENT));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.FOOD));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.HEALTH));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.HOUSE));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.SPORT));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.TRANSPORT));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.OTHER));
            }
            for (int i = 0; i < 1; i++) {
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.CLOTHES));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.ENTERTAINMENT));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.FOOD));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.HEALTH));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.HOUSE));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.SPORT));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.TRANSPORT));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.OTHER));
            }
            for (int i = 0; i < 4; i++) {
                result.add(new Transaction(localDateTime_1, random.nextDouble() * 100, TransactionCategory.INVESTMENT, TransactionType.INCOME));
                result.add(new Transaction(localDateTime_2, random.nextDouble() * 100, TransactionCategory.SALARY, TransactionType.INCOME));
                result.add(new Transaction(localDateTime_3, random.nextDouble() * 100, TransactionCategory.SAVINGS, TransactionType.INCOME));
            }

            return result;

        }

//        List<Transaction> transactions = Arrays.asList(
//                new Transaction(LocalDateTime.of(2019, 8, 1, 19, 1), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 2, 19, 2), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 3, 19, 3), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 4, 19, 4), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 5, 19, 5), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 6, 19, 6), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 7, 19, 7), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 8, 19, 8), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 9, 19, 2), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 0, 19, 3), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 11, 19, 4), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 21, 19, 5), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 21, 19, 6), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 21, 19, 7), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 21, 19, 8), 1.0),
//                new Transaction(LocalDateTime.of(2019, 8, 21, 19, 2), 1.0), // 16 w sierpniu
//
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 3), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 4), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 5), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 6), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 7), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 8), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 2), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 3), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 4), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 5), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 6), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 7), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 8), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 2), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 3), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 4), 1.0),
//                new Transaction(LocalDateTime.of(2019, 9, 21, 19, 5), 1.0), //17 we wrzesniu
//
//                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 6), 1.0),
//                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 7), 1.0),
//                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 8), 1.0),
//                new Transaction(LocalDateTime.of(2019, 10, 21, 19, 9), 1.0) // 4 w październiku
//        );

//        List<Transaction> transactions = Arrays.asList(
//                new Transaction(2.2, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(3.3, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(4.4, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(5.5, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(6.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(7.7, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(8.8, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(9.9, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(8.8, TransactionCategory.OTHER, TransactionType.OUTCOME)
//        );

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

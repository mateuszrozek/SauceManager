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
import pl.rozekm.saucemanager.backend.database.daos.TransactionDao;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;

@Database(entities = {Transaction.class}, version = 1, exportSchema = false)
public abstract class TransactionRoomDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    private static TransactionRoomDatabase INSTANCE;

    public static TransactionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TransactionRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TransactionRoomDatabase.class,
                            "transaction_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
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

        List<Transaction> transactions = Arrays.asList(
//                new Transaction(25.7, TransactionCategory.FOOD, TransactionType.INCOME),
//                new Transaction(4.3, TransactionCategory.FOOD, TransactionType.INCOME),
//                new Transaction(12.6, TransactionCategory.CLOTHES, TransactionType.INCOME),
//                new Transaction(14.2, TransactionCategory.OTHER, TransactionType.INCOME),
//                new Transaction(22.7, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(11.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(17.2, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(25.7, TransactionCategory.FOOD, TransactionType.OUTCOME),
//                new Transaction(32.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(34.2, TransactionCategory.OTHER, TransactionType.OUTCOME),
//                new Transaction(9.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
//                new Transaction(19.2, TransactionCategory.OTHER, TransactionType.OUTCOME)
                new Transaction(25.7, TransactionCategory.FOOD, TransactionType.OUTCOME),
                new Transaction(4.3, TransactionCategory.FOOD, TransactionType.OUTCOME),
                new Transaction(12.6, TransactionCategory.CLOTHES, TransactionType.OUTCOME),
                new Transaction(14.2, TransactionCategory.OTHER, TransactionType.OUTCOME)
        );

        PopulateDatabaseAsync(TransactionRoomDatabase database) {
            transactionDao = database.transactionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionDao.deleteAllTransactions();
            transactions.forEach(transactionDao::insertTransaction);
            return null;
        }
    }
}

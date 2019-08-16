package pl.rozekm.saucemanager.backend;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;

    public TransactionRepository(Application application) {
        TransactionRoomDatabase database = TransactionRoomDatabase.getDatabase(application);
        transactionDao = database.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public void insertTransaction(Transaction transaction) {
        new InsertAsyncTask(transactionDao).execute(transaction);
    }

    private static class InsertAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private TransactionDao transactionDao;

        InsertAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            transactionDao.insertTransaction(params[0]);
            return null;
        }
    }
}

package pl.rozekm.saucemanager.backend.database.repos;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.rozekm.saucemanager.backend.database.daos.TransactionDao;
import pl.rozekm.saucemanager.backend.database.databases.TransactionRoomDatabase;
import pl.rozekm.saucemanager.backend.database.model.Transaction;

public class TransactionRepository {

    private TransactionDao transactionDao;

    private LiveData<List<Transaction>> allTransactions;
    private LiveData<List<Transaction>> allOutcomeTransactions;
    private LiveData<List<Transaction>> allIncomeTransactions;
    private LiveData<List<Transaction>> allTransactionsByTransactionType;

    public TransactionRepository(Application application, Transaction transaction) {
        TransactionRoomDatabase database = TransactionRoomDatabase.getDatabase(application);
        transactionDao = database.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
        allOutcomeTransactions = transactionDao.getAllOutcomeTransactions();
        allIncomeTransactions = transactionDao.getAllIncomeTransactions();
        allTransactionsByTransactionType = transactionDao.getAllTransactionsByTransactionType(transaction.getType().getCode());
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public LiveData<List<Transaction>> getAllOutcomeTransactions() {
        return allOutcomeTransactions;
    }

    public LiveData<List<Transaction>> getAllIncomeTransactions() {
        return allIncomeTransactions;
    }

    public LiveData<List<Transaction>> getAllTransactionsByTransactionType(int transactionType) {
        return allTransactionsByTransactionType;
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

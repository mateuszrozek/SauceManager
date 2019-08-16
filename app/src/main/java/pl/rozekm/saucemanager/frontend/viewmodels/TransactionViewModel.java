package pl.rozekm.saucemanager.frontend.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.repos.TransactionRepository;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;
    private LiveData<List<Transaction>> allTransactions;
    private LiveData<List<Transaction>> allOutcomeTransactions;
    private LiveData<List<Transaction>> allIncomeTransactions;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        transactionRepository = new TransactionRepository(application);
        allTransactions = transactionRepository.getAllTransactions();
        allOutcomeTransactions = transactionRepository.getAllOutcomeTransactions();
        allIncomeTransactions = transactionRepository.getAllIncomeTransactions();
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

    public void insert(Transaction transaction) {
        transactionRepository.insertTransaction(transaction);
    }
}

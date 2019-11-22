package pl.rozekm.saucemanager.backend.database.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.repos.TransactionRepository;

public class TransactionsViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;

    private LiveData<List<Transaction>> allTransactions;
    private LiveData<List<Transaction>> allOutcomeTransactions;
    private LiveData<List<Transaction>> allIncomeTransactions;
    private LiveData<List<Transaction>> allTransactionsByTransactionType;
    private LiveData<List<Transaction>> limitOutcomeTransactions;

    public TransactionsViewModel(@NonNull Application application, Transaction transaction) {
        super(application);
        transactionRepository = new TransactionRepository(application, transaction);
        allTransactions = transactionRepository.getAllTransactions();
        allOutcomeTransactions = transactionRepository.getAllOutcomeTransactions();
        allIncomeTransactions = transactionRepository.getAllIncomeTransactions();
        allTransactionsByTransactionType = transactionRepository.getAllTransactionsByTransactionType(transaction.getType().getCode());
        limitOutcomeTransactions = transactionRepository.getLimitOutcomeTransactions();
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

    public LiveData<List<Transaction>> getLimitOutcomeTransactions(){
        return limitOutcomeTransactions;
    }

    public void insert(Transaction transaction) {
        transactionRepository.insertTransaction(transaction);
    }

    public void update(Transaction transaction){
        transactionRepository.update(transaction);
    }

    public void delete(Transaction transaction){
        transactionRepository.delete(transaction);
    }
}

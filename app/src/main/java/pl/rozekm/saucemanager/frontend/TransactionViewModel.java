package pl.rozekm.saucemanager.frontend;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.rozekm.saucemanager.backend.Transaction;
import pl.rozekm.saucemanager.backend.TransactionRepository;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;
    private LiveData<List<Transaction>> allTransactions;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        transactionRepository = new TransactionRepository(application);
        allTransactions = transactionRepository.getAllTransactions();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public void insert(Transaction transaction) {
        transactionRepository.insertTransaction(transaction);
    }
}

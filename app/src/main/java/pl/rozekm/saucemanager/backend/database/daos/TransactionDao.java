package pl.rozekm.saucemanager.backend.database.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import pl.rozekm.saucemanager.backend.database.model.Transaction;

@Dao
public interface TransactionDao {

    @Insert
    void insertTransaction(Transaction transaction);

    @Update
    void updateTransaction(Transaction...transactions);

    @Delete
    void deleteTransaction(Transaction transaction);

    @Query("DELETE FROM `transaction`")
    void deleteAllTransactions();

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM `transaction` WHERE transaction_type=:transactionType")
    LiveData<List<Transaction>> getAllTransactionsByTransactionType(int transactionType);

    @Query("SELECT * FROM `transaction` WHERE transaction_type=1")
    LiveData<List<Transaction>> getAllIncomeTransactions();

    @Query("SELECT * FROM `transaction` WHERE transaction_type=0")
    LiveData<List<Transaction>> getAllOutcomeTransactions();

    @Query("SELECT * FROM `transaction` WHERE transaction_type=0 ORDER BY date DESC LIMIT 5")
    LiveData<List<Transaction>> getLimitOutcomeTransactions();
}

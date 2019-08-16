package pl.rozekm.saucemanager.backend.database.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import pl.rozekm.saucemanager.backend.database.model.Transaction;

@Dao
public interface TransactionDao {

    @Insert
    void insertTransaction(Transaction transaction);

    @Query("DELETE FROM `transaction`")
    void deleteAllTransactions();

    @Query("SELECT * FROM `transaction` ORDER BY id ASC")
    LiveData<List<Transaction>> getAllTransactions();

//    @Query("SELECT * FROM `transaction` WHERE transaction_type=:transactionType ORDER BY id ASC")
//    LiveData<List<Transaction>> getAllTransactionsByTransactionType(TransactionType transactionType);

    @Query("SELECT * FROM `transaction` WHERE transaction_type=1 ORDER BY id ASC")
    LiveData<List<Transaction>> getAllIncomeTransactions();

    @Query("SELECT * FROM `transaction` WHERE transaction_type='OUTCOME' ORDER BY id ASC")
    LiveData<List<Transaction>> getAllOutcomeTransactions();
}

package pl.rozekm.saucemanager.backend;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TransactionDao {

    @Insert
    void insertTransaction(Transaction transaction);

    @Query("DELETE FROM `transaction`")
    void deleteAllTransactions();

    @Query("SELECT * FROM `transaction` ORDER BY id ASC")
    LiveData<List<Transaction>> getAllTransactions();
}

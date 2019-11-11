package pl.rozekm.saucemanager.backend.database.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import pl.rozekm.saucemanager.backend.database.model.Reminder;

@Dao
public interface ReminderDao {
    @Insert
    void insertReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Query("SELECT * FROM `reminder`")
    LiveData<List<Reminder>> getAllReminders();

    @Query("DELETE FROM `reminder`")
    void deleteAllReminders();
}

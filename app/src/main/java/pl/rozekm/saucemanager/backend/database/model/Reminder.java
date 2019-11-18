package pl.rozekm.saucemanager.backend.database.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;
import pl.rozekm.saucemanager.backend.utils.converters.LocalDateTimeConverter;
import pl.rozekm.saucemanager.backend.utils.converters.ReminderFrequencyConverter;

@Entity
public class Reminder implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "frequency")
    @TypeConverters(ReminderFrequencyConverter.class)
    private Frequency frequency;

    @ColumnInfo(name = "date")
    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime date;

    @ColumnInfo(name = "enabled")
    private Boolean enabled;

    public Reminder(String title, Frequency frequency, LocalDateTime date) {
        this.title = title;
        this.frequency = frequency;
        this.date = date;
        enabled = true;
    }

    @Ignore
    public Reminder() {
        this.title = "New title";
        this.frequency = Frequency.MONTHLY;
        this.date = LocalDateTime.now();
        enabled = true;
    }

    @Ignore
    public Reminder(String title, Frequency frequency, LocalDateTime date, Boolean enabled) {
        this.title = title;
        this.frequency = frequency;
        this.date = date;
        this.enabled = enabled;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}

package pl.rozekm.saucemanager.backend;

import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "transaction")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    private Double amount;
    private String title;
    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime date;
    @TypeConverters(TransactionCategoryConverter.class)
    private TransactionCategory category;
    @TypeConverters(TransactionTypeConverter.class)
    private TransactionType type;

    public Transaction(@NonNull Long id, Double amount, LocalDateTime date, TransactionCategory category, TransactionType type) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.type = type;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}

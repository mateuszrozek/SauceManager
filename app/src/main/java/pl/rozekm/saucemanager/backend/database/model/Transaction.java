package pl.rozekm.saucemanager.backend.database.model;

import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.backend.utils.converters.LocalDateTimeConverter;
import pl.rozekm.saucemanager.backend.utils.converters.TransactionCategoryConverter;
import pl.rozekm.saucemanager.backend.utils.converters.TransactionTypeConverter;

@Entity(tableName = "transaction")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "amount")
    private Double amount;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime date;

    @ColumnInfo(name = "transaction_category")
    @TypeConverters(TransactionCategoryConverter.class)
    private TransactionCategory category;

    @ColumnInfo(name = "transaction_type")
    @TypeConverters(TransactionTypeConverter.class)
    private TransactionType type;

    public Transaction(Double amount, TransactionCategory category, TransactionType type) {
        this.amount = amount;
        title = "";
        this.date = LocalDateTime.now();
        this.category = category;
        this.type = type;
    }

    @Ignore
    public Transaction(){
        this.amount = 0.0;
        title = "";
        this.date = LocalDateTime.now();
        this.category = TransactionCategory.OTHER;
        this.type = TransactionType.OUTCOME;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", category=" + category +
                ", type=" + type +
                '}';
    }
}

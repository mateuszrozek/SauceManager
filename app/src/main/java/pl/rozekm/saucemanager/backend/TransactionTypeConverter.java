package pl.rozekm.saucemanager.backend;

import androidx.room.TypeConverter;

import static pl.rozekm.saucemanager.backend.TransactionType.INCOME;
import static pl.rozekm.saucemanager.backend.TransactionType.OUTCOME;

public class TransactionTypeConverter {

    @TypeConverter
    public static TransactionType toTransactionType(int transactionType) {

        switch (transactionType) {
            case 1:
                return INCOME;
            default:
                return OUTCOME;
        }
    }

    @TypeConverter
    public static Integer toInteger(TransactionType transactionType) {
        return transactionType.getCode();
    }
}

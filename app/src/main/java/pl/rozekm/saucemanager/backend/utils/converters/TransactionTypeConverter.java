package pl.rozekm.saucemanager.backend.utils.converters;

import androidx.room.TypeConverter;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;

import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionType.INCOME;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionType.OUTCOME;

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

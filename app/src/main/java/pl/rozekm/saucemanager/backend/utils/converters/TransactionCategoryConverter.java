package pl.rozekm.saucemanager.backend.utils.converters;

import androidx.room.TypeConverter;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;

import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.CLOTHES;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.ENTERTAINMENT;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.FOOD;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.HOUSE;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.OTHER;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.SPORT;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.TRANSPORT;

public class TransactionCategoryConverter {

    @TypeConverter
    public static TransactionCategory toTransactionCategory(int transactionCategory) {

        switch (transactionCategory) {
            case 1:
                return CLOTHES;
            case 2:
                return ENTERTAINMENT;
            case 3:
                return FOOD;
            case 4:
                return HOUSE;
            case 5:
                return SPORT;
            case 6:
                return TRANSPORT;
            default:
                return OTHER;
        }
    }

    @TypeConverter
    public static Integer toInteger(TransactionCategory transactionCategory) {
        return transactionCategory.getCode();
    }
}

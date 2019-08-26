package pl.rozekm.saucemanager.backend.utils.converters;

import androidx.room.TypeConverter;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;

import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.CLOTHES;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.ENTERTAINMENT;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.FOOD;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.HEALTH;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.HOUSE;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.INVESTMENT;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.OTHER;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.SALARY;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.SAVINGS;
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
                return HEALTH;
            case 5:
                return HOUSE;
            case 6:
                return SPORT;
            case 7:
                return TRANSPORT;
            case 91:
                return INVESTMENT;
            case 92:
                return SALARY;
            case 93:
                return SAVINGS;
            default:
                return OTHER;
        }
    }

    @TypeConverter
    public static Integer toInteger(TransactionCategory transactionCategory) {
        return transactionCategory.getCode();
    }
}

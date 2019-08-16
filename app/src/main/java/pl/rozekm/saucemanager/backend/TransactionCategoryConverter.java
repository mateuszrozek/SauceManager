package pl.rozekm.saucemanager.backend;

import androidx.room.TypeConverter;

import static pl.rozekm.saucemanager.backend.TransactionCategory.CLOTHES;
import static pl.rozekm.saucemanager.backend.TransactionCategory.ENTERTAINMENT;
import static pl.rozekm.saucemanager.backend.TransactionCategory.FOOD;
import static pl.rozekm.saucemanager.backend.TransactionCategory.HOUSE;
import static pl.rozekm.saucemanager.backend.TransactionCategory.OTHER;
import static pl.rozekm.saucemanager.backend.TransactionCategory.SPORT;
import static pl.rozekm.saucemanager.backend.TransactionCategory.TRANSPORT;

class TransactionCategoryConverter {

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

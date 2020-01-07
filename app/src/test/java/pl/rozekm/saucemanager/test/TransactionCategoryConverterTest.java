package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.utils.converters.TransactionCategoryConverter;

public class TransactionCategoryConverterTest {

    private final static TransactionCategory TRANSACTION_CATEGORY_ENUM = TransactionCategory.CLOTHES;
    private final static int TRANSACTION_CATEGORY_INT = 1;

    private static TransactionCategoryConverter converter = null;

    @Before
    public void setUp() throws Exception {
        converter = new TransactionCategoryConverter();
    }

    @Test
    public void integerToTransactionCategory() {
        TransactionCategory result = converter.toTransactionCategory(TRANSACTION_CATEGORY_INT);

        Assert.assertEquals(TRANSACTION_CATEGORY_ENUM, result);
    }

    @Test
    public void transactionCategoryToInteger() {
        int result = converter.toInteger(TRANSACTION_CATEGORY_ENUM);

        Assert.assertEquals(TRANSACTION_CATEGORY_INT, result);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
    }
}

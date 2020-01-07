package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.utils.converters.TransactionCategoryConverter;

import static org.junit.Assert.*;

public class TransactionCategoryConverterTest {

    private final static TransactionCategory TRANSACTION_CATEGORY_ENUM = TransactionCategory.CLOTHES;
    private final static int TRANSACTION_CATEGORY_INT = 1;

    private TransactionCategoryConverter converter = null;

    @Before
    public void setUp() throws Exception {
        converter = new TransactionCategoryConverter();
    }

    @Test
    public void toTransactionCategory() {
        TransactionCategory result = converter.toTransactionCategory(TRANSACTION_CATEGORY_INT);

        Assert.assertEquals(result, TRANSACTION_CATEGORY_ENUM);
    }

    @Test
    public void toInteger() {
        int result = converter.toInteger(TRANSACTION_CATEGORY_ENUM);

        Assert.assertEquals(result, TRANSACTION_CATEGORY_INT);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
    }
}
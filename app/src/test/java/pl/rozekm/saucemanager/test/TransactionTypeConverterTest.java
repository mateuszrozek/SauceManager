package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.backend.utils.converters.TransactionTypeConverter;

public class TransactionTypeConverterTest {

    private final static TransactionType TRANSACTION_TYPE = TransactionType.OUTCOME;
    private final static int INT = 0;

    private TransactionTypeConverter converter = null;

    @Before
    public void setUp() throws Exception {
        converter = new TransactionTypeConverter();
    }

    @Test
    public void toTransactionType() {
        TransactionType result = converter.toTransactionType(INT);

        Assert.assertEquals(result, TRANSACTION_TYPE);
    }

    @Test
    public void toInteger() {
        int result = converter.toInteger(TRANSACTION_TYPE);

        Assert.assertEquals(result, INT);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
    }
}
package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.utils.generators.TransactionsGenerator;

import static java.util.stream.Collectors.groupingBy;

public class TransactionsGeneratorTest {

    private static final int GENERATION_PERIOD = 6;
    private static final int CATEGORIES_NUMBER = TransactionCategory.values().length;

    private TransactionsGenerator generator = null;
    private List<Transaction> transactions = null;

    @Before
    public void setUp() throws Exception {
        generator = new TransactionsGenerator(GENERATION_PERIOD);
        transactions = generator.getTransactions();
    }

    @Test
    public void shouldGenerateTransactions() {
        Assert.assertTrue(transactions.size() > 0);
    }

    @Test
    public void shouldGenerateDifferentMonthTransactions() {
        int keySize = transactions.stream()
                .collect(groupingBy(Transaction::getMonth))
                .keySet().size();

        Assert.assertTrue(keySize > 0);
        Assert.assertTrue(keySize < GENERATION_PERIOD || keySize == GENERATION_PERIOD);
    }

    @Test
    public void shouldGenerateDifferentCategoriesTransactions() {
        int keySize = transactions.stream()
                .collect(groupingBy(Transaction::getCategory))
                .keySet().size();

        Assert.assertTrue(keySize > 0);
        Assert.assertTrue(keySize < CATEGORIES_NUMBER || keySize == CATEGORIES_NUMBER);
    }

    @After
    public void tearDown() throws Exception {
        generator = null;
        transactions = null;
    }
}
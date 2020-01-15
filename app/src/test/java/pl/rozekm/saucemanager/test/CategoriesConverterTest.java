package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.frontend.utils.CategoriesConverter;

import static org.junit.Assert.*;

public class CategoriesConverterTest {

    private final static TransactionCategory TRANSACTION_CATEGORY = TransactionCategory.CLOTHES;
    private final static String STRING = "Ubrania";
    
    private ArrayList<String> operations;
    private ArrayList<String> incomes;
    private ArrayList<String> outcomes;
    private List<TransactionCategory> categories;

    private CategoriesConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new CategoriesConverter();
        operations = converter.getOperationsStrings();
        incomes = converter.getIncomesStrings();
        outcomes = converter.getOutcomesStrings();
        categories = Arrays.asList(TransactionCategory.values());
    }

    @Test
    public void stringToEnum(){
        TransactionCategory result = converter.stringToEnum(STRING);

        Assert.assertEquals(result, TRANSACTION_CATEGORY);

        for (int i = 0; i < operations.size()-1; i++) {
            Assert.assertEquals(categories.get(i), converter.stringToEnum(operations.get(i)));
        }

    }

    @Test
    public void enumToString(){
        String result = converter.enumToString(TRANSACTION_CATEGORY);

        Assert.assertEquals(result, STRING);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
    }
}
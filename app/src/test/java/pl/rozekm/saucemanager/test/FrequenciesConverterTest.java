package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.frontend.utils.CategoriesConverter;
import pl.rozekm.saucemanager.frontend.utils.FrequenciesConverter;

import static org.junit.Assert.*;

public class FrequenciesConverterTest {

    private final static Frequency FREQUENCY = Frequency.MONTHLY;
    private final static String STRING = "Co miesiąc";

    private FrequenciesConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new FrequenciesConverter();
    }

    @Test
    public void stringToEnum(){
        Frequency result = converter.stringToEnum(STRING);

        Assert.assertEquals(result, FREQUENCY);
    }

    @Test
    public void enumToString(){
        String result = converter.enumToString(FREQUENCY);

        Assert.assertEquals(result, STRING);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
    }
}
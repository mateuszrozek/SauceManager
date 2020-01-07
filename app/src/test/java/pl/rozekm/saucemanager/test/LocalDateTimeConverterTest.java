package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import pl.rozekm.saucemanager.backend.utils.converters.LocalDateTimeConverter;

public class LocalDateTimeConverterTest {

    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2019, 12, 31, 12, 12, 12);
    private final static String STRING = "2019-12-31T12:12:12";

    private LocalDateTimeConverter converter = null;

    @Before
    public void setUp() throws Exception {
        converter = new LocalDateTimeConverter();
    }

    @Test
    public void toDate() {
        String result = converter.toDateString(LOCAL_DATE_TIME);

        Assert.assertEquals(result, STRING);
    }

    @Test
    public void toDateString() {
        LocalDateTime result = converter.toDate(STRING);

        Assert.assertEquals(result, LOCAL_DATE_TIME);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
    }
}
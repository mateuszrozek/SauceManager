package pl.rozekm.saucemanager.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;
import pl.rozekm.saucemanager.backend.utils.converters.LocalDateTimeConverter;
import pl.rozekm.saucemanager.backend.utils.converters.ReminderFrequencyConverter;

import static org.junit.Assert.*;

public class ReminderFrequencyConverterTest {

    private final static Frequency FREQUENCY = Frequency.YEARLY;
    private final static int INT = 365;

    private ReminderFrequencyConverter converter = null;

    @Before
    public void setUp() throws Exception {
        converter = new ReminderFrequencyConverter();
    }

    @Test
    public void toFrequency() {
        Frequency result = converter.toFrequency(INT);

        Assert.assertEquals(result, FREQUENCY);
    }

    @Test
    public void toInteger() {
        int result = converter.toInteger(FREQUENCY);

        Assert.assertEquals(result, INT);
    }

    @After
    public void tearDown() throws Exception {
        converter = null;
    }
}

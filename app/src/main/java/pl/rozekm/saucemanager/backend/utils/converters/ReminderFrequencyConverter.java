package pl.rozekm.saucemanager.backend.utils.converters;

import androidx.room.TypeConverter;
import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;

import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.DAILY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.MINUTELY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.MONTHLY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.WEEKLY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.YEARLY;

public class ReminderFrequencyConverter {

    @TypeConverter
    public static Frequency toFrequency(int frequency) {

        switch (frequency) {
            case 1:
                return DAILY;
            case 7:
                return WEEKLY;
            case 30:
                return MONTHLY;
            case 365:
                return YEARLY;
            case 88:
                return MINUTELY;
            default:
                return MONTHLY;
        }
    }

    @TypeConverter
    public static Integer toInteger(Frequency frequency) {
        return frequency.getCode();
    }
}

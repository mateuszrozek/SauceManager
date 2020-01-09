package pl.rozekm.saucemanager.frontend.utils;

import java.util.ArrayList;
import java.util.List;

import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;

import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.DAILY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.MINUTELY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.MONTHLY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.WEEKLY;
import static pl.rozekm.saucemanager.backend.database.model.enums.Frequency.YEARLY;


public class FrequenciesConverter {


    List<String> frequencies = new ArrayList<>();

    public FrequenciesConverter() {
        frequencies.add("Codziennie");
        frequencies.add("Co tydzień");
        frequencies.add("Co miesiąc");
        frequencies.add("Co rok");
        frequencies.add("Co minutę");
    }

    public String enumToString(Frequency frequency) {
        switch (frequency) {
            case DAILY:
                return "Codziennie";
            case WEEKLY:
                return "Co tydzień";
            case MONTHLY:
                return "Co miesiąc";
            case YEARLY:
                return "Co rok";
            case MINUTELY:
                return "Co minutę";
            default:
                return "HE";
        }
    }

    public Frequency stringToEnum(String string) {
        switch (string) {
            case "Codziennie":
                return DAILY;
            case "Co tydzień":
                return WEEKLY;
            case "Co miesiąc":
                return MONTHLY;
            case "Co rok":
                return YEARLY;
            case "Co minutę":
                return MINUTELY;
            default:
                return MINUTELY;
        }
    }

    public List<String> getFrequencies() {
        return frequencies;
    }
}

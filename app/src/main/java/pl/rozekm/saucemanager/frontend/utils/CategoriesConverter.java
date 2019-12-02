package pl.rozekm.saucemanager.frontend.utils;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;

import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.ALL;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.CLOTHES;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.ENTERTAINMENT;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.FOOD;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.HEALTH;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.HOUSE;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.INVESTMENT;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.OTHER;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.SALARY;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.SAVINGS;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.SPORT;
import static pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory.TRANSPORT;

public class CategoriesConverter {

    ArrayList<String> outcomesStrings = new ArrayList<>();
    ArrayList<String> incomesStrings = new ArrayList<>();
    ArrayList<String> operationsStrings = new ArrayList<>();

    public CategoriesConverter(){
        outcomesStrings.add("Ubrania");
        outcomesStrings.add("Rozrywka");
        outcomesStrings.add("Jedzenie");
        outcomesStrings.add("Zdrowie");
        outcomesStrings.add("Art. domowe");
        outcomesStrings.add("Sport");
        outcomesStrings.add("Transport");
        outcomesStrings.add("Inne");

        incomesStrings.add("Inwestycje");
        incomesStrings.add("Pensja");
        incomesStrings.add("Oszczędności");

        operationsStrings.add("Wszystkie");

        operationsStrings.add("Ubrania");
        operationsStrings.add("Rozrywka");
        operationsStrings.add("Jedzenie");
        operationsStrings.add("Zdrowie");
        operationsStrings.add("Art. domowe");
        operationsStrings.add("Sport");
        operationsStrings.add("Transport");
        operationsStrings.add("Inne");

        operationsStrings.add("Inwestycje");
        operationsStrings.add("Pensja");
        operationsStrings.add("Oszczędności");
    }

    public ArrayList<String> getOutcomesStrings() {
        return outcomesStrings;
    }

    public void setOutcomesStrings(ArrayList<String> outcomesStrings) {
        this.outcomesStrings = outcomesStrings;
    }

    public ArrayList<String> getIncomesStrings() {
        return incomesStrings;
    }

    public void setIncomesStrings(ArrayList<String> incomesStrings) {
        this.incomesStrings = incomesStrings;
    }

    public ArrayList<String> getOperationsStrings() {
        return operationsStrings;
    }

    public void setOperationsStrings(ArrayList<String> operationsStrings) {
        this.operationsStrings = operationsStrings;
    }

    public String enumToString(TransactionCategory category) {
        switch (category) {
            case CLOTHES:
                return "Ubrania";
            case ENTERTAINMENT:
                return "Rozrywka";
            case FOOD:
                return "Jedzenie";
            case HEALTH:
                return "Zdrowie";
            case HOUSE:
                return "Art. domowe";
            case SPORT:
                return "Sport";
            case TRANSPORT:
                return "Transport";
            case OTHER:
                return "Inne";

            case INVESTMENT:
                return "Inwestycje";
            case SALARY:
                return "Pensja";
            case SAVINGS:
                return "Oszczędności";
            default:
                return "Wszystkie";
        }
    }

    public TransactionCategory stringToEnum(String string) {
        switch (string) {
            case "Ubrania":
                return CLOTHES;
            case "Rozrywka":
                return ENTERTAINMENT;
            case "Jedzenie":
                return FOOD;
            case "Zdrowie":
                return HEALTH;
            case "Art. domowe":
                return HOUSE;
            case "Sport":
                return SPORT;
            case "Transport":
                return TRANSPORT;
            case "Inne":
                return OTHER;

            case "Inwestycje":
                return INVESTMENT;
            case "Pensja":
                return SALARY;
            case "Oszczędności":
                return SAVINGS;
            default:
                return ALL;
        }
    }
}

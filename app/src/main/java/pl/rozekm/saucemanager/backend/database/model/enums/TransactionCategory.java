package pl.rozekm.saucemanager.backend.database.model.enums;

import java.io.Serializable;

public enum TransactionCategory implements Serializable {
    ALL(99),

    CLOTHES(1),
    ENTERTAINMENT(2),
    FOOD(3),
    HEALTH(4),
    HOUSE(5),
    SPORT(6),
    TRANSPORT(7),
    OTHER(0),

    INVESTMENT(91),
    SALARY(92),
    SAVINGS(93);

    private int code;

    TransactionCategory(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

package pl.rozekm.saucemanager.backend.database.model.enums;

import java.io.Serializable;

public enum Frequency implements Serializable {
    DAILY(1),
    WEEKLY(7),
    MONTHLY(30),
    YEARLY(365),
    MINUTELY(88);

    private int code;

    Frequency(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

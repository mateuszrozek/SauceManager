package pl.rozekm.saucemanager.backend.database.model.enums;

import java.io.Serializable;

public enum TransactionType implements Serializable {
    INCOME(1),
    OUTCOME(0);

    private int code;

    TransactionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

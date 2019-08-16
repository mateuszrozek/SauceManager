package pl.rozekm.saucemanager.backend.database.model.enums;

public enum TransactionType {
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

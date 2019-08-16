package pl.rozekm.saucemanager.backend.database.model.enums;

public enum TransactionCategory {
    CLOTHES(1),
    ENTERTAINMENT(2),
    FOOD(3),
    HOUSE(4),
    SPORT(5),
    TRANSPORT(6),
    OTHER(0);

    private int code;

    TransactionCategory(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

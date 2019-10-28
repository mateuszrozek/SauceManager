package pl.rozekm.saucemanager.frontend.utils;

import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;

public class Forecast {

    TransactionCategory category;
    Double typically;
    Double soFar;
    Double remaining;

    public Forecast(TransactionCategory category, Double typically, Double soFar, Double remaining) {
        this.category = category;
        this.typically = typically;
        this.soFar = soFar;
        this.remaining = remaining;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public Double getTypically() {
        return typically;
    }

    public void setTypically(Double typically) {
        this.typically = typically;
    }

    public Double getSoFar() {
        return soFar;
    }

    public void setSoFar(Double soFar) {
        this.soFar = soFar;
    }

    public Double getRemaining() {
        return remaining;
    }

    public void setRemaining(Double remaining) {
        this.remaining = remaining;
    }
}

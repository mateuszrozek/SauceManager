package pl.rozekm.saucemanager.frontend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;

public class TransactionsSorter {

    private List<Transaction> allTransactions;

    public TransactionsSorter(List<Transaction> allTransactions) {
        this.allTransactions = allTransactions;
    }

    public List<Transaction> sortByType(List<Transaction> allTransactions, TransactionType type) {
        ArrayList<Transaction> sortedTransactions = new ArrayList<>();
        allTransactions.stream().filter(t -> t.getType().equals(type)).forEach(sortedTransactions::add);
        return sortedTransactions;
    }

    public Map<TransactionCategory, Float> valuesOfEachCategory(List<Transaction> transactions) {
        Map<TransactionCategory, Float> result = new HashMap<>();

        Map<TransactionCategory, List<Transaction>> groupedByCategoryTransactions = transactions.stream().collect(Collectors.groupingBy(Transaction::getCategory));


        for (TransactionCategory category : groupedByCategoryTransactions.keySet()) {
            List<Transaction> transactionsOfCategory = groupedByCategoryTransactions.get(category);
            float sum = 0.0f;
            for (Transaction transaction : transactionsOfCategory) {
                sum = sum + (float) ((double) transaction.getAmount());
            }
            result.put(category, sum);
        }

        return result;
    }

    public List<Float> accountState(List<Transaction> transactions, float initialValue) {
        ArrayList<Float> result = new ArrayList<>();

        result.add(initialValue);

        for (int i = 1; i < transactions.size(); i++) {

            float val;
            if (transactions.get(i).getType() == TransactionType.OUTCOME) {
                val = (float) ((double) transactions.get(i).getAmount()) - result.get(i - 1);
            } else {
                val = (float) ((double) transactions.get(i).getAmount()) + result.get(i - 1);
            }
            result.add(val);
        }
        return result;
    }

    public List<float[]> cashFlow(List<Transaction> allTransactions) {

        Map<Month, List<Transaction>> groupedByMonthTransactions = allTransactions.stream().collect(Collectors.groupingBy(t -> t.getDate().getMonth()));

        List<float[]> result = new ArrayList<>();

        for (Month month : groupedByMonthTransactions.keySet()) {
            List<Transaction> transactionsOfMonth = groupedByMonthTransactions.get(month);

            float incomes = 0.0f;
            float outcomes = 0.0f;

            for (Transaction transaction : transactionsOfMonth) {
                if (transaction.getType()==TransactionType.OUTCOME){
                    outcomes = outcomes + (float)((double)transaction.getAmount());
                }
                else {
                    incomes = incomes + (float)((double)transaction.getAmount());
                }
            }
            result.add( new float[]{-outcomes, incomes});
        }
        return result;
    }


    public List<Transaction> sortByFrequency(List<Transaction> transactions, Frequency frequency) {

        List<Transaction> result = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        switch (frequency) {
            case DAILY: {
                result = filterDay(transactions, now);
            }
            break;
            case WEEKLY: {
                result = filterWeek(transactions, now);
            }
            break;
            case MONTHLY: {
                result = filterMonth(transactions, now);
            }
            break;
            case YEARLY: {
                result = filterYear(transactions, now);
            }
            break;
        }
        return result;
    }


    private List<Transaction> filterDay(List<Transaction> transactions, LocalDateTime now) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate localDateTransaction = transaction.getDate().toLocalDate();
            LocalDate localDateNow = now.toLocalDate();
            if (localDateTransaction.compareTo(localDateNow) == 0) {
                result.add(transaction);
            }
        }
        return result;
    }

    private List<Transaction> filterWeek(List<Transaction> transactions, LocalDateTime now) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate ldTrans = transaction.getDate().toLocalDate();
            LocalDate ldNow = now.toLocalDate();

            TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int weekTrans = ldTrans.get(woy);
            int weekNow = ldNow.get(woy);

            if ((ldTrans.getYear() == ldNow.getYear()) && (ldTrans.getMonth() == ldNow.getMonth()) && (weekTrans == weekNow)) {
                result.add(transaction);
            }
        }
        return result;
    }

    private List<Transaction> filterMonth(List<Transaction> transactions, LocalDateTime now) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate ldTrans = transaction.getDate().toLocalDate();
            LocalDate ldNow = now.toLocalDate();
            if ((ldTrans.getYear() == ldNow.getYear()) && (ldTrans.getMonth() == ldNow.getMonth())) {
                result.add(transaction);
            }
        }
        return result;
    }

    private List<Transaction> filterYear(List<Transaction> transactions, LocalDateTime now) {
        List<Transaction> result = new ArrayList<>();
        transactions.stream().filter(t -> t.getDate().getYear() == now.getYear()).forEach(result::add);
        return result;
    }


    public Float getMinimum(List<float[]> cashFlowByMonths) {
        List<Float> negatives = new ArrayList<>();
        cashFlowByMonths.forEach(item->negatives.add(item[0]));

        return negatives.stream().min(Comparator.naturalOrder()).get();
    }

    public Float getMaximum(List<float[]> cashFlowByMonths) {
        List<Float> positives = new ArrayList<>();
        cashFlowByMonths.forEach(item->positives.add(item[1]));

        return positives.stream().max(Comparator.naturalOrder()).get();
    }
}

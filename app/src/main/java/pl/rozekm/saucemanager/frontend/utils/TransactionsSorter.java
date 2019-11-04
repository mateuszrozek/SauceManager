package pl.rozekm.saucemanager.frontend.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.rozekm.saucemanager.backend.database.model.Transaction;
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

    public int numberOfCategories(List<Transaction> transactions) {
        Map<TransactionCategory, List<Transaction>> groupedByCategoryTransactions = transactions.stream().collect(Collectors.groupingBy(Transaction::getCategory));
        return groupedByCategoryTransactions.size();
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

//    private ArrayList<BarEntry> addBarEntries(List<Transaction> transactions, TransactionType transactionType) {
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        ArrayList<Float> values;
//
//        Map<TransactionCategory, List<Transaction>> groupedTransactions =
//                transactions.stream().collect(Collectors.groupingBy(Transaction::getCategory));
//
//        values = processGroupedTransactions(groupedTransactions, transactionType);
//
//        int numberOfCategories;
//
//        if (transactionType == TransactionType.OUTCOME) {
//            numberOfCategories = 8;
//        } else {
//            numberOfCategories = 3;
//        }
//
//        float[] arr = new float[numberOfCategories];
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = values.get(i);
//        }
//
//        entries.add(new BarEntry(0, arr));
//        return entries;
//    }


}

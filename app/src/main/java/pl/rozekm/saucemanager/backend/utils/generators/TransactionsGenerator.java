package pl.rozekm.saucemanager.backend.utils.generators;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;

public class TransactionsGenerator {

    private List<Transaction> transactions = new ArrayList<>();
    private Random random;

    public TransactionsGenerator(int generationPeriod) {
        generateTransactions(generationPeriod);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        random = new Random();
    }

    private void generateTransactions(int generationPeriod) {

        LocalDateTime today = LocalDateTime.now();
        Month thisMonth = today.getMonth();
        int thisMonthValue = thisMonth.getValue();

        ArrayList<Integer> months = new ArrayList<>();
        for (int i = thisMonthValue; i > thisMonthValue - generationPeriod; i--) {
            months.add(i);
        }

        for (int month : months) {
            generateAndAdd(TransactionCategory.OTHER, month);
            generateAndAdd(TransactionCategory.TRANSPORT, month);
            generateAndAdd(TransactionCategory.SPORT, month);
            generateAndAdd(TransactionCategory.HOUSE, month);
            generateAndAdd(TransactionCategory.HEALTH, month);
            generateAndAdd(TransactionCategory.FOOD, month);
            generateAndAdd(TransactionCategory.ENTERTAINMENT, month);
            generateAndAdd(TransactionCategory.CLOTHES, month);

            generateAndAdd(TransactionCategory.SAVINGS, month);
            generateAndAdd(TransactionCategory.SALARY, month);
            generateAndAdd(TransactionCategory.INVESTMENT, month);
        }
    }

    private void generateAndAdd(TransactionCategory category, int month) {
        switch (category) {
            case OTHER:
                randomTransactions(0.5, 10, 2, 300, category, month, TransactionType.OUTCOME);
                break;
            case TRANSPORT:
                randomTransactions(0.2, 2, 70, 200, category, month, TransactionType.OUTCOME);
                break;
            case SPORT:
                randomTransactions(0.3, 2, 30, 250, category, month, TransactionType.OUTCOME);
                break;
            case HOUSE:
                randomTransactions(0.15, 2, 350, 700, category, month, TransactionType.OUTCOME);
                break;
            case HEALTH:
                randomTransactions(0.2, 2, 250, 450, category, month, TransactionType.OUTCOME);
                break;
            case FOOD:
                randomTransactions(0.97, 12, 10, 35, category, month, TransactionType.OUTCOME);
                break;
            case ENTERTAINMENT:
                randomTransactions(0.7, 5, 35, 100, category, month, TransactionType.OUTCOME);
                break;
            case CLOTHES:
                randomTransactions(0.2, 2, 120, 650, category, month, TransactionType.OUTCOME);
                break;

            case SAVINGS:
                randomTransactions(1, 1, 3, 7, category, month, TransactionType.INCOME);
                break;
            case SALARY:
                randomTransactions(1, 1, 4000, 4500, category, month, TransactionType.INCOME);
                break;
            case INVESTMENT:
                randomTransactions(0.1, 2, 200, 1000, category, month, TransactionType.INCOME);
                break;
        }
    }

    private void randomTransactions(double probability, int frequency, double amountMin, double amountMax, TransactionCategory category, int month, TransactionType type) {
        Transaction transaction;
        for (int i = 0; i < frequency; i++) {
            if (random.nextDouble() < probability) {
                Double amount = amountMin + (amountMax - amountMin) * random.nextDouble();
                LocalDateTime date = LocalDateTime.of(2019, month, random.nextInt(27) + 1, random.nextInt(23), random.nextInt(55));
                String title = givenUsingPlainJavaWhenGeneratingRandomStringBoundedThenCorrect(random.nextInt(3) + 1, random.nextInt(7) + 1);

                transaction = new Transaction(date, amount, title, category, type);
                transactions.add(transaction);
            }
        }
    }

    private String givenUsingPlainJavaWhenGeneratingRandomStringBoundedThenCorrect(int numOfWords, int lengthOfWords) {
        StringBuilder result;

        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        StringBuilder buffer = new StringBuilder(lengthOfWords);
        for (int i = 0; i < 1; i++) {
            int randomLimitedInt = leftLimit + (random.nextInt() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        result = new StringBuilder(buffer.toString());

        int leftLimit3 = 97; // letter 'a'
        int rightLimit3 = 122; // letter 'z'
        StringBuilder buffer3 = new StringBuilder(lengthOfWords);
        for (int i = 0; i < lengthOfWords; i++) {
            int randomLimitedInt = leftLimit3 + (random.nextInt() * (rightLimit3 - leftLimit3 + 1));
            buffer3.append((char) randomLimitedInt);
        }
        String generatedString3 = buffer3.toString();
        result.append(generatedString3);

        for (int j = 0; j < numOfWords; j++) {
            int leftLimit2 = 97; // letter 'a'
            int rightLimit2 = 122; // letter 'z'
            StringBuilder buffer2 = new StringBuilder(lengthOfWords);
            for (int i = 0; i < lengthOfWords; i++) {
                int randomLimitedInt = leftLimit2 + (random.nextInt() * (rightLimit2 - leftLimit2 + 1));
                buffer2.append((char) randomLimitedInt);
            }
            String generatedString2 = buffer2.toString();
            result.append(" ").append(generatedString2);
        }
        return result.toString();
    }
}

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
    private int GENERATION_PERIOD;

    public TransactionsGenerator(int GENERATION_PERIOD) {
        this.GENERATION_PERIOD = GENERATION_PERIOD;
        generateTransactions(GENERATION_PERIOD);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getGENERATION_PERIOD() {
        return GENERATION_PERIOD;
    }

    public void setGENERATION_PERIOD(int GENERATION_PERIOD) {
        this.GENERATION_PERIOD = GENERATION_PERIOD;
    }

    private void generateTransactions(int GENERATION_PERIOD) {

        LocalDateTime today = LocalDateTime.now();
        Month this_month = today.getMonth();
        int this_month_value = this_month.getValue();


        ArrayList<Integer> months = new ArrayList<>();
        for (int i = this_month_value; i > this_month_value - GENERATION_PERIOD; i--) {
            months.add(i);
        }

        ArrayList<TransactionCategory> outcomeCategories = new ArrayList<>();
        outcomeCategories.add(TransactionCategory.OTHER);
        outcomeCategories.add(TransactionCategory.TRANSPORT);
        outcomeCategories.add(TransactionCategory.SPORT);
        outcomeCategories.add(TransactionCategory.HOUSE);
        outcomeCategories.add(TransactionCategory.HEALTH);
        outcomeCategories.add(TransactionCategory.FOOD);
        outcomeCategories.add(TransactionCategory.ENTERTAINMENT);
        outcomeCategories.add(TransactionCategory.CLOTHES);

        ArrayList<TransactionCategory> incomeCategories = new ArrayList<>();
        incomeCategories.add(TransactionCategory.SAVINGS);
        incomeCategories.add(TransactionCategory.SALARY);
        incomeCategories.add(TransactionCategory.INVESTMENT);

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
        Random random = new Random();

        Transaction transaction;

        for (int i = 0; i < frequency; i++) {
            if (random.nextDouble() < probability) {
                Double amount = amountMin + (amountMax - amountMin) * random.nextDouble();
                LocalDateTime date = LocalDateTime.of(2019, month, random.nextInt(27) + 1, random.nextInt(23), random.nextInt(55));
                String title = givenUsingPlainJava_whenGeneratingRandomStringBounded_thenCorrect(random.nextInt(4) + 1, random.nextInt(7) + 1);

                transaction = new Transaction(date, amount, title, category, type);
                System.out.println(transaction.toString());
                transactions.add(transaction);
            }
        }
    }

    public String givenUsingPlainJava_whenGeneratingRandomStringBounded_thenCorrect(int numOfWords, int lengthOfWords) {
        Random random = new Random();
        String result;

        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        StringBuilder buffer = new StringBuilder(lengthOfWords);
        for (int i = 0; i < 1; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        result = generatedString;

        int leftLimit3 = 97; // letter 'a'
        int rightLimit3 = 122; // letter 'z'
        StringBuilder buffer3 = new StringBuilder(lengthOfWords);
        for (int i = 0; i < lengthOfWords; i++) {
            int randomLimitedInt = leftLimit3 + (int)
                    (random.nextFloat() * (rightLimit3 - leftLimit3 + 1));
            buffer3.append((char) randomLimitedInt);
        }
        String generatedString3 = buffer3.toString();
        result = result + generatedString3;

        for (int j = 0; j < numOfWords; j++) {
            int leftLimit2 = 97; // letter 'a'
            int rightLimit2 = 122; // letter 'z'
            StringBuilder buffer2 = new StringBuilder(lengthOfWords);
            for (int i = 0; i < lengthOfWords; i++) {
                int randomLimitedInt = leftLimit2 + (int)
                        (random.nextFloat() * (rightLimit2 - leftLimit2 + 1));
                buffer2.append((char) randomLimitedInt);
            }
            String generatedString2 = buffer2.toString();
            result = result + " " + generatedString2;
        }
        return result;
    }
}

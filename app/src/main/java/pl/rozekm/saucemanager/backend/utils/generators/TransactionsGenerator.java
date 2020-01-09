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
        random = new Random();
        generateTransactions(generationPeriod);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    private void generateTransactions(int generationPeriod) {
        List<int[]> monthYears = generateMonthYears(generationPeriod);

        for (int[] monthYear : monthYears) {
            generateAndAdd(TransactionCategory.OTHER, monthYear);
            generateAndAdd(TransactionCategory.TRANSPORT, monthYear);
            generateAndAdd(TransactionCategory.SPORT, monthYear);
            generateAndAdd(TransactionCategory.HOUSE, monthYear);
            generateAndAdd(TransactionCategory.HEALTH, monthYear);
            generateAndAdd(TransactionCategory.FOOD, monthYear);
            generateAndAdd(TransactionCategory.ENTERTAINMENT, monthYear);
            generateAndAdd(TransactionCategory.CLOTHES, monthYear);

            generateAndAdd(TransactionCategory.SAVINGS, monthYear);
            generateAndAdd(TransactionCategory.SALARY, monthYear);
            generateAndAdd(TransactionCategory.INVESTMENT, monthYear);
        }
    }

    private List<int[]> generateMonthYears(int generationPeriod) {
        ArrayList<int[]> result = new ArrayList<>();

        LocalDateTime today = LocalDateTime.now();
        Month thisMonth = today.getMonth();
        int thisMonthValue = thisMonth.getValue();

        int[] firstMonthYear = new int[]{thisMonthValue, today.getYear()};
        result.add(firstMonthYear);

        for (int i = 0; i < generationPeriod - 1; i++) {
            result.add(nextMonthYear(result.get(i)));
        }
        return result;
    }

    private int[] nextMonthYear(int[] monthYear) {
        int[] result = {0, 0};

        int month = monthYear[0];
        int year = monthYear[1];

        if (month == 1) {
            month = 12;
            year = year - 1;
        }

        result[0] = month - 1;
        result[1] = year - 1;

        return result;
    }

    private void generateAndAdd(TransactionCategory category, int[] monthYear) {
        switch (category) {
            case OTHER:
                randomTransactions(0.5, 10, 2, 300, category, monthYear, TransactionType.OUTCOME);
                break;
            case TRANSPORT:
                randomTransactions(0.2, 2, 70, 200, category, monthYear, TransactionType.OUTCOME);
                break;
            case SPORT:
                randomTransactions(0.3, 2, 30, 250, category, monthYear, TransactionType.OUTCOME);
                break;
            case HOUSE:
                randomTransactions(0.15, 2, 350, 700, category, monthYear, TransactionType.OUTCOME);
                break;
            case HEALTH:
                randomTransactions(0.2, 2, 250, 450, category, monthYear, TransactionType.OUTCOME);
                break;
            case FOOD:
                randomTransactions(1, 12, 10, 35, category, monthYear, TransactionType.OUTCOME);
                break;
            case ENTERTAINMENT:
                randomTransactions(1, 5, 35, 100, category, monthYear, TransactionType.OUTCOME);
                break;
            case CLOTHES:
                randomTransactions(0.2, 2, 120, 650, category, monthYear, TransactionType.OUTCOME);
                break;

            case SAVINGS:
                randomTransactions(1, 1, 3, 7, category, monthYear, TransactionType.INCOME);
                break;
            case SALARY:
                randomTransactions(1, 1, 4000, 4500, category, monthYear, TransactionType.INCOME);
                break;
            case INVESTMENT:
                randomTransactions(0.1, 2, 200, 1000, category, monthYear, TransactionType.INCOME);
                break;
        }
    }

    private void randomTransactions(
            double probability,
            int frequency,
            double amountMin,
            double amountMax,
            TransactionCategory category,
            int[] monthYear,
            TransactionType type) {
        for (int i = 0; i < frequency; i++) {
            if (random.nextDouble() < probability) {
                Double amount = amountMin + (amountMax - amountMin) * random.nextDouble();
                LocalDateTime date = LocalDateTime.of(
                        monthYear[1],
                        monthYear[0],
                        maxDay(monthYear[0]),
                        random.nextInt(23),
                        random.nextInt(55));
                String title = generateTitles(
                        random.nextInt(3) + 1,
                        random.nextInt(7) + 1);
                transactions.add(new Transaction(date, amount, title, category, type));
            }
        }
    }

    private int maxDay(int i) {
        LocalDateTime now = LocalDateTime.now();
        int result = random.nextInt(27) + 1;

        if (i == now.getMonthValue() && result != 1) {
            return now.getDayOfMonth() - 1;
        }
        return result;
    }

    private String generateTitles(int numOfWords, int lengthOfWords) {
        StringBuilder result;

        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        StringBuilder buffer = new StringBuilder(lengthOfWords);
        for (int i = 0; i < 1; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        result = new StringBuilder(buffer.toString());

        int leftLimit3 = 97; // letter 'a'
        int rightLimit3 = 122; // letter 'z'
        StringBuilder buffer3 = new StringBuilder(lengthOfWords);
        for (int i = 0; i < lengthOfWords; i++) {
            int randomLimitedInt = leftLimit3 + (int) (random.nextFloat() * (rightLimit3 - leftLimit3 + 1));
            buffer3.append((char) randomLimitedInt);
        }
        String generatedString3 = buffer3.toString();
        result.append(generatedString3);

        for (int j = 0; j < numOfWords; j++) {
            int leftLimit2 = 97; // letter 'a'
            int rightLimit2 = 122; // letter 'z'
            StringBuilder buffer2 = new StringBuilder(lengthOfWords);
            for (int i = 0; i < lengthOfWords; i++) {
                int randomLimitedInt = leftLimit2 + (int) (random.nextFloat() * (rightLimit2 - leftLimit2 + 1));
                buffer2.append((char) randomLimitedInt);
            }
            String generatedString2 = buffer2.toString();
            result.append(" ").append(generatedString2);
        }
        return result.toString();
    }
}

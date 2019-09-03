package pl.rozekm.saucemanager.frontend.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionViewModel;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;

    @BindView(R.id.addTransactionImageButton)
    public Button addTransactionImageButton;

    @BindView(R.id.chart)
    public BarChart chart;

    @BindView(R.id.editTextAmount)
    TextInputLayout editTextAmount;

    @BindView(R.id.editTextTitle)
    TextInputLayout editTextTitle;

    ArrayList<BarEntry> barEntry = new ArrayList<>();

    BarDataSet barDataSet;

    BarData barData;

    Transaction classTransaction = new Transaction();

    int[] imageViewsOutcomeTransactions = new int[]{
            R.id.imageViewClothes,
            R.id.imageViewEntertaiment,
            R.id.imageViewFood,
            R.id.imageViewHealth,
            R.id.imageViewHouse,
            R.id.imageViewSport,
            R.id.imageViewTransport,
            R.id.imageViewOther
    };

    int[] imageViewsIncomeTransactions = new int[]{
            R.id.imageViewInvestments,
            R.id.imageViewSalary,
            R.id.imageViewSavings,
    };

    int[] outcomeColors = new int[]{
            Color.parseColor("#660000"),
            Color.parseColor("#CC0000"),
            Color.parseColor("#FF3333"),
            Color.parseColor("#FF9999"),
            Color.parseColor("#FFCC99"),
            Color.parseColor("#FF9933"),
            Color.parseColor("#CC6600"),
            Color.parseColor("#663300")
    };

    int[] incomeColors = new int[]{
            Color.parseColor("#B2FF66"),
            Color.parseColor("#80FF00"),
            Color.parseColor("#4C9900")
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        transactionViewModel = ViewModelProviders.of(this, new TransactionViewModelFactory(this.getApplication(), classTransaction)).get(TransactionViewModel.class);

        setChart(classTransaction.getType());

        applyChartSettings();

        addTransactionImageButton.setOnClickListener(view -> {
            if (validateAmount() && validateCategory()) {
                Transaction newTransaction = new Transaction(
                        Double.valueOf(editTextAmount.getEditText().getText().toString()),
                        classTransaction.getCategory(),
                        classTransaction.getType()
                );
                if (validateTitle()) {
                    newTransaction.setTitle(editTextTitle.getEditText().toString());
                }
                transactionViewModel.insert(newTransaction);
                System.out.println(newTransaction);
            }
        });
    }

    private boolean validateTitle() {
        return editTextTitle.getEditText().getText() != null;
    }

    private boolean validateCategory() {
        return classTransaction.getCategory() != null;
    }

    private boolean validateAmount() {
        return Objects.requireNonNull(editTextAmount.getEditText()).getText() != null;
    }

    private void applyChartSettings() {
        chart.setNoDataText("No data available");
        chart.animateY(850, Easing.Linear);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(false);
    }

    private ArrayList<BarEntry> addBarEntries(List<Transaction> transactions, TransactionType transactionType) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Float> values;

        Map<TransactionCategory, List<Transaction>> groupedTransactions =
                transactions.stream().collect(Collectors.groupingBy(Transaction::getCategory));

        values = processGroupedTransactions(groupedTransactions, transactionType);

        int numberOfCategories;

        if (transactionType == TransactionType.OUTCOME) {
            numberOfCategories = 8;
        } else {
            numberOfCategories = 3;
        }

        float[] arr = new float[numberOfCategories];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }

        entries.add(new BarEntry(0, arr));
        return entries;
    }

    private ArrayList<Float> processGroupedTransactions(
            Map<TransactionCategory, List<Transaction>> groupedTransactions,
            TransactionType transactionType) {

        ArrayList<Float> result = new ArrayList<>();
        ArrayList<TransactionCategory> transactionCategories = new ArrayList<>();

        if (transactionType == TransactionType.OUTCOME) {
            transactionCategories.add(TransactionCategory.OTHER);
            transactionCategories.add(TransactionCategory.TRANSPORT);
            transactionCategories.add(TransactionCategory.SPORT);
            transactionCategories.add(TransactionCategory.HOUSE);
            transactionCategories.add(TransactionCategory.HEALTH);
            transactionCategories.add(TransactionCategory.FOOD);
            transactionCategories.add(TransactionCategory.ENTERTAINMENT);
            transactionCategories.add(TransactionCategory.CLOTHES);
        } else {
            transactionCategories.add(TransactionCategory.SAVINGS);
            transactionCategories.add(TransactionCategory.SALARY);
            transactionCategories.add(TransactionCategory.INVESTMENT);
        }

        for (TransactionCategory transactionCategory : transactionCategories) {
            if (groupedTransactions.containsKey(transactionCategory)) {
                result.add(calculateSum(groupedTransactions.get(transactionCategory)));
            } else {
                result.add(0F);
            }
        }
        return result;
    }

    private Float calculateSum(List<Transaction> transactions) {
        Float sum = 0f;
        for (Transaction trans : transactions) {
            sum += Float.valueOf(String.valueOf(trans.getAmount()));
        }
        return sum;
    }

    public void transactionCategorySelected(View view) {
        switch (view.getId()) {
            case R.id.imageViewClothes:
                setFocus(R.id.imageViewClothes);
                break;

            case R.id.imageViewEntertaiment:
                setFocus(R.id.imageViewEntertaiment);
                break;

            case R.id.imageViewFood:
                setFocus(R.id.imageViewFood);
                break;

            case R.id.imageViewHealth:
                setFocus(R.id.imageViewHealth);
                break;

            case R.id.imageViewHouse:
                setFocus(R.id.imageViewHouse);
                break;

            case R.id.imageViewSport:
                setFocus(R.id.imageViewSport);
                break;

            case R.id.imageViewTransport:
                setFocus(R.id.imageViewTransport);
                break;

            case R.id.imageViewOther:
                setFocus(R.id.imageViewOther);
                break;

            case R.id.imageViewInvestments:
                setFocus(R.id.imageViewInvestments);
                break;

            case R.id.imageViewSalary:
                setFocus(R.id.imageViewSalary);
                break;

            case R.id.imageViewSavings:
                setFocus(R.id.imageViewSavings);
                break;
        }
    }

    private void setFocus(int imageView) {

        for (Integer imageViewsOutcomeTransaction : imageViewsOutcomeTransactions) {
            setLayoutWeightForView(imageViewsOutcomeTransaction, 1.0f);
        }
        for (Integer imageViewsIncomeTransaction : imageViewsIncomeTransactions) {
            setLayoutWeightForView(imageViewsIncomeTransaction, 1.0f);
        }
        setLayoutWeightForView(imageView, 3.0f);

        if (imageView == R.id.imageViewInvestments || imageView == R.id.imageViewSalary || imageView == R.id.imageViewSavings) {
            classTransaction.setType(TransactionType.INCOME);
            setChart(classTransaction.getType());
        } else {
            classTransaction.setType(TransactionType.OUTCOME);
            setChart(classTransaction.getType());
        }
    }

    private void setChart(TransactionType transactionType) {

        transactionViewModel.getAllTransactionsByTransactionType(transactionType.getCode()).observe(this, transactions -> {

            barEntry = addBarEntries(transactions, transactionType);

            barDataSet = new BarDataSet(barEntry, "Bar Set");
            if (classTransaction.getType() == TransactionType.OUTCOME)
                barDataSet.setColors(outcomeColors);
            else barDataSet.setColors(incomeColors);
            barDataSet.setDrawValues(false);

            barData = new BarData(barDataSet);
            barData.notifyDataChanged();
            chart.setData(barData);
            barDataSet.notifyDataSetChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
    }

    private void setLayoutWeightForView(int view, float weight) {
        ImageView image = findViewById(view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) image.getLayoutParams();
        params.weight = weight;
        image.setLayoutParams(params);
    }
}

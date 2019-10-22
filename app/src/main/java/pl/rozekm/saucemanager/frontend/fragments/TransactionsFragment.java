package pl.rozekm.saucemanager.frontend.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.frontend.utils.TransactionsAdapter;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModelFactory;

public class TransactionsFragment extends Fragment {

    private TransactionsViewModel transactionsViewModel;

    private ArrayList<BarEntry> barEntry = new ArrayList<>();

    private BarDataSet barDataSet;

    private BarData barData;

    private Transaction classTransaction = new Transaction();

    @BindView(R.id.addTransactionImageButton)
    public Button addTransactionImageButton;

    @BindView(R.id.chartOutcome)
    public BarChart chartOutcome;

    @BindView(R.id.chartIncome)
    public BarChart chartIncome;

    @BindView(R.id.editTextAmount)
    TextInputLayout editTextAmount;

    @BindView(R.id.editTextTitle)
    TextInputLayout editTextTitle;

    @BindView(R.id.textViewOutcomeChartTitle)
    TextView textViewOutcomeChartTitle;

    @BindView(R.id.textViewIncomeChartTitle)
    TextView textViewIncomeChartTitle;


    @BindView(R.id.imageViewClothes)
    ImageView imageViewClothes;

    @BindView(R.id.imageViewEntertaiment)
    ImageView imageViewEntertainment;

    @BindView(R.id.imageViewFood)
    ImageView imageViewFood;

    @BindView(R.id.imageViewHealth)
    ImageView imageViewHealth;

    @BindView(R.id.imageViewHouse)
    ImageView imageViewHouse;

    @BindView(R.id.imageViewSport)
    ImageView imageViewSport;

    @BindView(R.id.imageViewTransport)
    ImageView imageViewTransport;

    @BindView(R.id.imageViewOther)
    ImageView imageViewOther;

    @BindView(R.id.imageViewInvestments)
    ImageView imageViewInvestments;

    @BindView(R.id.imageViewSalary)
    ImageView imageViewSalary;

    @BindView(R.id.imageViewSavings)
    ImageView imageViewSavings;

    @BindView(R.id.transactionsRecyclerView)
    RecyclerView transactionsRecyclerView;

    private TransactionsAdapter transactionsAdapter;

    private int[] imageViewsOutcomeTransactions = new int[]{
            R.id.imageViewClothes,
            R.id.imageViewEntertaiment,
            R.id.imageViewFood,
            R.id.imageViewHealth,
            R.id.imageViewHouse,
            R.id.imageViewSport,
            R.id.imageViewTransport,
            R.id.imageViewOther
    };

    private int[] imageViewsIncomeTransactions = new int[]{
            R.id.imageViewInvestments,
            R.id.imageViewSalary,
            R.id.imageViewSavings,
    };

    private int[] outcomeColors = new int[]{
            Color.parseColor("#660000"),
            Color.parseColor("#CC0000"),
            Color.parseColor("#FF3333"),
            Color.parseColor("#FF9999"),
            Color.parseColor("#FFCC99"),
            Color.parseColor("#FF9933"),
            Color.parseColor("#CC6600"),
            Color.parseColor("#663300")
    };

    private int[] incomeColors = new int[]{
            Color.parseColor("#4C9900"),
            Color.parseColor("#80FF00"),
            Color.parseColor("#B2FF66")
    };


    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions_fragment, container, false);
        ButterKnife.bind(this, view);

        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getActivity().getApplication(), classTransaction)).get(TransactionsViewModel.class);

        setChart(chartOutcome, TransactionType.OUTCOME);
        setChart(chartIncome, TransactionType.INCOME);

        imageViewClothes.setOnClickListener(this::transactionCategorySelected);
        imageViewEntertainment.setOnClickListener(this::transactionCategorySelected);
        imageViewFood.setOnClickListener(this::transactionCategorySelected);
        imageViewHealth.setOnClickListener(this::transactionCategorySelected);
        imageViewHouse.setOnClickListener(this::transactionCategorySelected);
        imageViewSport.setOnClickListener(this::transactionCategorySelected);
        imageViewTransport.setOnClickListener(this::transactionCategorySelected);
        imageViewOther.setOnClickListener(this::transactionCategorySelected);
        imageViewInvestments.setOnClickListener(this::transactionCategorySelected);
        imageViewSalary.setOnClickListener(this::transactionCategorySelected);
        imageViewSavings.setOnClickListener(this::transactionCategorySelected);


        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        transactionsAdapter = new TransactionsAdapter();
        transactionsRecyclerView.setAdapter(transactionsAdapter);
        getRecentTransactions();


        return view;
    }

    private void getRecentTransactions(){
        transactionsViewModel.getLimitOutcomeTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
//        transactionsViewModel.getAllOutcomeTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                transactionsAdapter.setTransactions((ArrayList<Transaction>)transactions);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addTransactionImageButton.setOnClickListener(v -> {
            if (isAmountValid() && isCategoryValid()) {
                Transaction newTransaction = new Transaction(
                        Double.valueOf(editTextAmount.getEditText().getText().toString()),
                        classTransaction.getCategory(),
                        classTransaction.getType()
                );
                if (isTitleValid()) {
                    newTransaction.setTitle(editTextTitle.getEditText().toString());
                }
                transactionsViewModel.insert(newTransaction);
            }
        });
    }

    private boolean isTitleValid() {
        boolean result = false;
        if (editTextTitle.getEditText() != null) {
            result = !TextUtils.isEmpty(editTextTitle.getEditText().getText());
        }
        return result;
    }

    private boolean isCategoryValid() {
        return classTransaction.getCategory() != null;
    }

    private boolean isAmountValid() {
        boolean result = false;
        if (editTextAmount.getEditText() != null) {
            result = !TextUtils.isEmpty(editTextAmount.getEditText().getText());
        }
        return result;
    }

    private void applyChartSettings(BarChart chart) {
        chart.setNoDataText("No data available yet");
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

    private void transactionCategorySelected(View view) {
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
            default:
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
        } else {
            classTransaction.setType(TransactionType.OUTCOME);
        }
        classTransaction.setCategory(translateImageViewToTransactionCategory(imageView));
    }

    private TransactionCategory translateImageViewToTransactionCategory(int imageView) {

        TransactionCategory transactionCategory = null;

        switch (imageView) {
            case R.id.imageViewClothes:
                transactionCategory = TransactionCategory.CLOTHES;
                break;

            case R.id.imageViewEntertaiment:
                transactionCategory = TransactionCategory.ENTERTAINMENT;
                break;

            case R.id.imageViewFood:
                transactionCategory = TransactionCategory.FOOD;
                break;

            case R.id.imageViewHealth:
                transactionCategory = TransactionCategory.HEALTH;
                break;

            case R.id.imageViewHouse:
                transactionCategory = TransactionCategory.HOUSE;
                break;

            case R.id.imageViewSport:
                transactionCategory = TransactionCategory.SPORT;
                break;

            case R.id.imageViewTransport:
                transactionCategory = TransactionCategory.TRANSPORT;
                break;

            case R.id.imageViewOther:
                transactionCategory = TransactionCategory.OTHER;
                break;

            case R.id.imageViewInvestments:
                transactionCategory = TransactionCategory.INVESTMENT;
                break;

            case R.id.imageViewSalary:
                transactionCategory = TransactionCategory.SALARY;
                break;

            case R.id.imageViewSavings:
                transactionCategory = TransactionCategory.SAVINGS;
                break;
            default:
                break;
        }
        return transactionCategory;
    }

    private void setChart(BarChart chart, TransactionType transactionType) {

        if (transactionType == TransactionType.OUTCOME) {
            transactionsViewModel.getAllOutcomeTransactions().observe(this, transactions -> setTypedChart(chart, transactionType, transactions));
        } else {
            transactionsViewModel.getAllIncomeTransactions().observe(this, transactions -> setTypedChart(chart, transactionType, transactions));
        }
        applyChartSettings(chart);
    }

    private void setTypedChart(BarChart chart, TransactionType transactionType, List<Transaction> transactions) {
        barEntry = addBarEntries(transactions, transactionType);
        barDataSet = new BarDataSet(barEntry, "Bar Set");
        if (transactionType == TransactionType.OUTCOME) {
            barDataSet.setColors(outcomeColors);
        } else {
            barDataSet.setColors(incomeColors);
        }
        barDataSet.setDrawValues(false);
        barData = new BarData(barDataSet);
        barData.notifyDataChanged();
        chart.setData(barData);
        barDataSet.notifyDataSetChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void setLayoutWeightForView(int view, float weight) {
        ImageView image = getView().findViewById(view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) image.getLayoutParams();
        params.weight = weight;
        image.setLayoutParams(params);
    }
}

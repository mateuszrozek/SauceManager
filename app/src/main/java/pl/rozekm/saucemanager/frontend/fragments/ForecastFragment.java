package pl.rozekm.saucemanager.frontend.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModelFactory;
import pl.rozekm.saucemanager.databinding.ForecastFragmentBinding;
import pl.rozekm.saucemanager.frontend.utils.CategoriesConverter;
import pl.rozekm.saucemanager.frontend.utils.Forecast;

public class ForecastFragment extends Fragment {

    private TableLayout tableLayout;
    private TransactionsViewModel transactionsViewModel;
    private CategoriesConverter categoriesConverter = new CategoriesConverter();

    @BindView(R.id.textViewNOW)
    TextView textViewNOW;

    @BindView(R.id.textViewFUT)
    TextView textViewFUT;

    @BindView(R.id.forecastButton)
    Button forecastButton;

    @BindView(R.id.forecastSpinner)
    Spinner forecastSpinner;

    private List<Transaction> allTransactions = new ArrayList<>();
    private List<Transaction> outcomeTransactions = new ArrayList<>();

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getActivity().getApplication(), new Transaction())).get(TransactionsViewModel.class);
    }

    private void fillTableLayout(List<Transaction> transactions) {
        tableLayout.removeAllViews();
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(2, 2, 2, 2);
        TableRow row;
        TextView t1;
        TextView t2;
        TextView t3;
        TextView t4;

        row = new TableRow(getActivity());
        t1 = new TextView(getActivity());
        t2 = new TextView(getActivity());
        t3 = new TextView(getActivity());
        t4 = new TextView(getActivity());
        t1.setText(R.string.kategoria);
        t2.setText(R.string.zazwyczaj);
        t3.setText(R.string.do_teraz);
        t4.setText(R.string.pozostalo);
        t1.setTextSize(16);
        t2.setTextSize(16);
        t3.setTextSize(16);
        t4.setTextSize(16);
        t1.setPadding(10, 4, 4, 4);
        t2.setPadding(4, 4, 4, 4);
        t3.setPadding(4, 4, 4, 4);
        t4.setPadding(4, 4, 10, 4);
        t1.setGravity(Gravity.CENTER_HORIZONTAL);
        t2.setGravity(Gravity.CENTER_HORIZONTAL);
        t3.setGravity(Gravity.CENTER_HORIZONTAL);
        t4.setGravity(Gravity.CENTER_HORIZONTAL);
        t1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        t2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        t3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        t4.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        t1.setLayoutParams(params);
        t2.setLayoutParams(params);
        t3.setLayoutParams(params);
        t4.setLayoutParams(params);

        row.addView(t1);
        row.addView(t2);
        row.addView(t3);
        row.addView(t4);
        styleHeader(row);
        tableLayout.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ArrayList<Forecast> forecasts = processTransactions(transactions);

        for (Forecast forecast : forecasts) {
            row = new TableRow(getActivity());
            t1 = new TextView(getActivity());
            t2 = new TextView(getActivity());
            t3 = new TextView(getActivity());
            t4 = new TextView(getActivity());
            t1.setText(categoriesConverter.enumToString(forecast.getCategory()));
            t2.setText(String.format(Locale.forLanguageTag("PL"), "%.2f", forecast.getTypically()));
            t3.setText(String.format(Locale.forLanguageTag("PL"), "%.2f", forecast.getSoFar()));
            t4.setText(String.format(Locale.forLanguageTag("PL"), "%.2f", forecast.getRemaining()));
            t1.setTextSize(16);
            t2.setTextSize(16);
            t3.setTextSize(16);
            t4.setTextSize(16);
            t1.setPadding(10, 4, 4, 4);
            t2.setPadding(4, 4, 4, 4);
            t3.setPadding(4, 4, 4, 4);
            t4.setPadding(4, 4, 10, 4);
            t1.setBackgroundColor(getResources().getColor(R.color.colorButtonBackground));
            t2.setBackgroundColor(getResources().getColor(R.color.colorButtonBackground));
            t3.setBackgroundColor(getResources().getColor(R.color.colorButtonBackground));
            t4.setBackgroundColor(getResources().getColor(R.color.colorButtonBackground));
            t1.setGravity(Gravity.START);
            t2.setGravity(Gravity.END);
            t3.setGravity(Gravity.END);
            t4.setGravity(Gravity.END);
            t1.setLayoutParams(params);
            t2.setLayoutParams(params);
            t3.setLayoutParams(params);
            t4.setLayoutParams(params);

            row.addView(t1);
            row.addView(t2);
            row.addView(t3);
            row.addView(t4);

            styleRow(row);

            tableLayout.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void styleHeader(TableRow row) {
        row.setPadding(5, 5, 5, 5);
        row.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
    }

    private void styleRow(TableRow row) {
        row.setPadding(5, 5, 5, 5);
        row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    private ArrayList<Forecast> processTransactions(List<Transaction> transactions) {
        ArrayList<Forecast> forecasts = new ArrayList<>();

        Map<TransactionCategory, List<Transaction>> transactionsGroupedByCat =
                transactions.stream().collect(Collectors.groupingBy(Transaction::getCategory));

        List<List<Transaction>> transactionsGroupedByCatList = new ArrayList<>(transactionsGroupedByCat.values());

        for (List<Transaction> transactionsByCat : transactionsGroupedByCatList) {

            Map<Month, List<Transaction>> transactionsGroupedByCatAndMonths =
                    transactionsByCat.stream().collect(Collectors.groupingBy(Transaction::getMonth));

            List<List<Transaction>> transactionsGroupedByCatAndMon = new ArrayList<>(transactionsGroupedByCatAndMonths.values());

            ArrayList<Double> sumForEachMonth = new ArrayList<>();
            Double soFar = 0.0;
            Double totalSum = 0.0;
            Double typically = 0.0;
            Double remaining = 0.0;

            for (List<Transaction> transactionsInMonth : transactionsGroupedByCatAndMon) {

                double sum = 0.0;
                double thisMonth = 0.0;

                boolean previousMonth = false;
                LocalDateTime localDateTime = LocalDateTime.now();
                for (Transaction transaction : transactionsInMonth) {
                    if (transaction.getMonth() != localDateTime.getMonth()) {
                        sum = sum + transaction.getAmount();
                        previousMonth = true;
                    } else {
                        thisMonth = thisMonth + transaction.getAmount();
                        previousMonth = false;
                    }
                }
                if (!previousMonth) {
                    soFar = thisMonth;
                }
                if (previousMonth) {
                    sumForEachMonth.add(sum);
                }
            }
            for (Double monthlyOutcome : sumForEachMonth) {
                totalSum = totalSum + monthlyOutcome;
            }
            typically = totalSum / sumForEachMonth.size();
            remaining = typically - soFar;

            Forecast forecast = new Forecast(transactionsByCat.get(0).getCategory(), typically, soFar, remaining);
            forecasts.add(forecast);
        }
        return forecasts;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ForecastFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.forecast_fragment, container, false);
        View view = binding.getRoot();

        ButterKnife.bind(this, view);
        tableLayout = view.findViewById(R.id.tableLayout);
        textViewNOW.setText(convertFloatToCash(StatisticsFragment.INITIAL_VALUE));

        ArrayList<String> spinnerArray = new ArrayList<>(Arrays.asList("1 miesiąc", "2 miesiące", "3 miesiące", "4 miesiące"));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        forecastSpinner.setAdapter(spinnerArrayAdapter);

        forecastButton.setOnClickListener(v -> calculateFutureAccount(allTransactions, getMonthOfForecast()));

        transactionsViewModel.getAllTransactions().observe(
                ForecastFragment.this,
                transactions -> {
                    allTransactions = transactions;
                    transactions.stream()
                            .filter(t -> t.getType() == TransactionType.OUTCOME)
                            .forEach(outcomeTransactions::add);
                    fillTableLayout(outcomeTransactions);
                    calculateFutureAccount(allTransactions, getMonthOfForecast());
                });
        return view;
    }

    private float getMonthOfForecast() {
        char c = (forecastSpinner.getSelectedItem().toString().charAt(0));
        int val = Integer.parseInt(String.valueOf(c));
        return (float) val;
    }

    private void calculateFutureAccount(List<Transaction> transactions, float month) {
        float init = StatisticsFragment.INITIAL_VALUE;

        Map<Month, List<Transaction>> groupedByMonth = transactions.stream().collect(Collectors.groupingBy(t -> t.getDate().getMonth()));
        int months = groupedByMonth.size();

        LocalDateTime now = LocalDateTime.now();
        ArrayList<Transaction> transesWithoutCurrentMonth = new ArrayList<>();
        ArrayList<Transaction> transesCurrentMonth = new ArrayList<>();
        transactions.stream().filter(t -> t.getDate().getMonth() != now.getMonth()).forEach(transesWithoutCurrentMonth::add);
        transactions.stream().filter(t -> t.getDate().getMonth() == now.getMonth()).forEach(transesCurrentMonth::add);

        float sumWithoutCurrentMonth = getSum(init, transesWithoutCurrentMonth);
        float averageWithoutCurrentMonth = sumWithoutCurrentMonth / months - 1;
        float sumCurrentMonth = getSum(sumWithoutCurrentMonth, transesCurrentMonth);
        float stateCurrentMonth = sumWithoutCurrentMonth + sumCurrentMonth;

        textViewNOW.setText(convertFloatToCash(stateCurrentMonth));
        textViewFUT.setText(convertFloatToCash(stateCurrentMonth + (averageWithoutCurrentMonth * month)));
    }

    private float getSum(float startValue, ArrayList<Transaction> transactions) {

        float sum = 0.0f;

        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.OUTCOME) {
                sum = sum - (float) ((double) transaction.getAmount());
            } else {
                sum = sum + (float) ((double) transaction.getAmount());
            }
        }
        return sum;
    }

    private String convertFloatToCash(float value) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(value) + " " + "zł";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

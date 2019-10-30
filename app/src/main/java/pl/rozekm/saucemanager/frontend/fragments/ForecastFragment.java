package pl.rozekm.saucemanager.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.databinding.ForecastFragmentBinding;
import pl.rozekm.saucemanager.frontend.utils.Forecast;
import pl.rozekm.saucemanager.frontend.viewmodels.ForecastViewModel;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModelFactory;

public class ForecastFragment extends Fragment {

    private ForecastViewModel mViewModel;
    private TableLayout tableLayout;
    private TransactionsViewModel transactionsViewModel;

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getActivity().getApplication(), new Transaction())).get(TransactionsViewModel.class);
        transactionsViewModel.getAllOutcomeTransactions().observe(ForecastFragment.this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                fillTableLayout(transactions);
            }
        });
    }

    private void fillTableLayout(List<Transaction> transactions) {

        if (transactions.isEmpty()) {
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

            t1.setText("Category");
            t2.setText("Typically");
            t3.setText("So far");
            t4.setText("Remaining");

            t1.setTextSize(20);
            t2.setTextSize(20);
            t3.setTextSize(20);
            t4.setTextSize(20);

            t1.setPadding(2, 2, 2, 2);
            t2.setPadding(2, 2, 2, 2);
            t3.setPadding(2, 2, 2, 2);
            t4.setPadding(2, 2, 2, 2);

            t1.setGravity(2);
            t2.setGravity(3);
            t3.setGravity(3);
            t4.setGravity(3);

            t1.setTextColor(getResources().getColor(R.color.sauceColor));
            t2.setTextColor(getResources().getColor(R.color.sauceColor));
            t3.setTextColor(getResources().getColor(R.color.sauceColor));
            t4.setTextColor(getResources().getColor(R.color.sauceColor));

            row.addView(t1);
            row.addView(t2);
            row.addView(t3);
            row.addView(t4);

            styleHeader(row);

            tableLayout.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        } else {
            ArrayList<Forecast> forecasts = processTransactions(transactions);
            for (Forecast forecast : forecasts) {
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

                t1.setText(forecast.getCategory().toString());
                t2.setText(String.format(Locale.forLanguageTag("PL"), "%.2f", forecast.getTypically()));
                t3.setText(String.format(Locale.forLanguageTag("PL"), "%.2f", forecast.getSoFar()));
                t4.setText(String.format(Locale.forLanguageTag("PL"), "%.2f", forecast.getRemaining()));

                t1.setTextSize(16);
                t2.setTextSize(16);
                t3.setTextSize(16);
                t4.setTextSize(16);

                t1.setPadding(0, 2, 2, 2);
                t2.setPadding(2, 2, 2, 2);
                t3.setPadding(2, 2, 2, 2);
                t4.setPadding(2, 2, 2, 2);

                t1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                t2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                t3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                t4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

                t1.setGravity(2);
                t2.setGravity(5);
                t3.setGravity(5);
                t4.setGravity(5);

                row.addView(t1);
                row.addView(t2);
                row.addView(t3);
                row.addView(t4);

                styleRow(row);

                tableLayout.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            }
        }
    }

    private void styleHeader(TableRow row) {
        row.setPadding(5, 5, 5, 5);
        row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
        fillTableLayout(new ArrayList<>());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
        // TODO: Use the ViewModel
    }

}

package pl.rozekm.saucemanager.frontend.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionViewModel;

public class MainActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;

    @BindView(R.id.addTransactionImageButton)
    public ImageButton addTransactionImageButton;


    @BindView(R.id.chart)
    public BarChart chart;

    ArrayList<BarEntry> barEntry = new ArrayList<>();

    BarDataSet barDataSet;

    BarData barData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int[] colors = new int[]{Color.BLUE, Color.CYAN, Color.RED};

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.getAllTransactions().observe(this, transactions -> {

            barEntry = addBarEntries(transactions);

            barDataSet = new BarDataSet(barEntry, "Bar Set");
            barDataSet.setColors(colors);
            barDataSet.setDrawValues(false);

            barData = new BarData(barDataSet);
            barData.notifyDataChanged();
            chart.setData(barData);
            barDataSet.notifyDataSetChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });

        initializeBarChart();

        addTransactionImageButton.setOnClickListener(view -> {
            Transaction transaction = new Transaction(21.2, LocalDateTime.now(), TransactionCategory.CLOTHES, TransactionType.INCOME);
            transactionViewModel.insert(transaction);
            System.out.println("Added!!");
        });
    }

    private void initializeBarChart() {

        applySettings();
    }

    private void applySettings() {
        chart.setNoDataText("No data available");
        chart.animateY(1800, Easing.Linear);
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

    private ArrayList<BarEntry> addBarEntries(List<Transaction> transactions) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Float> values = new ArrayList<>();
        transactions.forEach(transaction -> values.add(Float.valueOf(String.valueOf(transaction.getAmount()))));

        float[] arr = new float[values.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }

        entries.add(new BarEntry(0, arr));
        return entries;
    }
}

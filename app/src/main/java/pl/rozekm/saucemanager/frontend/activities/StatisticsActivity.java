package pl.rozekm.saucemanager.frontend.activities;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.frontend.utils.TransactionsSorter;
import pl.rozekm.saucemanager.frontend.utils.charts.MyMarkerView;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModelFactory;

public class StatisticsActivity extends AppCompatActivity {

    @BindView(R.id.buttonBackStats)
    Button buttonBackStats;

    @BindView(R.id.pieChartOutcomes)
    PieChart pieChartOutcomes;

    @BindView(R.id.pieChartIncomes)
    PieChart pieChartIncomes;

    @BindView(R.id.lineChartAccount)
    LineChart lineChartAccount;

    @BindView(R.id.barChartCashFlow)
    BarChart barChartCashFlow;

//    @BindView(R.id.tableLayoutOutcomes)
//    TableLayout tableLayoutOutcomes;

    @BindView(R.id.pieChartRadioButtonDay)
    RadioButton pieChartRadioButtonDay;

    @BindView(R.id.pieChartRadioButtonWeek)
    RadioButton pieChartRadioButtonWeek;

    @BindView(R.id.pieChartRadioButtonMonth)
    RadioButton pieChartRadioButtonMonth;

    @BindView(R.id.pieChartRadioButtonYear)
    RadioButton pieChartRadioButtonYear;

    List<Transaction> allTransactions = new ArrayList<>();
    private TransactionsViewModel transactionsViewModel;

    private TransactionsSorter transactionsSorter;

    private List<Float> accountStates;

    public static float INITIAL_VALUE = 1452.78f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);
        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getApplication(), new Transaction())).get(TransactionsViewModel.class);

        buttonBackStats.setOnClickListener(v -> onBackPressed());

        transactionsViewModel.getAllTransactions().observe(this, transactions -> {

            allTransactions = transactions;
            transactionsSorter = new TransactionsSorter(allTransactions);
            accountStates = transactionsSorter.accountState(transactions, INITIAL_VALUE);

            setPolylinePieChart(pieChartOutcomes, TransactionType.OUTCOME, Frequency.YEARLY);
            setPolylinePieChart(pieChartIncomes, TransactionType.INCOME, Frequency.YEARLY);
            setLineChart(lineChartAccount);
            setBarChart(barChartCashFlow);
        });
    }

    public void onRadioButtonClickedStats(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.pieChartRadioButtonDay:
                if (checked) {
                    setPolylinePieChart(pieChartOutcomes, TransactionType.OUTCOME, Frequency.DAILY);
                    setPolylinePieChart(pieChartIncomes, TransactionType.INCOME, Frequency.DAILY);
                }
                break;
            case R.id.pieChartRadioButtonWeek:
                if (checked) {
                    setPolylinePieChart(pieChartOutcomes, TransactionType.OUTCOME, Frequency.WEEKLY);
                    setPolylinePieChart(pieChartIncomes, TransactionType.INCOME, Frequency.WEEKLY);
                }
                break;
            case R.id.pieChartRadioButtonMonth:
                if (checked) {
                    setPolylinePieChart(pieChartOutcomes, TransactionType.OUTCOME, Frequency.MONTHLY);
                    setPolylinePieChart(pieChartIncomes, TransactionType.INCOME, Frequency.MONTHLY);
                }
                break;
            case R.id.pieChartRadioButtonYear:
                if (checked) {
                    setPolylinePieChart(pieChartOutcomes, TransactionType.OUTCOME, Frequency.YEARLY);
                    setPolylinePieChart(pieChartIncomes, TransactionType.INCOME, Frequency.YEARLY);
                }
                break;
        }
    }

    private void setBarChart(BarChart chart) {
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);

        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setHighlightFullBarEnabled(false);

        chart.getAxisLeft().setEnabled(false);

        List<float[]> cashFlowByMonths = transactionsSorter.cashFlow(allTransactions);

        Float minimum = transactionsSorter.getMinimum(cashFlowByMonths);
        Float maximum = transactionsSorter.getMaximum(cashFlowByMonths);

        chart.getAxisRight().setAxisMaximum(maximum * 1.2f);
        chart.getAxisRight().setAxisMinimum(minimum - (Math.abs(minimum) * 0.2f));

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawZeroLine(true);
        chart.getAxisRight().setLabelCount(7, false);
        chart.getAxisRight().setValueFormatter(new CustomFormatter());
        chart.getAxisRight().setTextSize(9f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(9f);

        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(cashFlowByMonths.size());

        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelCount(cashFlowByMonths.size());
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {

                int size = cashFlowByMonths.size();

                float monthValue = value + (float) (size - 1);

                return Month.of((int) monthValue).toString();
            }
        });

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 1; i < cashFlowByMonths.size(); i++) {
            values.add(new BarEntry(i, cashFlowByMonths.get(i)));
        }

        BarDataSet set = new BarDataSet(values, "Cash flow through the months");
        set.setDrawIcons(false);
        set.setValueFormatter(new CustomFormatter());
        set.setValueTextSize(7f);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 200, 0));
        set.setStackLabels(new String[]{
                "Outcomes", "Incomes"
        });

        BarData data = new BarData(set);
        data.setBarWidth(0.75f);
        chart.setData(data);
        chart.invalidate();
    }

    private void setLineChart(LineChart chart) {
        chart.setBackgroundColor(getResources().getColor(R.color.colorScreenBackground));
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(chart);
        chart.setMarker(mv);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        XAxis xAxis;
        xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.DAYS.toMillis((long) (value + 100));
                return mFormat.format(new Date(millis));
            }
        });

        YAxis yAxis;
        yAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setAxisMaximum(Collections.max(accountStates) * 1.1f);
        yAxis.setAxisMinimum(Collections.min(accountStates) - (Math.abs(Collections.min(accountStates)) * 0.1f));

        LimitLine llXAxis = new LimitLine(9f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        LimitLine ll1 = new LimitLine(Collections.max(accountStates), "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(Collections.min(accountStates), "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        yAxis.setDrawLimitLinesBehindData(true);
        xAxis.setDrawLimitLinesBehindData(true);
        yAxis.addLimitLine(ll1);
        yAxis.addLimitLine(ll2);
        setLineChartData(allTransactions, chart);
        chart.animateX(1500);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setLineChartData(List<Transaction> transactions, LineChart chart) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < accountStates.size(); i++) {
            values.add(new Entry(i, accountStates.get(i)));
        }

        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Balanse of the account");
            set1.setDrawIcons(false);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(0.5f);
            set1.setCircleRadius(0.5f);
            set1.setDrawCircles(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setValueTextSize(9f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });
            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            chart.setData(data);
        }
    }

    private void setPolylinePieChart(PieChart pieChart, TransactionType type, Frequency frequency) {
        setPolylinePieChartProperties(pieChart, type);
        setPolylinePieChartData(pieChart, type, frequency);
    }

    private void setPolylinePieChartData(PieChart chart, TransactionType type, Frequency frequency) {
        List<Transaction> transactions = transactionsSorter.sortByType(allTransactions, type);
        ArrayList<PieEntry> entries = new ArrayList<>();

        List<Transaction> transactionsByFrequency = transactionsSorter.sortByFrequency(transactions, frequency);

        Map<TransactionCategory, Float> valueOfEachCategory = transactionsSorter.valuesOfEachCategory(transactionsByFrequency);

        for (Map.Entry<TransactionCategory, Float> entry : valueOfEachCategory.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "List of operations");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

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
                Color.parseColor("#4C9900"),
                Color.parseColor("#80FF00"),
                Color.parseColor("#B2FF66")
        };

        ArrayList<Integer> colors = new ArrayList<>();
        if (type == TransactionType.OUTCOME) {
            for (int i : outcomeColors) {
                colors.add(i);
            }
        } else {
            for (int i : incomeColors) {
                colors.add(i);
            }
        }

        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        chart.setData(data);

        chart.highlightValues(null);

        chart.invalidate();

    }

    private void setPolylinePieChartProperties(PieChart chart, TransactionType type) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterText(generateCenterSpannableText(type));

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(getResources().getColor(R.color.colorScreenBackground));

        chart.setEntryLabelColor(getResources().getColor(R.color.colorPrimaryDark));
        chart.setEntryLabelTextSize(13f);


        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(48f);
        chart.setTransparentCircleRadius(53f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(800, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private SpannableString generateCenterSpannableText(TransactionType type) {
        String text;
        if (type == TransactionType.OUTCOME) {
            text = "Outcomes";
        } else {
            text = "Incomes";
        }
        SpannableString s = new SpannableString(text);

        s.setSpan(new RelativeSizeSpan(2f), 0, text.length(), 0);

        return s;
    }

    private class CustomFormatter extends ValueFormatter {

        private final DecimalFormat mFormat;

        CustomFormatter() {
            mFormat = new DecimalFormat("###");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(Math.abs(value)) + " zł";
        }
    }
}

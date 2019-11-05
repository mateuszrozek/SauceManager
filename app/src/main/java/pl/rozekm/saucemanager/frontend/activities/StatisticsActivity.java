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
import android.widget.TableLayout;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @BindView(R.id.tabularStatistics)
    TableLayout tabularStatistics;


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
            accountStates = transactionsSorter.accountState(transactions, 1452f);

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

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setHighlightFullBarEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setAxisMaximum(25f);
        chart.getAxisRight().setAxisMinimum(-25f);
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

        //TODO
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(110f);

        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(12);
        xAxis.setGranularity(10f);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final DecimalFormat format = new DecimalFormat("###");

            @Override
            public String getFormattedValue(float value) {
                return format.format(value) + "-" + format.format(value + 10);
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

        // IMPORTANT: When using negative values in stacked bars, always make sure the negative values are in the array first
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(5, new float[]{-10, 10}));
        values.add(new BarEntry(15, new float[]{-12, 13}));
        values.add(new BarEntry(25, new float[]{-15, 15}));
        values.add(new BarEntry(35, new float[]{-17, 17}));
        values.add(new BarEntry(45, new float[]{-19, 20}));
        values.add(new BarEntry(45, new float[]{-19, 20}));
        values.add(new BarEntry(55, new float[]{-19, 19}));
        values.add(new BarEntry(65, new float[]{-16, 16}));
        values.add(new BarEntry(75, new float[]{-13, 14}));
        values.add(new BarEntry(85, new float[]{-10, 11}));
        values.add(new BarEntry(95, new float[]{-5, 6}));
        values.add(new BarEntry(105, new float[]{-1, 2}));

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
        data.setBarWidth(8.5f);
        chart.setData(data);
        chart.invalidate();
    }

    private void setLineChart(LineChart chart) {

        {   // // Chart Style // //

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(chart);
            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range

            //TODO coś ze skalowaniem zrobić
            yAxis.setAxisMaximum(Collections.max(accountStates) * 1.1f);

            yAxis.setAxisMinimum(Collections.min(accountStates) - (Math.abs(Collections.min(accountStates)) * 0.1f));
        }


        {   // // Create Limit Lines // //
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

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }

        // add data
//        setLineChartData(45, 180, chart);
        setLineChartData(allTransactions, chart);


        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setLineChartData(List<Transaction> transactions, LineChart chart) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < accountStates.size(); i++) {
            values.add(new Entry(i, accountStates.get(i)));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
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
//        Map<TransactionCategory, Float> valueOfEachCategory = transactionsSorter.valuesOfEachCategoryForFrequency(transactions, frequency);

        for (Map.Entry<TransactionCategory, Float> entry : valueOfEachCategory.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "List of operations");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        ArrayList<Integer> colors = new ArrayList<>();

        //TODO colors
//        if (type == TransactionType.OUTCOME){
//
//        }
//        else {
//
//        }

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);

        // undo all highlights
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
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

//        // add a selection listener
//        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

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

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }

    private class CustomFormatter extends ValueFormatter {

        private final DecimalFormat mFormat;

        CustomFormatter() {
            mFormat = new DecimalFormat("###");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(Math.abs(value)) + "m";
        }
    }
}

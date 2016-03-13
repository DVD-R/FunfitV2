package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/6/2016.
 */
public class MonthlyGraphActivity extends AppCompatActivity {
    private static final String LOG_TAG = MonthlyGraphActivity.class.getSimpleName();
    @Bind(R.id.text_month)
    TextView mTextMonth;
    @Bind(R.id.text_year)
    TextView mTextYear;
    @Bind(R.id.text_cal_consumed)
    TextView mTextCalConsumed;
    @Bind(R.id.text_cal_burned)
    TextView mTextCalBurned;
    @Bind(R.id.layout_monthly)
    RelativeLayout mLayoutMonthly;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.graph)
    GraphView mGraph;
    @Bind(R.id.text_rdi)
    TextView mTextRdi;

    boolean isFirst;
    String month;
    int year;
    double calConsumed, calBurned, rdi;
    private int[] monthlyConsumedDay, monthlyBurnedDay;
    private double[] monthlyConsumedValue, monthlyBurnedValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_graph);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent i = getIntent();
        isFirst = i.getBooleanExtra(Constants.IS_FIRST, false);
        month = i.getStringExtra(Constants.MONTH);
        year = i.getIntExtra(Constants.YEAR, 0);
        rdi = i.getDoubleExtra(Constants.RDI_LAPSE, 0);
        calConsumed = i.getDoubleExtra(Constants.CAL_CONSUMED, 0);
        calBurned = i.getDoubleExtra(Constants.CAL_BURNED, 0);
        monthlyConsumedDay = i.getIntArrayExtra(Constants.CONSUMED_TIME);
        monthlyBurnedDay = i.getIntArrayExtra(Constants.BURNED_TIME);
        monthlyConsumedValue = i.getDoubleArrayExtra(Constants.CONSUMED_VALUE);
        monthlyBurnedValue = i.getDoubleArrayExtra(Constants.BURNED_VALUE);

        mTextMonth.setTextColor(Color.parseColor("#FFFFFF"));
        mTextYear.setTextColor(Color.parseColor("#FFFFFF"));
        mTextCalConsumed.setTextColor(Color.parseColor("#FFFFFF"));
        mTextCalBurned.setTextColor(Color.parseColor("#FFFFFF"));
        mTextRdi.setTextColor(Color.parseColor("#FFFFFF"));
        if (Utils.getCluster(this).equals("velocity"))
            mLayoutMonthly.setBackgroundColor(Color.parseColor("#2980b9"));
        else
            mLayoutMonthly.setBackgroundColor(Color.parseColor("#c0392b"));

        mTextMonth.setText(month);
        mTextYear.setText(year + "");
        mTextCalConsumed.setText(calConsumed + "");
        mTextCalBurned.setText(calBurned + "");

        mTextCalConsumed.setText("Calories Consumed this week: " + calConsumed);
        mTextCalBurned.setText("Calories Burned this week: " + calBurned);
        if (rdi > calConsumed) {
            mTextRdi.setText(Utils.roundOneDecimal(rdi - calConsumed) +
                    " Calories lacking");
        } else {
            mTextRdi.setText(Math.abs(Utils.roundOneDecimal(rdi - calConsumed)) +
                    " Calories exceeded");
        }

        String[] dateRange = getMonthDateRanges(month, year);

        List<DataPoint> consumedPts = new ArrayList<>();
        List<DataPoint> burnedPts = new ArrayList<>();
        for (int x = 0; x < dateRange.length; x++) {
            double consumed = getCalories(monthlyConsumedDay,
                    monthlyConsumedValue, dateRange[x], year);
            consumedPts.add(new DataPoint(x, consumed));

            double burned = getCalories(monthlyBurnedDay,
                    monthlyBurnedValue, dateRange[x], year);
            burnedPts.add(new DataPoint(x, burned));
        }

        LineGraphSeries<DataPoint> consumedSeries = new LineGraphSeries<DataPoint>(consumedPts.toArray(new DataPoint[0]));
        LineGraphSeries<DataPoint> burnedSeries = new LineGraphSeries<DataPoint>(burnedPts.toArray(new DataPoint[0]));
        consumedSeries.setColor(Color.parseColor("#2980b9"));
        consumedSeries.setTitle("Calories Consumed");
        burnedSeries.setColor(Color.parseColor("#c0392b"));
        burnedSeries.setTitle("Calories Burned");
        mGraph.addSeries(consumedSeries);
        mGraph.addSeries(burnedSeries);
        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(mGraph);
        staticLabelsFormatter.setHorizontalLabels(dateRange);
        mGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private String[] getMonthDateRanges(String month, int year) {
        try {
            int[] weekNos = Utils.getWeeksOfMonth(month, year);
            List<String> weekStr = new ArrayList<>();
            for (int x = 0; x < weekNos.length; x++) {
                if (x == 0)
                    weekStr.add(Utils.getDateFromWeekNumber(weekNos[x], year));
                else if (!weekStr.get(weekStr.size() - 1).equals(Utils.getDateFromWeekNumber(weekNos[x], year)))
                    weekStr.add(Utils.getDateFromWeekNumber(weekNos[x], year));
            }

            return weekStr.toArray(new String[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private double getCalories(int[] monthlyDay, double[] monthlyValue, String day, int year) {
        try {
            for (int x = 0; x < monthlyDay.length; x++) {
                if (Utils.getDateFromWeekNumber(monthlyDay[x], year).equals(day)) {
                    return monthlyValue[x];
                }
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException", e);
        }
        return 0;
    }
}

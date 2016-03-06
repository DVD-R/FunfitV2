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

    boolean isFirst;
    String month;
    int year;
    double calConsumed, calBurned;
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
        calConsumed = i.getDoubleExtra(Constants.CAL_CONSUMED, 0);
        calBurned = i.getDoubleExtra(Constants.CAL_BURNED, 0);
        monthlyConsumedDay = i.getIntArrayExtra(Constants.CONSUMED_TIME);
        monthlyBurnedDay = i.getIntArrayExtra(Constants.BURNED_TIME);
        monthlyConsumedValue = i.getDoubleArrayExtra(Constants.CONSUMED_VALUE);
        monthlyBurnedValue = i.getDoubleArrayExtra(Constants.BURNED_VALUE);

        if (isFirst) {
            mTextMonth.setTextColor(Color.parseColor("#FFFFFF"));
            mTextYear.setTextColor(Color.parseColor("#FFFFFF"));
            mTextCalConsumed.setTextColor(Color.parseColor("#FFFFFF"));
            mTextCalBurned.setTextColor(Color.parseColor("#FFFFFF"));
            if (Utils.getCluster(this).equals("velocity"))
                mLayoutMonthly.setBackgroundColor(Color.parseColor("#2980b9"));
            else
                mLayoutMonthly.setBackgroundColor(Color.parseColor("#c0392b"));
        }

        mTextMonth.setText(month);
        mTextYear.setText(year + "");
        mTextCalConsumed.setText(calConsumed + "");
        mTextCalBurned.setText(calBurned + "");

        mTextCalConsumed.setText("Calories Consumed this week: " + calConsumed);
        mTextCalBurned.setText("Calories Burned this week: " + calBurned);

        String[] dateRange = getMonthDateRanges(month, year);

        List<DataPoint> pts = new ArrayList<>();
        for(int x=0;x<dateRange.length;x++) {
            double consumed = getConsumed(monthlyConsumedDay,
                    monthlyConsumedValue, dateRange[x], year);
            pts.add(new DataPoint(x, consumed));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(pts.toArray(new DataPoint[0]));
        mGraph.addSeries(series);

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

    private double getConsumed(int[] monthlyConsumedDay, double[] monthlyConsumedValue, String day, int year) {
        try {
            for (int x = 0; x < monthlyConsumedDay.length; x++) {
                Log.v(LOG_TAG,Utils.getDateFromWeekNumber(monthlyConsumedDay[x], year) + " : " + day);
                if (Utils.getDateFromWeekNumber(monthlyConsumedDay[x], year).equals(day)) {
                    Log.v(LOG_TAG,"YES!");
                    return monthlyConsumedValue[x];
                }
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException", e);
        }
        return 0;
    }
}

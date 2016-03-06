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

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/6/2016.
 */
public class MonthlyGraphActivity  extends AppCompatActivity {
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
    String month, year;
    double calConsumed, calBurned;
    private String[] monthlyConsumedDay, monthlyBurnedDay;
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
        year = i.getStringExtra(Constants.YEAR);
        calConsumed = i.getDoubleExtra(Constants.CAL_CONSUMED, 0);
        calBurned = i.getDoubleExtra(Constants.CAL_BURNED, 0);
        monthlyConsumedDay = i.getStringArrayExtra(Constants.CONSUMED_TIME);
        monthlyBurnedDay = i.getStringArrayExtra(Constants.BURNED_TIME);
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
        mTextYear.setText(year);
        mTextCalConsumed.setText(calConsumed + "");
        mTextCalBurned.setText(calBurned + "");

        mTextCalConsumed.setText("Calories Consumed this week: " + calConsumed);
        mTextCalBurned.setText("Calories Burned this week: " + calBurned);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Jan")),
                new DataPoint(1, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Feb")),
                new DataPoint(2, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Mar")),
                new DataPoint(3, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Apr")),
                new DataPoint(4, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "May")),
                new DataPoint(5, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Jun")),
                new DataPoint(6, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Jul")),
                new DataPoint(7, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Aug")),
                new DataPoint(8, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Sep")),
                new DataPoint(9, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Oct")),
                new DataPoint(10, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Nov")),
                new DataPoint(11, getConsumed(monthlyConsumedDay, monthlyConsumedValue, "Dec"))
        });
        mGraph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(mGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        mGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private double getConsumed(String[] monthlyConsumedDay, double[] monthlyConsumedValue, String day) {
        try {
            for (int x = 0; x < monthlyConsumedDay.length; x++) {
                if (monthlyConsumedDay[x].equals(day)) {
                    return monthlyConsumedValue[x];
                }
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException", e);
        }
        return 0;
    }
}

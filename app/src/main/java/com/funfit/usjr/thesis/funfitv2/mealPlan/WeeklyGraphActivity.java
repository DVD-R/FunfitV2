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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/6/2016.
 */
public class WeeklyGraphActivity extends AppCompatActivity {
    private static final String LOG_TAG = WeeklyGraphActivity.class.getSimpleName();
    @Bind(R.id.text_start)
    TextView mTextStart;
    @Bind(R.id.text_end)
    TextView mTextEnd;
    @Bind(R.id.text_month)
    TextView mTextMonth;
    @Bind(R.id.text_cal_consumed)
    TextView mTextCalConsumed;
    @Bind(R.id.text_cal_burned)
    TextView mTextCalBurned;
    @Bind(R.id.layout_weekly)
    RelativeLayout mLayoutWeekly;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.graph)
    GraphView mGraph;

    boolean isFirst;
    String start, end, month;
    double calConsumed, calBurned;
    private String[] weeklyConsumedDay, weeklyBurnedDay;
    private double[] weeklyConsumedValue, weeklyBurnedValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_graph);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent i = getIntent();
        isFirst = i.getBooleanExtra(Constants.IS_FIRST, false);
        start = i.getStringExtra(Constants.START_DAY);
        end = i.getStringExtra(Constants.END_DAY);
        month = i.getStringExtra(Constants.MONTH);
        calConsumed = i.getDoubleExtra(Constants.CAL_CONSUMED, 0);
        calBurned = i.getDoubleExtra(Constants.CAL_BURNED, 0);
        weeklyConsumedDay = i.getStringArrayExtra(Constants.CONSUMED_TIME);
        weeklyBurnedDay = i.getStringArrayExtra(Constants.BURNED_TIME);
        weeklyConsumedValue = i.getDoubleArrayExtra(Constants.CONSUMED_VALUE);
        weeklyBurnedValue = i.getDoubleArrayExtra(Constants.BURNED_VALUE);

        if (isFirst) {
            mTextStart.setTextColor(Color.parseColor("#FFFFFF"));
            mTextEnd.setTextColor(Color.parseColor("#FFFFFF"));
            mTextMonth.setTextColor(Color.parseColor("#FFFFFF"));
            mTextCalConsumed.setTextColor(Color.parseColor("#FFFFFF"));
            mTextCalBurned.setTextColor(Color.parseColor("#FFFFFF"));
            if (Utils.getCluster(this).equals("velocity"))
                mLayoutWeekly.setBackgroundColor(Color.parseColor("#2980b9"));
            else
                mLayoutWeekly.setBackgroundColor(Color.parseColor("#c0392b"));
        }

        mTextStart.setText(start);
        mTextEnd.setText(end);
        mTextMonth.setText(month);
        mTextCalConsumed.setText(calConsumed + "");
        mTextCalBurned.setText(calBurned + "");

        mTextCalConsumed.setText("Calories Consumed this week: " + calConsumed);
        mTextCalBurned.setText("Calories Burned this week: " + calBurned);

        LineGraphSeries<DataPoint> seriesConsumed = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, getConsumed(weeklyConsumedDay, weeklyConsumedValue, "Sun")),
                new DataPoint(1, getConsumed(weeklyConsumedDay, weeklyConsumedValue, "Mon")),
                new DataPoint(2, getConsumed(weeklyConsumedDay, weeklyConsumedValue, "Tue")),
                new DataPoint(3, getConsumed(weeklyConsumedDay, weeklyConsumedValue, "Wed")),
                new DataPoint(4, getConsumed(weeklyConsumedDay, weeklyConsumedValue, "Thu")),
                new DataPoint(5, getConsumed(weeklyConsumedDay, weeklyConsumedValue, "Fri")),
                new DataPoint(6, getConsumed(weeklyConsumedDay, weeklyConsumedValue, "Sat"))
        });
        LineGraphSeries<DataPoint> seriesBurned = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, getConsumed(weeklyBurnedDay, weeklyBurnedValue, "Sun")),
                new DataPoint(1, getConsumed(weeklyBurnedDay, weeklyBurnedValue, "Mon")),
                new DataPoint(2, getConsumed(weeklyBurnedDay, weeklyBurnedValue, "Tue")),
                new DataPoint(3, getConsumed(weeklyBurnedDay, weeklyBurnedValue, "Wed")),
                new DataPoint(4, getConsumed(weeklyBurnedDay, weeklyBurnedValue, "Thu")),
                new DataPoint(5, getConsumed(weeklyBurnedDay, weeklyBurnedValue, "Fri")),
                new DataPoint(6, getConsumed(weeklyBurnedDay, weeklyBurnedValue, "Sat"))
        });
        seriesConsumed.setColor(Color.parseColor("#2980b9"));
        seriesConsumed.setTitle("Calories Consumed");
        seriesBurned.setColor(Color.parseColor("#c0392b"));
        seriesBurned.setTitle("Calories Burned");
        mGraph.addSeries(seriesBurned);
        mGraph.addSeries(seriesConsumed);
        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(mGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"});
        mGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private double getConsumed(String[] weeklyConsumedDay, double[] weeklyConsumedValue, String day) {
        try {
            for (int x = 0; x < weeklyConsumedDay.length; x++) {
                if (weeklyConsumedDay[x].equals(day)) {
                    return weeklyConsumedValue[x];
                }
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException", e);
        }
        return 0;
    }
}

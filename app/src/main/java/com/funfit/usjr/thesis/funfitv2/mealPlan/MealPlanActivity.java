package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.listener.LeftGestureListener;
import com.funfit.usjr.thesis.funfitv2.listener.RightGestureListener;
import com.funfit.usjr.thesis.funfitv2.search.SearchActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealPlanActivity extends Fragment {

    GestureDetectorCompat gestureDetectorCompat;
    @Bind(R.id.container)FrameLayout piechartLayout;

    private PieChart mPieChart;
    private float [] yData = {50, 46, 4};
    private String [] xData = {"Carbs","Fat","Protein"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meal_plan, container, false);
        gestureDetectorCompat = new GestureDetectorCompat(getActivity(),new LeftGestureListener(getActivity()));
        ButterKnife.bind(this,view);
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        mPieChart = new PieChart(getActivity());
        //add pie chart to pie chart layout
        piechartLayout.addView(mPieChart);
        piechartLayout.setBackgroundColor(Color.TRANSPARENT);

        //configure pie chart
        mPieChart.setUsePercentValues(true);
//        mPieChart.setDescription("");

        //enable and configure
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColorTransparent(true);
        mPieChart.setHoleRadius(7);
        mPieChart.setTransparentCircleRadius(10);

        //enable rotation of the chart by touch
        mPieChart.setRotationAngle(0);
        mPieChart.setRotationEnabled(true);

        //set a chart value selected listener
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (entry == null)
                    return;

            }

            @Override
            public void onNothingSelected() {

            }
        });

        //add Data
        addData();

        // customize legends
        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setXEntrySpace(5);
        l.setYEntrySpace(3);

    }


    private void addData(){
        ArrayList<Entry> yValue = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yValue.add(new Entry(yData[i], i));

        ArrayList<String> xValue = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xValue.add(xData[i]);

        //create pie data set
        PieDataSet dataSet = new PieDataSet(yValue, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        //add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c: ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //instantiate pie data object now
        PieData pieData = new PieData(xValue, dataSet);
        pieData.setValueFormatter(new PercentFormatter());
//        pieData.setValueTextSize(11f);
//        pieData.setValueTextColor(Color.GRAY);

        mPieChart.setData(pieData);

        //undo all highlights
        mPieChart.highlightValues(null);

        //update pie chart
        mPieChart.invalidate();
    }

    @OnClick(R.id.breakfastClk)
    public void addBreakfast(){
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }



    @OnClick(R.id.lunchClk)
    public void addLunch(){
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }


    @OnClick(R.id.dinnerClk)
    public void addDinner(){
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }



    @OnClick(R.id.snackClk)
    public void addSnack(){
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }


}
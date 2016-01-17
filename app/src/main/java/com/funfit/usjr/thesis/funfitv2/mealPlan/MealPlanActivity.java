package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.listener.LeftGestureListener;
import com.funfit.usjr.thesis.funfitv2.search.SearchActivity;
import com.funfit.usjr.thesis.funfitv2.views.IMealPlanFragmentView;
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

public class MealPlanActivity extends Fragment implements IMealPlanFragmentView{

    GestureDetectorCompat gestureDetectorCompat;
    @Bind(R.id.container)FrameLayout piechartLayout;
    @Bind(R.id.breakfastCardView)CardView mBreakFastCardView;
    private PieChart mPieChart;
    private float [] yData = {50, 46, 4};
    private String [] xData = {"Carbs","Fat","Protein"};
    private int descriptionViewFullHeight;
    private MealPlanPresenter mealPlanPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meal_plan, container, false);
        gestureDetectorCompat = new GestureDetectorCompat(getActivity(),new LeftGestureListener(getActivity()));
        ButterKnife.bind(this,view);

        mealPlanPresenter = new MealPlanPresenter(this);
        mBreakFastCardView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mBreakFastCardView.getViewTreeObserver().removeOnPreDrawListener(this);
                //save the full height
                descriptionViewFullHeight = mBreakFastCardView.getHeight();

                ViewGroup.LayoutParams layoutParams = mBreakFastCardView.getLayoutParams();
                layoutParams.height = (int) getActivity().getResources().getDimension(R.dimen.cardview_min_height);
                mBreakFastCardView.setLayoutParams(layoutParams);
                return true;
            }
        });

        return view;
    }

    @OnClick(R.id.collapseLayout)
    public void touch(){
        mealPlanPresenter.initCommand();
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


    @Override
    public void collapseCardView() {
        int cardViewMinHeight = (int) getActivity().getResources().getDimension(R.dimen.cardview_min_height);
        if (mBreakFastCardView.getHeight() == cardViewMinHeight){
            //expand
            ValueAnimator animator = ValueAnimator.ofInt(mBreakFastCardView.getMeasuredHeightAndState(), descriptionViewFullHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mBreakFastCardView.getLayoutParams();
                    layoutParams.height = value;
                    mBreakFastCardView.setLayoutParams(layoutParams);
                }
            });
            animator.start();
        }else {
            //collapse
            ValueAnimator animator = ValueAnimator.ofInt(mBreakFastCardView.getMeasuredHeightAndState(), cardViewMinHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mBreakFastCardView.getLayoutParams();
                    mBreakFastCardView.setLayoutParams(layoutParams);
                }
            });
            animator.start();
        }
    }
}
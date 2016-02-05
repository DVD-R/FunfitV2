package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.adapter.BreakFastRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.adapter.DinnerRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.adapter.LunchRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.listener.LeftGestureListener;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealPlanActivity extends Fragment implements IMealPlanFragmentView{

    GestureDetectorCompat gestureDetectorCompat;
    @Bind(R.id.container)FrameLayout piechartLayout;

    //Breakfast Resources Binding
    @Bind(R.id.breakfast_recycler_view)RecyclerView breakfastRecyclerView;
    @Bind(R.id.breakfastRecyclerLayout)FrameLayout breakfastRecyclerLayout;
    @Bind(R.id.breakfastLayout)FrameLayout breakfastLayout;
    @Bind(R.id.collapseImgBtn)ImageButton mCollapseImgBtn;
    @Bind(R.id.breakfastMealCountTxt)TextView mBreakfastMealCount;
    @Bind(R.id.breafastTotalkCalTxt)TextView mBreakFastTotalkCal;
    //Breakfast Resources Binding

    //Lunch Resources Binding
    @Bind(R.id.lunchLayout)FrameLayout mLunchLayout;
    @Bind(R.id.lunchRecyclerLayout)FrameLayout mLunchRecyclerLayout;
    @Bind(R.id.lunchCollapseImgBtn)ImageButton mLunchCollapseImgBtn;
    @Bind(R.id.lunch_recycler_view)RecyclerView mLunchRecyclerView;
    @Bind(R.id.lunchMealCountTxt)TextView mLunchMealCount;
    @Bind(R.id.lunchTotalkCalTxt)TextView mLunchTotalkCal;
    //Lunch Resources Binding

    //Dinner Resources Binding
    @Bind(R.id.dinnerLayout)FrameLayout mDinnerLayout;
    @Bind(R.id.dinnerRecyclerLayout)FrameLayout mDinnerRecyclerLayout;
    @Bind(R.id.dinnerCollapseImgBtn)ImageButton mDinnerCollapseImgBtn;
    @Bind(R.id.dinner_recycler_view)RecyclerView mDinnerRecyclerView;
    @Bind(R.id.dinnerMealCountTxt)TextView mDinnerMealCount;
    @Bind(R.id.dinnerTotalkCalTxt)TextView mDinnerTotalkCal;
    //Dinner Resources Binding

    //Snack/Others Resource Binding
    @Bind(R.id.snackLayout)FrameLayout mSnackLayout;
    @Bind(R.id.snackRecyclerLayout)FrameLayout mSnackRecyclerLayout;
    @Bind(R.id.snackCollapseImgBtn)ImageButton mSnackCollapseImgBtn;
    @Bind(R.id.snack_recycler_view)RecyclerView mSnackRecyclerView;
    @Bind(R.id.snackMealCountTxt)TextView mSnackMealCount;
    @Bind(R.id.snackTotalkCalTxt)TextView mSnackTotalkCal;
    //Snack/Others Resources Binding

    protected boolean mBreakFastFlag;
    protected boolean mLunchFlag;
    protected boolean mDinnerFlag;
    protected boolean mSnackFlag;
    private PieChart mPieChart;
    private float [] yData = {50, 46, 4};
    private String [] xData = {"Carbs","Fat","Protein"};
    private int descriptionViewFullHeight;
    private MealPlanPresenter mealPlanPresenter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLunchLayoutManager;
    private RecyclerView.LayoutManager mDinnerLayoutManager;
    private RecyclerView.LayoutManager mSnackLayoutManager;
    private List<Meal> mealList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meal_plan, container, false);
        gestureDetectorCompat = new GestureDetectorCompat(getActivity(),new LeftGestureListener(getActivity()));
        ButterKnife.bind(this, view);

        mealPlanPresenter = new MealPlanPresenter(this);
        mBreakFastFlag = false;
        mLunchFlag = false;
        mDinnerFlag = false;
        mSnackFlag = false;
        return view;
    }

    //breakfast command <---------
    @OnClick(R.id.collapseImgBtn)
    public void collapse() {
        if (!mBreakFastFlag){
            mCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            breakfastRecyclerView.setVisibility(View.VISIBLE);
            mBreakFastFlag = true;
        } else{
            breakfastRecyclerView.setVisibility(View.GONE);
            mCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mBreakFastFlag = false;
        }
    }
    //breakfast command <---------



    //lunch command<--------------
    @OnClick(R.id.lunchCollapseImgBtn)
    public void lunchCollapseImgBtn(){
        if (!mLunchFlag){
            mLunchCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            mLunchRecyclerView.setVisibility(View.VISIBLE);
            mLunchFlag = true;
        } else{
            mLunchCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mLunchRecyclerView.setVisibility(View.GONE);
            mLunchFlag = false;
        }
    }
    //lunch command<--------------


    //dinner command<-------------
    @OnClick(R.id.dinnerCollapseImgBtn)
    public void dinnerCollapseBtn(){
        if (!mDinnerFlag){
            mDinnerCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            mDinnerRecyclerView.setVisibility(View.VISIBLE);
            mDinnerFlag = true;
        } else{
            mDinnerCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mDinnerRecyclerView.setVisibility(View.GONE);
            mDinnerFlag = false;
        }
    }
    //dinner command<-------------



    //snack command<--------------
    @OnClick(R.id.snackCollapseImgBtn)
    public void snackCollapseImgBtn(){
        if (!mSnackFlag){
            mSnackCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            mSnackRecyclerView.setVisibility(View.VISIBLE);
            mSnackFlag = true;
        } else{
            mSnackCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mSnackRecyclerView.setVisibility(View.GONE);
            mSnackFlag = false;
        }
    }
    //snack command<--------------

    @Override
    public void onResume() {
        super.onResume();

        //OPEN LOCAL DATABASE CONNECTION
        mealPlanPresenter.openDb();

        mPieChart = new PieChart(getActivity());
        //add pie chart to pie chart layout
        piechartLayout.addView(mPieChart);
        piechartLayout.setBackgroundColor(Color.TRANSPARENT);

        //configure pie chart
        mPieChart.setUsePercentValues(true);
        //mPieChart.setDescription("");

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

        //QUERY FROM LOCAL DATABASE FOR MOST RECENT FOOD LIST
        mealPlanPresenter.queryMealList();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //CLOSE LOCAL DATABASE CONNECTION
        mealPlanPresenter.closeDb();
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
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Breakfast");
        startActivity(i);
    }

    @OnClick(R.id.lunchClk)
    public void addLunch(){
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Lunch");
        startActivity(i);
    }

    @OnClick(R.id.dinnerClk)
    public void addDinner(){
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Dinner");
        startActivity(i);
    }

    @OnClick(R.id.snackClk)
    public void addSnack(){
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Snack");
        startActivity(i);
    }

    @Override
    public Context getContxt() {
        return getActivity();
    }

    @Override
    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
        if (mealList.size() != 0){
            mealPlanPresenter.checkTimeValidity();
        }
    }

    @Override
    public List<Meal> getMealList() {
        return mealList;
    }

    @Override
    public void unhideBreakfast() {
        breakfastLayout.setVisibility(View.GONE);
        breakfastRecyclerLayout.setVisibility(View.VISIBLE);
        mealPlanPresenter.displayBreakfast();
    }

    @Override
    public void unhideLunch() {
        mLunchLayout.setVisibility(View.GONE);
        mLunchRecyclerLayout.setVisibility(View.VISIBLE);
        mealPlanPresenter.displayLunch();
    }

    @Override
    public void unhideDinner() {
        mDinnerLayout.setVisibility(View.GONE);
        mDinnerRecyclerLayout.setVisibility(View.VISIBLE);
        mealPlanPresenter.displayDinner();
    }

    @Override
    public void unhideSnack() {
        mSnackLayout.setVisibility(View.GONE);
        mSnackRecyclerLayout.setVisibility(View.VISIBLE);
        mealPlanPresenter.displaySnack();
    }

    @Override
    public void displayBreakfast() {
        Meal meals = null;
        List<Meal> breakfastlist = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getmTime().equals("Breakfast")) {
                meals = new Meal();
                meals = getMeal(i, mealList);
                breakfastlist.add(meals);
            }
        }
        mBreakfastMealCount.setText(String.valueOf(breakfastlist.size()));
        BreakFastRecyclerAdapter breakFastRecyclerAdapter = new BreakFastRecyclerAdapter(breakfastlist);
        breakfastRecyclerView.setAdapter(breakFastRecyclerAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        breakfastRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void displayLunch() {
        Meal meals = null;
        List<Meal> lunchList = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getmTime().equals("Lunch")) {
                meals = new Meal();
                meals = getMeal(i, mealList);
                lunchList.add(meals);
            }
        }
        mLunchMealCount.setText(String.valueOf(lunchList.size()));
        LunchRecyclerAdapter lunchRecyclerAdapter = new LunchRecyclerAdapter(lunchList);
        mLunchRecyclerView.setAdapter(lunchRecyclerAdapter);
        mLunchLayoutManager = new LinearLayoutManager(getActivity());
        mLunchRecyclerView.setLayoutManager(mLunchLayoutManager);
    }

    @Override
    public void displaDinner() {
        Meal meals = null;
        List<Meal> dinnerList = new ArrayList<>();
        for (int i = 0; i <mealList.size(); i++){
            if (mealList.get(i).getmTime().equals("Dinner")){
                meals = new Meal();
                meals = getMeal(i,mealList);
                dinnerList.add(meals);
            }
        }
        mDinnerMealCount.setText(String.valueOf(dinnerList.size()));
        DinnerRecyclerAdapter dinnerRecyclerAdapter = new DinnerRecyclerAdapter(dinnerList);
        mDinnerRecyclerView.setAdapter(dinnerRecyclerAdapter);
        mDinnerLayoutManager = new LinearLayoutManager(getActivity());
        mDinnerRecyclerView.setLayoutManager(mDinnerLayoutManager);
    }

    @Override
    public void displaySnack() {
        Meal meals = null;
        List<Meal> snackList = new ArrayList<>();
        for (int i = 0; i <mealList.size(); i++){
            if (mealList.get(i).getmTime().equals("Snack")){
                meals = new Meal();
                meals = getMeal(i,mealList);
                snackList.add(meals);
            }
        }
        mSnackMealCount.setText(String.valueOf(snackList.size()));
        DinnerRecyclerAdapter dinnerRecyclerAdapter = new DinnerRecyclerAdapter(snackList);
        mSnackRecyclerView.setAdapter(dinnerRecyclerAdapter);
        mSnackLayoutManager = new LinearLayoutManager(getActivity());
        mSnackRecyclerView.setLayoutManager(mSnackLayoutManager);
    }

    public Meal getMeal(int i, List<Meal> mealList){
        Meal meal = new Meal();
        meal.setMeal_id(mealList.get(i).getMeal_id());
        meal.setmName(mealList.get(i).getmName());
        meal.setFat(mealList.get(i).getFat());
        meal.setCholesterol(mealList.get(i).getCholesterol());
        meal.setSodium(mealList.get(i).getSodium());
        meal.setCarbohydrate(mealList.get(i).getCarbohydrate());
        meal.setProtein(mealList.get(i).getProtein());
        meal.setCalories(mealList.get(i).getCalories());
        meal.setmTime(mealList.get(i).getmTime());
        return meal;
    }
}
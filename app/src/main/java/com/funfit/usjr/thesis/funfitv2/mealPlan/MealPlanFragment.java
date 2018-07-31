package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.adapter.BreakFastRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.adapter.DinnerRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.adapter.LunchRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.search.SearchActivity;
import com.funfit.usjr.thesis.funfitv2.services.MealService;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.views.IMealPlanFragmentView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MealPlanFragment extends Fragment implements IMealPlanFragmentView {
    private static final String TOTAL_CAL = "Total Calories: ";
    private static final String LOG_TAG = MealPlanFragment.class.getSimpleName();

    @BindView(R.id.container)
    FrameLayout piechartLayout;
    @BindView(R.id.txt_cal_rdi)
    TextView mTextCalRdi;

    //Breakfast Resources Binding
    @BindView(R.id.img_breakfast)
    ImageView mImageBreakfast;
    @BindView(R.id.txt_breakfast_name)
    TextView mTextBreakfastName;
    @BindView(R.id.breakfast_recycler_view)
    RecyclerView breakfastRecyclerView;
    @BindView(R.id.breakfastRecyclerLayout)
    FrameLayout breakfastRecyclerLayout;
    @BindView(R.id.breakfastLayout)
    FrameLayout breakfastLayout;
    @BindView(R.id.collapseImgBtn)
    ImageButton mCollapseImgBtn;
    @BindView(R.id.breakfastMealCountTxt)
    TextView mBreakfastMealCount;
    @BindView(R.id.breafastTotalkCalTxt)
    TextView mBreakFastTotalkCal;
    //Breakfast Resources Binding

    //Lunch Resources Binding
    @BindView(R.id.img_lunch)
    ImageView mImageLunch;
    @BindView(R.id.txt_lunch_name)
    TextView mTextLunchName;
    @BindView(R.id.lunchLayout)
    FrameLayout mLunchLayout;
    @BindView(R.id.lunchRecyclerLayout)
    FrameLayout mLunchRecyclerLayout;
    @BindView(R.id.lunchCollapseImgBtn)
    ImageButton mLunchCollapseImgBtn;
    @BindView(R.id.lunch_recycler_view)
    RecyclerView mLunchRecyclerView;
    @BindView(R.id.lunchMealCountTxt)
    TextView mLunchMealCount;
    @BindView(R.id.lunchTotalkCalTxt)
    TextView mLunchTotalkCal;
    //Lunch Resources Binding

    //Dinner Resources Binding
    @BindView(R.id.img_dinner)
    ImageView mImageDinner;
    @BindView(R.id.txt_dinner_name)
    TextView mTextDinnerName;
    @BindView(R.id.dinnerLayout)
    FrameLayout mDinnerLayout;
    @BindView(R.id.dinnerRecyclerLayout)
    FrameLayout mDinnerRecyclerLayout;
    @BindView(R.id.dinnerCollapseImgBtn)
    ImageButton mDinnerCollapseImgBtn;
    @BindView(R.id.dinner_recycler_view)
    RecyclerView mDinnerRecyclerView;
    @BindView(R.id.dinnerMealCountTxt)
    TextView mDinnerMealCount;
    @BindView(R.id.dinnerTotalkCalTxt)
    TextView mDinnerTotalkCal;
    //Dinner Resources Binding

    //Snack/Others Resource Binding
    @BindView(R.id.img_snack)
    ImageView mImageSnack;
    @BindView(R.id.txt_snack_name)
    TextView mTextSnackName;
    @BindView(R.id.snackLayout)
    FrameLayout mSnackLayout;
    @BindView(R.id.snackRecyclerLayout)
    FrameLayout mSnackRecyclerLayout;
    @BindView(R.id.snackCollapseImgBtn)
    ImageButton mSnackCollapseImgBtn;
    @BindView(R.id.snack_recycler_view)
    RecyclerView mSnackRecyclerView;
    @BindView(R.id.snackMealCountTxt)
    TextView mSnackMealCount;
    @BindView(R.id.snackTotalkCalTxt)
    TextView mSnackTotalkCal;
    @BindView(R.id.txt_summary)
    TextView mTextSummary;
    @BindView(R.id.txt_cal_consumed)
    TextView mTextCalConsumed;
    @BindView(R.id.txt_cal_remaining)
    TextView mTextCalRemaining;
    //Snack/Others Resources Binding

    protected boolean mBreakFastFlag;
    protected boolean mLunchFlag;
    protected boolean mDinnerFlag;
    protected boolean mSnackFlag;
    private PieChart mPieChart;
    private float[] yData = {50, 46, 4};
    private String[] xData = {"Carbs", "Fat", "Protein"};
    private MealPlanPresenter mealPlanPresenter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLunchLayoutManager;
    private RecyclerView.LayoutManager mDinnerLayoutManager;
    private RecyclerView.LayoutManager mSnackLayoutManager;
    private SharedPreferences sharedRdi;
    double bkf, lch, dnr, snk;
    long mealId = 0;
    private static List<Meal> mealList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);
        ButterKnife.bind(this, view);
        setLayoutEnhancements();

        sharedRdi = getActivity().getSharedPreferences(Constants.RDI_PREF_ID, getActivity().MODE_PRIVATE);
        float remCal = Float.parseFloat(sharedRdi.getString(Constants.RDI, "0"));
        mealPlanPresenter = new MealPlanPresenter(this, getActivity());
        mBreakFastFlag = false;
        mLunchFlag = false;
        mDinnerFlag = false;
        mSnackFlag = false;

        displayCalorieViews();
        return view;
    }

    private void setLayoutEnhancements() {
        Glide.with(this).load("https://78.media.tumblr.com/685b8225bc6c7b1da9e753d598ef59a8/tumblr_ox40fzA0zY1w3rvpoo1_500.jpg")
                .crossFade().centerCrop().into(mImageBreakfast);
        Glide.with(this).load("https://78.media.tumblr.com/a789877a8c5ce5c0c65c739ee224fee9/tumblr_ns8qswPrkq1u7gq3uo1_500.jpg")
                .crossFade().centerCrop().into(mImageLunch);
        Glide.with(this).load("https://78.media.tumblr.com/36c764e8535a43c95565a8a7e1487a2b/tumblr_p03pw3bu6c1t9ocfzo1_500.jpg")
                .crossFade().centerCrop().into(mImageDinner);
        Glide.with(this).load("https://floraisabelle.com/content/images/2014/Sep/gro.jpg")
                .crossFade().centerCrop().into(mImageSnack);
        mTextBreakfastName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextLunchName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextDinnerName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextSnackName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextSummary.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
    }

    //breakfast command <---------
    @OnClick(R.id.collapseLayout)
    public void breakfastCollapse() {
        if (!mBreakFastFlag) {
            mCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            breakfastRecyclerView.setVisibility(View.VISIBLE);
            mBreakFastFlag = true;
        } else {
            breakfastRecyclerView.setVisibility(View.GONE);
            mCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mBreakFastFlag = false;
        }
    }
    //breakfast command <---------


    //lunch command<--------------
    @OnClick(R.id.lunchRecyclerLayout)
    public void lunchCollapseImgBtn() {
        if (!mLunchFlag) {
            mLunchCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            mLunchRecyclerView.setVisibility(View.VISIBLE);
            mLunchFlag = true;
        } else {
            mLunchCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mLunchRecyclerView.setVisibility(View.GONE);
            mLunchFlag = false;
        }
    }
    //lunch command<--------------


    //dinner command<-------------
    @OnClick(R.id.dinnerRecyclerLayout)
    public void dinnerCollapseBtn() {
        if (!mDinnerFlag) {
            mDinnerCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            mDinnerRecyclerView.setVisibility(View.VISIBLE);
            mDinnerFlag = true;
        } else {
            mDinnerCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mDinnerRecyclerView.setVisibility(View.GONE);
            mDinnerFlag = false;
        }
    }
    //dinner command<-------------


    //snack command<--------------
    @OnClick(R.id.snackRecyclerLayout)
    public void snackCollapseImgBtn() {
        if (!mSnackFlag) {
            mSnackCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_down));
            mSnackRecyclerView.setVisibility(View.VISIBLE);
            mSnackFlag = true;
        } else {
            mSnackCollapseImgBtn.setBackground(getResources().getDrawable(R.drawable.arrow_right));
            mSnackRecyclerView.setVisibility(View.GONE);
            mSnackFlag = false;
        }
    }
    //snack command<--------------

    @Override
    public void onResume() {
        super.onResume();

        mPieChart = new PieChart(getActivity());
        //add pie chart to pie chart layout
        piechartLayout.addView(mPieChart);
        piechartLayout.setBackgroundColor(Color.TRANSPARENT);

        //configure pie chart
        mPieChart.setUsePercentValues(true);
        //mPieChart.setDescription("");

        //enable and configure
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.TRANSPARENT);
        mPieChart.setHoleRadius(7);
        mPieChart.setTransparentCircleRadius(10);

        //enable rotation of the chart by touch
        mPieChart.setRotationAngle(0);
        mPieChart.setRotationEnabled(true);

        //set a chart value selected listener
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
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

    private void addData() {
        List<PieEntry> yValue = new ArrayList<PieEntry>();

        for (int i = 0; i < yData.length; i++)
            yValue.add(new PieEntry(yData[i], i));

        ArrayList<String> xValue = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xValue.add(xData[i]);

        //create pie data set
        PieDataSet dataSet = new PieDataSet(yValue, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        //add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //instantiate pie data object now
        PieData pieData = new PieData(dataSet);
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
    public void addBreakfast() {
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Breakfast");
        startActivity(i);
    }

    @OnClick(R.id.lunchClk)
    public void addLunch() {
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Lunch");
        startActivity(i);
    }

    @OnClick(R.id.dinnerClk)
    public void addDinner() {
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Dinner");
        startActivity(i);
    }

    @OnClick(R.id.snackClk)
    public void addSnack() {
        Intent i = new Intent(getActivity(), SearchActivity.class);
        i.putExtra("MEALTIME", "Snack");
        startActivity(i);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void setMealList(MealService mealService) {
        mealService.getMeal(getContext().getSharedPreferences(Constants.RDI_PREF_ID, getContext().MODE_PRIVATE)
                        .getString(Constants.UID, ""),
                new Callback<List<ResponseMeal>>() {
                    @Override
                    public void success(List<ResponseMeal> mealModels, Response response) {
                        List<Meal> mealList = new ArrayList<Meal>();
                        for (int x = 0; x < mealModels.size(); x++) {
                            Log.v(LOG_TAG, Utils.getCurrentDate());
                            Log.v(LOG_TAG, mealModels.get(x).getDate());
                            if (Utils.getCurrentDate().equals(
                                    mealModels.get(x).getDate())) {
                                Meal meal =
                                        new Meal(mealId,
                                                mealModels.get(x).getName(),
                                                mealModels.get(x).getFat(),
                                                mealModels.get(x).getSodium(),
                                                mealModels.get(x).getCalories(),
                                                mealModels.get(x).getCholesterol(),
                                                mealModels.get(x).getCarbohydrate(),
                                                mealModels.get(x).getProtein(),
                                                mealModels.get(x).getCourse()
                                        );
                                mealList.add(meal);
                            }
                        }

                        MealPlanFragment.mealList = mealList;

                        if (mealList.size() != 0) {
                            mealPlanPresenter.checkCourseValidity();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(LOG_TAG, error.toString());
                    }
                });
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
        bkf = 0;
        Meal meals = null;
        List<Meal> breakfastlist = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getCourse().equals("Breakfast")) {
                meals = new Meal();
                meals = getMeal(i, mealList);
                breakfastlist.add(meals);
                bkf += meals.getCalories();
            }
        }
        mBreakfastMealCount.setText(String.valueOf(breakfastlist.size()) + " items");
        BreakFastRecyclerAdapter breakFastRecyclerAdapter = new BreakFastRecyclerAdapter(breakfastlist);
        breakfastRecyclerView.setAdapter(breakFastRecyclerAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        breakfastRecyclerView.setLayoutManager(mLayoutManager);

        mBreakFastTotalkCal.setText(TOTAL_CAL + bkf);
        displayCalorieViews();
    }

    @Override
    public void displayLunch() {
        lch = 0;
        Meal meals = null;
        List<Meal> lunchList = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getCourse().equals("Lunch")) {
                meals = new Meal();
                meals = getMeal(i, mealList);
                lunchList.add(meals);
                lch += meals.getCalories();
            }
        }
        mLunchMealCount.setText(String.valueOf(lunchList.size()) + " items");
        LunchRecyclerAdapter lunchRecyclerAdapter = new LunchRecyclerAdapter(lunchList);
        mLunchRecyclerView.setAdapter(lunchRecyclerAdapter);
        mLunchLayoutManager = new LinearLayoutManager(getActivity());
        mLunchRecyclerView.setLayoutManager(mLunchLayoutManager);

        mLunchTotalkCal.setText(TOTAL_CAL + lch);
        displayCalorieViews();
    }

    @Override
    public void displayDinner() {
        dnr = 0;
        Meal meals = null;
        List<Meal> dinnerList = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getCourse().equals("Dinner")) {
                meals = new Meal();
                meals = getMeal(i, mealList);
                dinnerList.add(meals);
                dnr += meals.getCalories();
            }
        }
        mDinnerMealCount.setText(String.valueOf(dinnerList.size()) + " items");
        DinnerRecyclerAdapter dinnerRecyclerAdapter = new DinnerRecyclerAdapter(dinnerList);
        mDinnerRecyclerView.setAdapter(dinnerRecyclerAdapter);
        mDinnerLayoutManager = new LinearLayoutManager(getActivity());
        mDinnerRecyclerView.setLayoutManager(mDinnerLayoutManager);

        mDinnerTotalkCal.setText(TOTAL_CAL + dnr);
        displayCalorieViews();
    }

    @Override
    public void displaySnack() {
        snk = 0;
        Meal meals = null;
        List<Meal> snackList = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getCourse().equals("Snack")) {
                meals = new Meal();
                meals = getMeal(i, mealList);
                snackList.add(meals);
                snk += meals.getCalories();
            }
        }
        mSnackMealCount.setText(String.valueOf(snackList.size()) + " items");
        DinnerRecyclerAdapter dinnerRecyclerAdapter = new DinnerRecyclerAdapter(snackList);
        mSnackRecyclerView.setAdapter(dinnerRecyclerAdapter);
        mSnackLayoutManager = new LinearLayoutManager(getActivity());
        mSnackRecyclerView.setLayoutManager(mSnackLayoutManager);

        mSnackTotalkCal.setText(TOTAL_CAL + snk);
        displayCalorieViews();
    }

    public Meal getMeal(int i, List<Meal> mealList) {
        Meal meal = new Meal();
        meal.setMeal_id(mealList.get(i).getMeal_id());
        meal.setName(mealList.get(i).getName());
        meal.setFat(mealList.get(i).getFat());
        meal.setCholesterol(mealList.get(i).getCholesterol());
        meal.setSodium(mealList.get(i).getSodium());
        meal.setCarbohydrate(mealList.get(i).getCarbohydrate());
        meal.setProtein(mealList.get(i).getProtein());
        meal.setCalories(mealList.get(i).getCalories());
        meal.setCourse(mealList.get(i).getCourse());
        return meal;
    }

    private void displayCalorieViews() {
        double rdi = Double.parseDouble(sharedRdi.getString(Constants.RDI, "0"));
        double calConsumed = bkf + lch + dnr + snk;
        double calRemaining = rdi - calConsumed;
        //Update Calorie Views in Summary
        mTextCalRdi.setText(String.format("%.2f", rdi));
        mTextCalRemaining.setText(String.format("%.2f", calRemaining));
        mTextCalConsumed.setText(String.format("%.2f", calConsumed));

        if (calConsumed > rdi)
            mTextCalConsumed.setTextColor(getResources().getColor(R.color.error_red));
        if (calRemaining < 0)
            mTextCalRemaining.setTextColor(getResources().getColor(R.color.error_red));
    }
}
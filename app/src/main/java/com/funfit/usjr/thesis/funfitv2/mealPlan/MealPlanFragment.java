package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.adapter.BreakFastRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.adapter.DinnerRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.adapter.LunchRecyclerAdapter;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
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

public class MealPlanFragment extends Fragment implements IMealPlanFragmentView {
    private static final String TOTAL_CAL = "Total Calories: ";
    private static final String LOG_TAG = MealPlanFragment.class.getSimpleName();

    @Bind(R.id.container)
    FrameLayout piechartLayout;
    @Bind(R.id.txt_cal_rdi)
    TextView mTextCalRdi;

    //Breakfast Resources Binding
    @Bind(R.id.img_breakfast)
    ImageView mImageBreakfast;
    @Bind(R.id.txt_breakfast_name)
    TextView mTextBreakfastName;
    @Bind(R.id.breakfast_recycler_view)
    RecyclerView breakfastRecyclerView;
    @Bind(R.id.breakfastRecyclerLayout)
    FrameLayout breakfastRecyclerLayout;
    @Bind(R.id.breakfastLayout)
    FrameLayout breakfastLayout;
    @Bind(R.id.collapseImgBtn)
    ImageButton mCollapseImgBtn;
    @Bind(R.id.breakfastMealCountTxt)
    TextView mBreakfastMealCount;
    @Bind(R.id.breafastTotalkCalTxt)
    TextView mBreakFastTotalkCal;
    //Breakfast Resources Binding

    //Lunch Resources Binding
    @Bind(R.id.img_lunch)
    ImageView mImageLunch;
    @Bind(R.id.txt_lunch_name)
    TextView mTextLunchName;
    @Bind(R.id.lunchLayout)
    FrameLayout mLunchLayout;
    @Bind(R.id.lunchRecyclerLayout)
    FrameLayout mLunchRecyclerLayout;
    @Bind(R.id.lunchCollapseImgBtn)
    ImageButton mLunchCollapseImgBtn;
    @Bind(R.id.lunch_recycler_view)
    RecyclerView mLunchRecyclerView;
    @Bind(R.id.lunchMealCountTxt)
    TextView mLunchMealCount;
    @Bind(R.id.lunchTotalkCalTxt)
    TextView mLunchTotalkCal;
    //Lunch Resources Binding

    //Dinner Resources Binding
    @Bind(R.id.img_dinner)
    ImageView mImageDinner;
    @Bind(R.id.txt_dinner_name)
    TextView mTextDinnerName;
    @Bind(R.id.dinnerLayout)
    FrameLayout mDinnerLayout;
    @Bind(R.id.dinnerRecyclerLayout)
    FrameLayout mDinnerRecyclerLayout;
    @Bind(R.id.dinnerCollapseImgBtn)
    ImageButton mDinnerCollapseImgBtn;
    @Bind(R.id.dinner_recycler_view)
    RecyclerView mDinnerRecyclerView;
    @Bind(R.id.dinnerMealCountTxt)
    TextView mDinnerMealCount;
    @Bind(R.id.dinnerTotalkCalTxt)
    TextView mDinnerTotalkCal;
    //Dinner Resources Binding

    //Snack/Others Resource Binding
    @Bind(R.id.img_snack)
    ImageView mImageSnack;
    @Bind(R.id.txt_snack_name)
    TextView mTextSnackName;
    @Bind(R.id.snackLayout)
    FrameLayout mSnackLayout;
    @Bind(R.id.snackRecyclerLayout)
    FrameLayout mSnackRecyclerLayout;
    @Bind(R.id.snackCollapseImgBtn)
    ImageButton mSnackCollapseImgBtn;
    @Bind(R.id.snack_recycler_view)
    RecyclerView mSnackRecyclerView;
    @Bind(R.id.snackMealCountTxt)
    TextView mSnackMealCount;
    @Bind(R.id.snackTotalkCalTxt)
    TextView mSnackTotalkCal;
    @Bind(R.id.txt_summary)
    TextView mTextSummary;
    @Bind(R.id.txt_cal_consumed)
    TextView mTextCalConsumed;
    @Bind(R.id.txt_cal_remaining)
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
    private static List<Meal> mealList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meal_plan, container, false);
        ButterKnife.bind(this, view);
        setLayoutEnhancements();

        sharedRdi = getActivity().getSharedPreferences(Constants.RDI_PREF_ID, getActivity().MODE_PRIVATE);
        float remCal = Float.parseFloat(sharedRdi.getString(Constants.RDI, "0"));
        mealPlanPresenter = new MealPlanPresenter(this);
        mBreakFastFlag = false;
        mLunchFlag = false;
        mDinnerFlag = false;
        mSnackFlag = false;

        displayCalorieViews();
        return view;
    }

    private void setLayoutEnhancements() {
        Glide.with(this).load("https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xtf1/t51.2885-15/sh0.08/e35/p640x640/11377778_1678871282346140_1451965866_n.jpg")
                .crossFade().centerCrop().into(mImageBreakfast);
        Glide.with(this).load("https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-15/e35/12237452_1036452903083192_2102231120_n.jpg")
                .crossFade().centerCrop().into(mImageLunch);
        Glide.with(this).load("https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xap1/t51.2885-15/e35/12224493_553293684823468_894543425_n.jpg")
                .crossFade().centerCrop().into(mImageDinner);
        Glide.with(this).load("https://igcdn-photos-f-a.akamaihd.net/hphotos-ak-xtp1/t51.2885-15/sh0.08/e35/p640x640/12394013_723004407801613_1182819768_n.jpg")
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

    private void addData() {
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
    public void setMealList(Firebase firebaseMeal) {
        firebaseMeal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Meal> mealList = new ArrayList<Meal>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    for(DataSnapshot finSnapshot: postSnapshot.getChildren()) {
                        Meal meal =
                                new Meal(
                                Long.parseLong(finSnapshot.child("meal_id").getValue() + ""),
                                finSnapshot.child("name").getValue() + "",
                                Double.parseDouble(finSnapshot.child("fat").getValue() + ""),
                                Double.parseDouble(finSnapshot.child("sodium").getValue() + ""),
                                Double.parseDouble(finSnapshot.child("calories").getValue() + ""),
                                Double.parseDouble(finSnapshot.child("cholesterol").getValue() + ""),
                                Double.parseDouble(finSnapshot.child("carbohydrate").getValue() + ""),
                                Double.parseDouble(finSnapshot.child("protein").getValue() + ""),
                                finSnapshot.child("course").getValue() + ""
                        );
                        mealList.add(meal);

                        Log.v(LOG_TAG, postSnapshot.toString());
                    }
                }

                MealPlanFragment.mealList = mealList;

                if (mealList.size() != 0) {
                    mealPlanPresenter.checkCourseValidity();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
        double calConsumed = bkf + lch + dnr + snk ;
        double calRemaining = rdi - calConsumed;
        //Update Calorie Views in Summary
        mTextCalRdi.setText(String.format("%.2f", rdi));
        mTextCalRemaining.setText(String.format("%.2f", calRemaining));
        mTextCalConsumed.setText(String.format("%.2f", calConsumed));

        if(calConsumed > rdi)
            mTextCalConsumed.setTextColor(getResources().getColor(R.color.error_red));
        if(calRemaining < 0)
            mTextCalRemaining.setTextColor(getResources().getColor(R.color.error_red));
    }
}
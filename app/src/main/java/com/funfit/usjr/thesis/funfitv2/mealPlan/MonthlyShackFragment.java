package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.util.Util;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.Monthly;
import com.funfit.usjr.thesis.funfitv2.model.MonthlyCal;
import com.funfit.usjr.thesis.funfitv2.model.Runs;
import com.funfit.usjr.thesis.funfitv2.model.Weekly;
import com.funfit.usjr.thesis.funfitv2.model.WeeklyCal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.viewmods.DarkDividerItemDecoration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dj on 3/3/2016.
 */
public class MonthlyShackFragment extends Fragment {
    private static final String LOG_TAG = MonthlyShackFragment.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab_switch)
    FloatingActionButton mFabSwitch;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    private MonthlyAdapter mAdapter;
    private SharedPreferences mUserPref, mRdiPref;
    int mealId = 0;

    MealDbHelper mealDbHelper;
    RunDbHelper runDbHelper;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shack, container, false);
        ButterKnife.bind(this, v);
        mUserPref = getActivity().getSharedPreferences(Constants.USER_PREF_ID, getActivity().MODE_PRIVATE);
        mRdiPref = getActivity().getSharedPreferences(Constants.USER_PREF_ID, getActivity().MODE_PRIVATE);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mRecyclerView.addItemDecoration(new DarkDividerItemDecoration(getActivity()));
        mealDbHelper = new MealDbHelper(getActivity());
        runDbHelper = new RunDbHelper(getActivity());

        fetchRunAndMeals();

        return v;
    }


    boolean isMealReady, isRunReady;
    List<MonthlyCal> monthlyMeal, monthlyRun;
    HashMap<Integer, Double> monthlyConsumed, monthlyBurned;

    public void fetchRunAndMeals() {
        isMealReady = false;
        isRunReady = false;


        mealDbHelper.getMealService().getMeal(getContext().getSharedPreferences(Constants.RDI_PREF_ID, getContext().MODE_PRIVATE)
                        .getString(Constants.UID, ""),
                new Callback<List<ResponseMeal>>() {
                    @Override
                    public void success(List<ResponseMeal> mealModels, Response response) {
                        mealModels = sortMealListByDate(mealModels);
                        monthlyMeal = new ArrayList<MonthlyCal>();
                        monthlyConsumed = new HashMap<Integer, Double>();
                        double calories = 0;
                        for (int x = 0; x < mealModels.size(); x++) {
                            try {
                                calories += mealModels.get(x).getCalories();
                                monthlyConsumed.put(Utils.getWeekOfYear(mealModels.get(x).getDate()),
                                        mealModels.get(x).getCalories());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (x != mealModels.size() - 1) {
                                    if (Utils.getMonthOfYear(mealModels.get(x).getDate()) !=
                                            Utils.getMonthOfYear(mealModels.get(x + 1).getDate())) {
                                        monthlyMeal.add(new MonthlyCal(Utils.getMonth(mealModels.get(x).getDate()),
                                                Utils.getYear(mealModels.get(x).getDate()),
                                                calories,
                                                0,
                                                monthlyConsumed,
                                                null));

                                        Log.v(LOG_TAG,"c"+mealModels.get(x).getDate()+", "+Utils.getYear(mealModels.get(x).getDate()));

                                        calories = 0;
                                        monthlyConsumed.clear();
                                    }
                                }
                                if (x == mealModels.size() - 1) {
                                    monthlyMeal.add(new MonthlyCal(Utils.getMonth(mealModels.get(x).getDate()),
                                            Utils.getYear(mealModels.get(x).getDate()),
                                            calories,
                                            0,
                                            monthlyConsumed,
                                            null));

                                    Log.v(LOG_TAG,"c"+mealModels.get(x).getDate()+", "+Utils.getYear(mealModels.get(x).getDate()));

                                    calories = 0;
                                    monthlyConsumed.clear();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        isMealReady = true;

                        if (monthlyMeal.size() != 0 && (isMealReady == true && isRunReady == true)) {
                            displayList(monthlyMeal, monthlyRun);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v(LOG_TAG, error.toString());
                    }
                });


        runDbHelper.getRunService().getRun(getContext().getSharedPreferences(Constants.RDI_PREF_ID, getContext().MODE_PRIVATE)
                        .getString(Constants.UID, ""),
                new Callback<List<Runs>>() {
                    @Override
                    public void success(List<Runs> runModels, Response response) {
                        runModels = sortRunListByDate(runModels);
                        monthlyRun = new ArrayList<MonthlyCal>();
                        monthlyBurned = new HashMap<Integer, Double>();
                        double calories = 0;

                        for (int x = 0; x < runModels.size(); x++) {
                            try {
                                double weight = Utils.checkWeight(mUserPref.getString(Constants.PROFILE_WEIGHT, ""));
                                calories += Utils.getCaloriesBurned(weight,
                                        runModels.get(x).getTime(),
                                        runModels.get(x).getDistance());
                                monthlyBurned.put(Utils.getWeekOfYear(Utils.getLastDay(runModels.get(x).getDate())), calories);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (x != runModels.size() - 1) {
                                    if (Utils.getMonthOfYear(runModels.get(x).getDate()) !=
                                            Utils.getMonthOfYear(runModels.get(x + 1).getDate())) {
                                        monthlyRun.add(new MonthlyCal(Utils.getMonth(runModels.get(x).getDate()),
                                                Utils.getYear(runModels.get(x).getDate()),
                                                0,
                                                calories,
                                                null,
                                                monthlyBurned));
                                        Log.v(LOG_TAG,"r"+runModels.get(x).getDate()+", "+Utils.getYear(runModels.get(x).getDate()));
                                        calories = 0;
                                        monthlyBurned.clear();
                                    }
                                }
                                if (x == runModels.size() - 1) {
                                    monthlyRun.add(new MonthlyCal(Utils.getMonth(runModels.get(x).getDate()),
                                            Utils.getYear(runModels.get(x).getDate()),
                                            0,
                                            calories,
                                            null,
                                            monthlyBurned));
                                    Log.v(LOG_TAG,"r"+runModels.get(x).getDate()+", "+Utils.getYear(runModels.get(x).getDate()));
                                    calories = 0;
                                    monthlyBurned.clear();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        isRunReady = true;
                        if (monthlyRun.size() != 0 && (isMealReady == true && isRunReady == true)) {
                            displayList(monthlyMeal, monthlyRun);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    private List<Runs> sortRunListByDate(List<Runs> runModels) {

        Collections.sort(runModels, new Comparator<Runs>() {
            public int compare(Runs m1, Runs m2) {
                try {
                    Date d1 = Utils.getDateValue(m1.getDate());
                    Date d2 = Utils.getDateValue(m2.getDate());
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return runModels;
    }

    private List<ResponseMeal> sortMealListByDate(List<ResponseMeal> mealModels) {

        Collections.sort(mealModels, new Comparator<ResponseMeal>() {
            public int compare(ResponseMeal m1, ResponseMeal m2) {
                try {
                    Date d1 = Utils.getDateValue(m1.getDate());
                    Date d2 = Utils.getDateValue(m2.getDate());
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return mealModels;
    }

    private List<MonthlyCal> sortCalListByDate(List<MonthlyCal> monthlyCals) {

        Collections.sort(monthlyCals, new Comparator<MonthlyCal>() {
            public int compare(MonthlyCal m1, MonthlyCal m2) {
                try {
                    Date d1 = Utils.getMonthValue(m1.getMonth(), m1.getYear());
                    Date d2 = Utils.getMonthValue(m1.getMonth(), m1.getYear());
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        Collections.sort(monthlyCals, Collections.reverseOrder());

        return monthlyCals;
    }

    private void displayList(List<MonthlyCal> monthlyMeal, List<MonthlyCal> monthlyRun) {
        for (int x = 0; x < monthlyMeal.size(); x++) {
            for (int y = 0; y < monthlyRun.size(); y++) {
                Log.v(LOG_TAG,monthlyMeal.get(x).getMonth()+"="+monthlyRun.get(y).getMonth()+"\n"+
                        monthlyMeal.get(x).getYear()+"="+monthlyRun.get(y).getYear());
                if (monthlyMeal.get(x).getMonth().equals(monthlyRun.get(y).getMonth())
                        && (monthlyMeal.get(x).getYear() == monthlyRun.get(y).getYear())) {
                    Log.v(LOG_TAG,"HIT");
                    monthlyMeal.get(x).setBurnedCalories(monthlyRun.get(y).getBurnedCalories());
                    monthlyMeal.get(x).setMonthlyBurnedValue(monthlyRun.get(y).getMonthlyBurnedValue());
                    monthlyMeal.get(x).setMonthlyBurnedWeek(monthlyRun.get(y).getMonthlyBurnedWeek());
                    monthlyRun.remove(y);
                    --y;
                }
            }
        }
        for (int y = 0; y < monthlyRun.size(); y++) {
            monthlyMeal.add(monthlyRun.get(y));
        }
        mAdapter = new MonthlyAdapter(monthlyMeal);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @OnClick(R.id.fab_switch)
    public void onFabSwitchClick() {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.root_frame, new MealPlanFragment());
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }


}

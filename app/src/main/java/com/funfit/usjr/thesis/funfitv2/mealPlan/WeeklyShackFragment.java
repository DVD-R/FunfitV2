package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.model.Runs;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dj on 3/3/2016.
 */
public class WeeklyShackFragment extends Fragment {
    private static final String LOG_TAG = WeeklyShackFragment.class.getSimpleName();
    private static List<Meal> mealList;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    private WeeklyAdapter mAdapter;
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
        mRdiPref = getActivity().getSharedPreferences(Constants.RDI_PREF_ID, getActivity().MODE_PRIVATE);

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
    List<WeeklyCal> weeklyMeal, weeklyRun;
    HashMap<String, Double> weeklyConsumed, weeklyBurned;

    public void fetchRunAndMeals() {
        isMealReady = false;
        isRunReady = false;
        mealDbHelper.getMealService().getMeal(getContext().getSharedPreferences(Constants.RDI_PREF_ID, getContext().MODE_PRIVATE)
                        .getString(Constants.UID, ""),
                new Callback<List<ResponseMeal>>() {
                    @Override
                    public void success(List<ResponseMeal> mealModels, Response response) {
                        mealModels = sortMealListByDate(mealModels);
                        weeklyMeal = new ArrayList<WeeklyCal>();
                        weeklyConsumed = new HashMap<String, Double>();
                        double calories = 0;

                        try {
                            for (int x = 0; x < mealModels.size(); x++) {
                                if (Utils.getYear(mealModels.get(x).getDate()) == 2016) {
                                    calories += mealModels.get(x).getCalories();
                                    weeklyConsumed.put(Utils.getDayOfWeek(mealModels.get(x).getDate()),
                                            mealModels.get(x).getCalories());

                                    if (x != mealModels.size() - 1) {
                                        if (Utils.getWeekOfYear(mealModels.get(x).getDate()) !=
                                                Utils.getWeekOfYear(mealModels.get(x + 1).getDate())) {
                                            weeklyMeal.add(new WeeklyCal(
                                                    Utils.getFirstDay(mealModels.get(x).getDate()),
                                                    Utils.getLastDay(mealModels.get(x).getDate()),
                                                    calories,
                                                    0,
                                                    weeklyConsumed,
                                                    null));

                                            calories = 0;
                                            weeklyConsumed.clear();
                                        }
                                    }
                                    if (x == mealModels.size() - 1) {
                                        weeklyMeal.add(new WeeklyCal(
                                                Utils.getFirstDay(mealModels.get(x).getDate()),
                                                Utils.getLastDay(mealModels.get(x).getDate()),
                                                calories,
                                                0,
                                                weeklyConsumed,
                                                null));

                                        calories = 0;
                                        weeklyConsumed.clear();
                                    }


                                }
                            }
                        } catch (ParseException e) {
                            Log.v(LOG_TAG, e.toString());
                        }

                        isMealReady = true;

                        if (weeklyMeal.size() != 0 && (isMealReady == true && isRunReady == true)) {
                            displayList(weeklyMeal, weeklyRun);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v(LOG_TAG, error.toString());
                    }
                }

        );


        runDbHelper.getRunService().getRun(getContext().getSharedPreferences(Constants.RDI_PREF_ID, getContext().MODE_PRIVATE)
                        .getString(Constants.UID, ""), new Callback<List<Runs>>() {
                    @Override
                    public void success(List<Runs> runModels, Response response) {
                        runModels = sortRunListByDate(runModels);
                        weeklyRun = new ArrayList<WeeklyCal>();
                        weeklyBurned = new HashMap<String, Double>();
                        double calories = 0;

                        try {
                            for (int x = 0; x < runModels.size(); x++) {
                                if (Utils.getYear(runModels.get(x).getDate()) == 2016) {
                                    double weight = Utils.checkWeight(mUserPref.getString(Constants.PROFILE_WEIGHT, ""));
                                    calories += Utils.getCaloriesBurned(weight,
                                            runModels.get(x).getTime(),
                                            runModels.get(x).getDistance());
                                    weeklyBurned.put(Utils.getDayOfWeek(runModels.get(x).getDate()), calories);

                                    if (x != runModels.size() - 1) {
                                        if (Utils.getWeekOfYear(runModels.get(x).getDate()) !=
                                                Utils.getWeekOfYear(runModels.get(x + 1).getDate())) {
                                            weeklyRun.add(new WeeklyCal(
                                                    Utils.getFirstDay(runModels.get(x).getDate()),
                                                    Utils.getLastDay(runModels.get(x).getDate()),
                                                    0,
                                                    calories,
                                                    null,
                                                    weeklyBurned));

                                            calories = 0;
                                            weeklyBurned.clear();
                                        }
                                    }
                                    if (x == runModels.size() - 1) {
                                        weeklyRun.add(new WeeklyCal(
                                                Utils.getFirstDay(runModels.get(x).getDate()),
                                                Utils.getLastDay(runModels.get(x).getDate()),
                                                0,
                                                calories,
                                                null,
                                                weeklyBurned));

                                        calories = 0;
                                        weeklyBurned.clear();
                                    }
                                }
                            }
                            isRunReady = true;
                            if (weeklyRun.size() != 0 && (isMealReady == true && isRunReady == true)) {
                                displayList(weeklyMeal, weeklyRun);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }

        );
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

    private List<WeeklyCal> sortCalListByDate(List<WeeklyCal> weeklyCals) {

        Collections.sort(weeklyCals, new Comparator<WeeklyCal>() {
            public int compare(WeeklyCal m1, WeeklyCal m2) {
                try {
                    Date d1 = Utils.getDateValue(m1.getEndDate());
                    Date d2 = Utils.getDateValue(m2.getEndDate());
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
//        Collections.sort(weeklyCals, Collections.reverseOrder());

        return weeklyCals;
    }

    private void displayList(List<WeeklyCal> weeklyMeal, List<WeeklyCal> weeklyRun) {

        for (int x = 0; x < weeklyMeal.size(); x++) {
            for (int y = 0; y < weeklyRun.size(); y++) {
                if (weeklyMeal.get(x).getStartDate().equals(weeklyRun.get(y).getStartDate())) {
                    weeklyMeal.get(x).setBurnedCalories(weeklyRun.get(y).getBurnedCalories());
                    weeklyMeal.get(x).setWeeklyBurnedDay(weeklyRun.get(y).getWeeklyBurnedDay());
                    weeklyMeal.get(x).setWeeklyBurnedValue(weeklyRun.get(y).getWeeklyBurnedValue());
                    weeklyRun.remove(y);
                    --y;
                }
            }
        }
        for (int y = 0; y < weeklyRun.size(); y++) {
            weeklyMeal.add(weeklyRun.get(y));
        }
        mAdapter = new WeeklyAdapter(sortCalListByDate(weeklyMeal),
                Double.parseDouble(mRdiPref.getString(Constants.RDI, "")));
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
}

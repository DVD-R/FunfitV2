package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.funfit.usjr.thesis.funfitv2.model.DailyCal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.viewmods.DarkDividerItemDecoration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dj on 3/10/2016.
 */
public class DailyShackFragment extends Fragment {
    private static final String LOG_TAG = DailyShackFragment.class.getSimpleName();
    private static List<Meal> mealList;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    private DailyAdapter mAdapter;
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
    List<DailyCal> dailyMeal, dailyRun;

    public void fetchRunAndMeals() {
        isMealReady = false;
        isRunReady = false;
        mealDbHelper.getMealService().getMeal(getContext().getSharedPreferences(Constants.RDI_PREF_ID, getContext().MODE_PRIVATE)
                        .getString(Constants.UID, ""),
                new Callback<List<ResponseMeal>>() {
                    @Override
                    public void success(List<ResponseMeal> mealModels, Response response) {
                        mealModels = sortMealListByDate(mealModels);
                        dailyMeal = new ArrayList<DailyCal>();
                        double calories = 0;
                        try {
                            for (int x = 0; x < mealModels.size(); x++) {
                                if (Utils.getYear(mealModels.get(x).getDate()) == 2016) {
                                    calories += mealModels.get(x).getCalories();

                                    if (x != mealModels.size() - 1) {
                                        if (!mealModels.get(x).getDate().substring(0, 2).equals(
                                                mealModels.get(x + 1).getDate().substring(0, 2))) {
                                            dailyMeal.add(new DailyCal(
                                                    mealModels.get(x).getDate().substring(0, 2),
                                                    Utils.getMonth(mealModels.get(x).getDate()),
                                                    calories,
                                                    0));

                                            calories = 0;
                                        }
                                    }
                                    if (x == mealModels.size() - 1) {
                                        dailyMeal.add(new DailyCal(
                                                mealModels.get(x).getDate().substring(0, 2),
                                                Utils.getMonth(mealModels.get(x).getDate()),
                                                calories,
                                                0));

                                        calories = 0;
                                    }
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        isMealReady = true;

                        if (dailyMeal.size() != 0 && (isMealReady == true && isRunReady == true)) {
                            displayList(dailyMeal, dailyRun);
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
                        dailyRun = new ArrayList<DailyCal>();
                        double calories = 0;

                        try {
                            for (int x = 0; x < runModels.size(); x++) {
                                if (Utils.getYear(runModels.get(x).getDate()) == 2016) {
                                    double weight = Utils.checkWeight(mUserPref.getString(Constants.PROFILE_WEIGHT, ""));
                                    calories += Utils.getCaloriesBurned(weight,
                                            runModels.get(x).getTime(),
                                            runModels.get(x).getDistance());

                                    if (x != runModels.size() - 1) {
                                        if (!runModels.get(x).getDate().substring(0, 2).equals(
                                                runModels.get(x + 1).getDate().substring(0, 2))) {

                                            dailyRun.add(new DailyCal(
                                                    runModels.get(x).getDate().substring(0, 2),
                                                    Utils.getMonth(runModels.get(x).getDate()),
                                                    0,
                                                    calories));

                                            calories = 0;
                                        }
                                    }
                                    if (x == runModels.size() - 1) {
                                        dailyRun.add(new DailyCal(
                                                runModels.get(x).getDate().substring(0, 2),
                                                Utils.getMonth(runModels.get(x).getDate()),
                                                0,
                                                calories));

                                        calories = 0;
                                    }

                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        isRunReady = true;
                        if (dailyRun.size() != 0 && (isMealReady == true && isRunReady == true)) {
                            displayList(dailyMeal, dailyRun);
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

    private List<DailyCal> sortCalListByDate(List<DailyCal> DailyCals) {

        Collections.sort(DailyCals, new Comparator<DailyCal>() {
            public int compare(DailyCal m1, DailyCal m2) {
                try {
                    Date d1 = Utils.getDateVal(m1.getDay() + "-" + m1.getMonth() + "-" + "2016");
                    Date d2 = Utils.getDateVal(m2.getDay() + "-" + m2.getMonth() + "-" + "2016");
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return DailyCals;
    }

    private void displayList(List<DailyCal> dailyMeal, List<DailyCal> dailyRun) {
        Log.v(LOG_TAG, "to display");

        for (int x = 0; x < dailyMeal.size(); x++) {
            for (int y = 0; y < dailyRun.size(); y++) {
                if (dailyMeal.get(x).getDay().equals(dailyRun.get(y).getDay()) && dailyMeal.get(x).getMonth().equals(dailyRun.get(y).getMonth())) {
                    dailyMeal.get(x).setBurnedCalories(dailyRun.get(y).getBurnedCalories());
                    dailyRun.remove(y);
                    --y;
                }
            }
        }
        for (int y = 0; y < dailyRun.size(); y++) {
            dailyMeal.add(dailyRun.get(y));
        }
        mAdapter = new DailyAdapter(sortCalListByDate(dailyMeal),
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

    @OnClick(R.id.fab_switch)
    public void onFabSwitchClick() {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.root_frame, new WeeklyShackFragment());
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }
}

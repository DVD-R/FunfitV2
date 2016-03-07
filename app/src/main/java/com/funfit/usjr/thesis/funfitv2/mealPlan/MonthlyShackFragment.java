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
import com.funfit.usjr.thesis.funfitv2.model.Weekly;
import com.funfit.usjr.thesis.funfitv2.model.WeeklyCal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.viewmods.DarkDividerItemDecoration;

import java.text.ParseException;
import java.util.ArrayList;
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

    //MEAL
    MealDbHelper mealDbHelper;

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
                        monthlyMeal = new ArrayList<MonthlyCal>();
                        monthlyConsumed = new HashMap<Integer, Double>();
                        int lastMonth = 0;
                        int lastYear = 0;
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
                                int latestMonth = Utils.getMonthOfYear(mealModels.get(x).getDate());
                                int latestYear = Utils.getYear(mealModels.get(x).getDate());
                                if ((mealModels.size() - 1 == x) || (latestMonth != lastMonth ||
                                        (latestMonth == lastMonth && lastYear != latestYear))) {
                                    monthlyMeal.add(new MonthlyCal(Utils.getMonth(mealModels.get(x).getDate()),
                                            Utils.getYear(mealModels.get(x).getDate()),
                                            calories,
                                            0,
                                            monthlyConsumed,
                                            null));

                                    calories = 0;
                                    monthlyConsumed.clear();
                                }
                                lastMonth = latestMonth;
                                lastYear = latestYear;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            isMealReady = true;

                            if (monthlyMeal.size() != 0 && (isMealReady == true && isRunReady == true)) {
                                displayList(monthlyMeal, monthlyRun);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v(LOG_TAG, error.toString());
                    }
                });


        Firebase mFirebaseRuns = new Firebase(Constants.FIREBASE_URL_RUNS)
                .child(mUserPref.getString(Constants.PROFILE_EMAIL, ""));

        mFirebaseRuns.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                monthlyRun = new ArrayList<MonthlyCal>();
                monthlyBurned = new HashMap<Integer, Double>();
                int lastMonth = 0;
                int lastYear = 0;
                double calories = 0;
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot : daySnapshot.getChildren()) {
                        try {
                            double weight = Utils.checkWeight(mUserPref.getString(Constants.PROFILE_WEIGHT, ""));
                            calories += Utils.getCaloriesBurned(weight,
                                    Long.parseLong(postSnapshot.child("time").getValue() + ""),
                                    Double.parseDouble(postSnapshot.child("distance").getValue() + ""));
                            monthlyBurned.put(Utils.getWeekOfYear(Utils.getLastDay(daySnapshot.getKey())), calories);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        int latestMonth = Utils.getMonthOfYear(daySnapshot.getKey());
                        int latestYear = Utils.getYear(daySnapshot.getKey());
                        if (latestMonth != lastMonth || (latestMonth == lastMonth && lastYear != latestYear)) {
                            monthlyRun.add(new MonthlyCal(Utils.getMonth(daySnapshot.getKey()),
                                    Utils.getYear(daySnapshot.getKey()),
                                    0,
                                    calories,
                                    null,
                                    monthlyBurned));

                            calories = 0;
                            monthlyBurned.clear();
                        }
                        lastMonth = latestMonth;
                        lastYear = latestYear;
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void displayList(List<MonthlyCal> monthlyMeal, List<MonthlyCal> monthlyRun) {
        for (int x = 0; x < monthlyMeal.size(); x++) {
            for (int y = 0; y < monthlyRun.size(); y++) {
                if (monthlyMeal.get(x).getMonth().equals(monthlyRun.get(y).getMonth())
                        && (monthlyMeal.get(x).getYear() == monthlyRun.get(y).getYear())) {
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

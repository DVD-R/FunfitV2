package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
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

/**
 * Created by Dj on 3/3/2016.
 */
public class WeeklyShackFragment extends Fragment {
    private static final String LOG_TAG = WeeklyShackFragment.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mCurrentLayoutManagerType;
    private WeeklyAdapter mAdapter;
    private SharedPreferences mUserPref;

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

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mRecyclerView.addItemDecoration(new DarkDividerItemDecoration(getActivity()));

        fetchRunAndMeals();

        return v;
    }


    boolean isMealReady, isRunReady;
    List<WeeklyCal> weeklyMeal, weeklyRun;
    HashMap<String, Double> weeklyConsumed, weeklyBurned;

    public void fetchRunAndMeals() {
        isMealReady = false;
        isRunReady = false;
        Firebase mFirebaseMeals = new Firebase(Constants.FIREBASE_URL_MEALS)
                .child(mUserPref.getString(Constants.PROFILE_EMAIL, ""));

        mFirebaseMeals.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                weeklyMeal = new ArrayList<WeeklyCal>();
                weeklyConsumed = new HashMap<String, Double>();
                int lastWoy = 0;
                int lastYear = 0;
                double calories = 0;
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot : daySnapshot.getChildren()) {
                        for (DataSnapshot finSnapshot : postSnapshot.getChildren()) {
                            try {
                                calories += Double.parseDouble(finSnapshot.child("calories").getValue() + "");
                                weeklyConsumed.put(Utils.getDayOfWeek(daySnapshot.getKey()),
                                        Double.parseDouble(finSnapshot.child("calories").getValue() + ""));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        int latestWoy = Utils.getWeekOfYear(daySnapshot.getKey());
                        int latestYear = Utils.getYear(daySnapshot.getKey());
                        if (latestWoy != lastWoy || (latestWoy == lastWoy && lastYear != latestYear)) {
                            weeklyMeal.add(new WeeklyCal(
                                    Utils.getFirstDay(daySnapshot.getKey()),
                                    Utils.getLastDay(daySnapshot.getKey()),
                                    calories,
                                    0,
                                    weeklyConsumed,
                                    null));

                            calories = 0;
                            weeklyConsumed.clear();
                        }
                        lastWoy = latestWoy;
                        lastYear = latestYear;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                isMealReady = true;

                if (weeklyMeal.size() != 0 && (isMealReady == true && isRunReady == true)) {
                    displayList(weeklyMeal, weeklyRun);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase mFirebaseRuns = new Firebase(Constants.FIREBASE_URL_RUNS)
                .child(mUserPref.getString(Constants.PROFILE_EMAIL, ""));

        mFirebaseRuns.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                weeklyRun = new ArrayList<WeeklyCal>();
                weeklyBurned = new HashMap<String, Double>();
                int lastWoy = 0;
                int lastYear = 0;
                double calories = 0;
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot timeSnapshot : daySnapshot.getChildren()) {
                        try {
                            double weight = Utils.checkWeight(mUserPref.getString(Constants.PROFILE_WEIGHT, ""));
                            calories += Utils.getCaloriesBurned(weight,
                                    Long.parseLong(timeSnapshot.child("time").getValue() + ""),
                                    Double.parseDouble(timeSnapshot.child("distance").getValue() + ""));
                            weeklyBurned.put(Utils.getDayOfWeek(daySnapshot.getKey()), calories);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        int latestWoy = Utils.getWeekOfYear(daySnapshot.getKey());
                        int latestYear = Utils.getYear(daySnapshot.getKey());
                        if (latestWoy != lastWoy || (latestWoy == lastWoy && lastYear != latestYear)) {
                            weeklyRun.add(new WeeklyCal(
                                    Utils.getFirstDay(daySnapshot.getKey()),
                                    Utils.getLastDay(daySnapshot.getKey()),
                                    0,
                                    calories,
                                    null,
                                    weeklyBurned));

                            calories = 0;
                            weeklyBurned.clear();
                        }
                        lastWoy = latestWoy;
                        lastYear = latestYear;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                isRunReady = true;

                if (weeklyRun.size() != 0 && (isMealReady == true && isRunReady == true)) {
                    displayList(weeklyMeal, weeklyRun);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
        mAdapter = new WeeklyAdapter(weeklyMeal);
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
        trans.replace(R.id.root_frame, new MonthlyShackFragment());
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }
}

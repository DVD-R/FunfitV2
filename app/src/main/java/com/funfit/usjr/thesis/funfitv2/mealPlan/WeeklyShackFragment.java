package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.funfit.usjr.thesis.funfitv2.adapter.ViewPagerAdapter;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.model.Weekly;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.viewmods.DarkDividerItemDecoration;
import com.funfit.usjr.thesis.funfitv2.viewmods.DividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/3/2016.
 */
public class WeeklyShackFragment extends Fragment {
    private static final String LOG_TAG = WeeklyShackFragment.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab_switch)
    FloatingActionButton mFabSwitch;
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

//        List<Weekly> list = new ArrayList<>();
//        list.add(new Weekly(Utils.getCurrentDate(), Utils.getCurrentDate(),"321321","3213213"));
//        list.add(new Weekly(Utils.getCurrentDate(), Utils.getCurrentDate(),"123213","6435432"));
//        list.add(new Weekly(Utils.getCurrentDate(), Utils.getCurrentDate(),"321321","3213213"));
//
//        mAdapter = new WeeklyAdapter(list);
//        mRecyclerView.setAdapter(mAdapter);

        fetchMeals();

        return v;
    }


    public void fetchMeals() {
        Firebase mFirebaseMeals = new Firebase(Constants.FIREBASE_URL_MEALS)
                .child(mUserPref.getString(Constants.PROFILE_EMAIL, ""));

        mFirebaseMeals.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Weekly> weekly = new ArrayList<Weekly>();
                int lwoy = 0;
                double calories = 0;
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot : daySnapshot.getChildren()) {
                        for (DataSnapshot finSnapshot : postSnapshot.getChildren()) {
                            calories += Double.parseDouble(finSnapshot.child("calories").getValue() + "");
                        }
                    }
                    try {
                        int woy = Utils.getWeekOfYear(daySnapshot.getKey());
                        if (woy != lwoy) {
                            weekly.add(new Weekly(Utils.getFirstDay(daySnapshot.getKey()),
                                    Utils.getLastDay(daySnapshot.getKey()),
                                    calories,
                                    calories));

                            calories = 0;
                        }
                        lwoy = woy;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (weekly.size() != 0) {
                    displayList(weekly);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void displayList(List<Weekly> weekly) {
        mAdapter = new WeeklyAdapter(weekly);
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

package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.Monthly;
import com.funfit.usjr.thesis.funfitv2.model.Weekly;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.viewmods.DarkDividerItemDecoration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        fetchMeals();

        return v;
    }


    public void fetchMeals() {
        Firebase mFirebaseMeals = new Firebase(Constants.FIREBASE_URL_MEALS)
                .child(mUserPref.getString(Constants.PROFILE_EMAIL, ""));

        mFirebaseMeals.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Monthly> monthly = new ArrayList<Monthly>();
                int lastMonth = 0;
                double calories = 0;
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot : daySnapshot.getChildren()) {
                        for (DataSnapshot finSnapshot : postSnapshot.getChildren()) {
                            calories += Double.parseDouble(finSnapshot.child("calories").getValue() + "");
                        }
                    }
                    try {
                        int latestMonth = Utils.getMonthOfYear(daySnapshot.getKey());
                        if (latestMonth != lastMonth) {
                            monthly.add(new Monthly(Utils.getMonth(daySnapshot.getKey()),
                                    Utils.getYear(daySnapshot.getKey()),
                                    calories,
                                    calories));

                            calories = 0;
                        }
                        lastMonth = latestMonth;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (monthly.size() != 0) {
                    displayList(monthly);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void displayList(List<Monthly> monthly) {
        mAdapter = new MonthlyAdapter(monthly);
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
    public void onFabSwitchClick(){
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.root_frame, new MealPlanFragment());
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }
}

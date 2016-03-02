package com.funfit.usjr.thesis.funfitv2.searchFragment;


import android.content.Context;
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
import com.funfit.usjr.thesis.funfitv2.adapters.SearchAdapter;
import com.funfit.usjr.thesis.funfitv2.dataManager.MealDbAdapter;
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.search.SearchActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by victor on 1/6/2016.
 */
public class RecentlyEatenSearchFragment extends Fragment implements SearchActivity.DisplayList {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private static final String LOG_TAG = RecentlyEatenSearchFragment.class.getSimpleName();
    private MealDbAdapter mealDbAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mLayoutManagerType;
    private String melTime;
    private List<Food> item;
    private SearchAdapter searchAdapter;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        setRecyclerViewLayoutManager();

        mealDbAdapter = new MealDbAdapter(getActivity());
        mealDbAdapter.openDb();
        List<Meal> mealList = mealDbAdapter.getMeals();
        List<Food> foodList = new ArrayList<>();
        searchAdapter = new SearchAdapter(item, getActivity(), melTime);
        for (int x = mealList.size() - 1; x >= 0; x--) {
            Food food = new Food(mealList.get(x).getMeal_id() + "", mealList.get(x).getmName());
            food.setIsSelected(false);
            foodList.add(food);
        }

        sendFoodList(foodList, "");

        searchAdapter = new SearchAdapter(item, getActivity(), melTime);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mealDbAdapter.closeDb();
        searchAdapter.onDestroy();
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void sendFoodList(final List<Food> items, final String mealTime) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                item = items;
                melTime = mealTime;
                searchAdapter = new SearchAdapter(items, getActivity(), mealTime);
                mRecyclerView.setAdapter(searchAdapter);
            }
        });

    }
}
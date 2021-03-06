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
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.search.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victor on 1/6/2016.
 */
public class SearchFragment extends Fragment implements SearchActivity.DisplayList{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Context context;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutManagerType mLayoutManagerType;
    private SearchAdapter searchAdapter;
    private  List<Food> item;
    private String melTime;
    private enum LayoutManagerType{
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        setRecyclerViewLayoutManager();
        return view;
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
    public void onDestroy() {
        super.onDestroy();
        searchAdapter  = new SearchAdapter(item, context, melTime);
    }

    public void click(){
        List<Food> foods = ((SearchAdapter) searchAdapter).getFoodList();
        for (int i = 0; i < foods.size(); i++)
        {
            Food food  = foods.get(i);
            if (food.isSelected() == true)
            Log.i("Click Size", String.valueOf(food.getFood_name()));
        }
    }

    @Override
    public void sendFoodList(final List<Food> items, final String mealTime) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                item = items;
                melTime = mealTime;
               searchAdapter  = new SearchAdapter(items, context, mealTime);
                mRecyclerView.setAdapter(searchAdapter);
            }
        });
    }
}
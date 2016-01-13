package com.funfit.usjr.thesis.funfitv2.fatSecretImplementation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.views.ISearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/12/2016.
 */
public class FatSecretPresenter {

    private ISearchView iSearchView;
    private Food foodResult;
    private FatSecretSearch mFatSecretSearch;

    public FatSecretPresenter(ISearchView iSearchView) {
        this.iSearchView = iSearchView;
        mFatSecretSearch = new FatSecretSearch();
    }

    public void searchFoodQuery(String item) {
        iSearchView.mProgressInit();
        DoInBackGround(item);
    }

    private void DoInBackGround(final String item) {
        AsyncTask<Void, Void, Food> doInBackground = new AsyncTask<Void, Void, Food>() {
            @Override
            protected Food doInBackground(Void... params) {
                JSONObject food = mFatSecretSearch.searchFood(item);
                JSONArray FOODS_ARRAY;
                List<Food> foods = new ArrayList<Food>();
                Food items = null;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                items = new Food();
                                items.setFood_id(food_items.getString("food_id"));
                                items.setFood_name(food_items.getString("food_name"));
                                foods.add(items);
                            }
                            iSearchView.getFood(foods);
                            iSearchView.mProgressBarGone();
                        }
                    }
                } catch (Exception e) {
                    return null;
                }
                return foodResult;
            }
        };
        doInBackground.execute();
    }
}
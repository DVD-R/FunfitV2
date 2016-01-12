package com.funfit.usjr.thesis.funfitv2.fatSecretImplementation;

import android.os.AsyncTask;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.views.ISearchFragmentView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/12/2016.
 */
public class FatSecretGetPresenter {
    private ISearchFragmentView iSearchFragmentView;
    private FatSecretGet fatSecretGet;
    private FoodServing foodServing;

    public FatSecretGetPresenter(ISearchFragmentView iSearchFragmentView){
            this.iSearchFragmentView = iSearchFragmentView;
            fatSecretGet = new FatSecretGet();
    }

    public void searchFoodWithServings(){
        DoInBackGround();
    }

    private void DoInBackGround(){
        AsyncTask<Void,Void,FoodServing> doInBackGround = new AsyncTask<Void, Void, FoodServing>() {
            @Override
            protected FoodServing doInBackground(Void... params) {
                JSONObject foodItem = fatSecretGet.getFood(iSearchFragmentView.getFoodId());
                Log.e("FatSecret Api", foodItem.length()+"");

                JSONArray FOODS_ARRAY;
                List<Food> foods = new ArrayList<Food>();
                Food items = null;
                JSONObject jsonObjectServing;
                try {
                    if (foodItem != null) {
                        jsonObjectServing = foodItem.getJSONObject("servings");
                        FOODS_ARRAY = jsonObjectServing.getJSONArray("serving");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                Log.i("Size", String.valueOf(food_items.getString("serving_description")));
                            }
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", String.valueOf(e));
                    return null;
                }
                return foodServing;
            }
        };
        doInBackGround.execute();
    }
}
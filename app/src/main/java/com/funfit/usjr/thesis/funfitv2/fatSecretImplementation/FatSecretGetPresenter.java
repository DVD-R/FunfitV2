package com.funfit.usjr.thesis.funfitv2.fatSecretImplementation;

import android.os.AsyncTask;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.dataManager.MealDbAdapter;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.views.ISearchAdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/12/2016.
 */
public class FatSecretGetPresenter {
    private ISearchAdapterView iSearchAdapterView;
    private FatSecretGet fatSecretGet;
    private FoodServing foodServing;
    private MealDbAdapter mealDbAdapter;
    private boolean flag;
    public FatSecretGetPresenter(ISearchAdapterView iSearchAdapterView){
            this.iSearchAdapterView = iSearchAdapterView;
            fatSecretGet = new FatSecretGet();
            mealDbAdapter = new MealDbAdapter(iSearchAdapterView.getContxt());
    }

    public void searchFoodWithServings(boolean flag){
        this.flag = flag;
        DoInBackGround();
    }

    private void DoInBackGround(){
        AsyncTask<Void,Void,FoodServing> doInBackGround = new AsyncTask<Void, Void, FoodServing>() {
            @Override
            protected FoodServing doInBackground(Void... params) {
                JSONObject foodItem = fatSecretGet.getFood(iSearchAdapterView.getFoodId());

                JSONArray FOODS_ARRAY;
                List<FoodServing> foods = new ArrayList<FoodServing>();
                FoodServing items = null;
                JSONObject jsonObjectServing;
                try {
                    if (foodItem != null) {
                        jsonObjectServing = foodItem.getJSONObject("servings");
                        FOODS_ARRAY = jsonObjectServing.getJSONArray("serving");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                items = new FoodServing();
                                items.setCalcium(food_items.getString("calcium"));
                                items.setCalories(food_items.getString("calories"));
                                items.setCarbohydrate(food_items.getString("carbohydrate"));
                                items.setCholesterol(food_items.getString("cholesterol"));
                                items.setFat(food_items.getString("fat"));
                                items.setIron(food_items.getString("iron"));
                                items.setMeasurement_description(food_items.getString("measurement_description"));
                                items.setMetric_serving_amount(food_items.getString("metric_serving_amount"));
                                items.setMetric_serving_unit(food_items.getString("metric_serving_unit"));
                                items.setMonounsaturated_fat(food_items.getString("monounsaturated_fat"));
                                items.setNumber_of_units(food_items.getString("number_of_units"));
                                items.setPolyunsaturated_fat(food_items.getString("polyunsaturated_fat"));
                                items.setPotassium(food_items.getString("potassium"));
                                items.setProtein(food_items.getString("protein"));
                                items.setSaturated_fat(food_items.getString("saturated_fat"));
                                items.setServing_description(food_items.getString("serving_description"));
                                items.setSodium(food_items.getString("sodium"));
                                foods.add(items);
                            }
                        if (flag) {
                            iSearchAdapterView.sendList(foods);
                        }else{
                            iSearchAdapterView.setList(foods);
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

    public void saveMeal(){
        ArrayList<FoodServing> foodServings = (ArrayList<FoodServing>) iSearchAdapterView.getList();
        ArrayList<Meal> mealArrayList = new ArrayList<>();
        Meal meal = null;
        for (FoodServing foodServing1: foodServings){
            meal = new Meal();
            meal.setMeal_id(iSearchAdapterView.getFoodId());
            meal.setmName(iSearchAdapterView.getMealName());
            meal.setFat(Double.parseDouble(foodServing1.getFat()));
            meal.setCholesterol(Double.parseDouble(foodServing1.getCholesterol()));
            meal.setSodium(Double.parseDouble(foodServing1.getSodium()));
            meal.setCarbohydrate(Double.parseDouble(foodServing1.getCarbohydrate()));
            meal.setProtein(Double.parseDouble(foodServing1.getProtein()));
            meal.setCalories(Double.parseDouble(foodServing1.getCalories()));
            meal.setmTime(iSearchAdapterView.getMealTime());
        }
            mealArrayList.add(meal);
            long count = mealDbAdapter.saveMeal(mealArrayList);
            Log.i("Save to Db", String.valueOf(count));
    }

    public void openDb(){
        mealDbAdapter.openDb();
    }

    public void closeDb(){
        mealDbAdapter.closeDb();
    }
}
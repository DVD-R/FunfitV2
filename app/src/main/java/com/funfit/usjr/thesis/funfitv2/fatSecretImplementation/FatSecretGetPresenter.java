package com.funfit.usjr.thesis.funfitv2.fatSecretImplementation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.mealPlan.MealDbHelper;
import com.funfit.usjr.thesis.funfitv2.mealPlan.RequestMeal;
import com.funfit.usjr.thesis.funfitv2.mealPlan.ResponseMeal;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.views.ISearchAdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 1/12/2016.
 */
public class FatSecretGetPresenter {
    private static final String LOG_TAG = FatSecretGetPresenter.class.getSimpleName();
    private ISearchAdapterView iSearchAdapterView;
    private FatSecretGet fatSecretGet;
    private FoodServing foodServing;
    private MealDbHelper mealDbHelper;
    private boolean flag;
    private Context context;

    public FatSecretGetPresenter(ISearchAdapterView iSearchAdapterView, Context context) {
        this.iSearchAdapterView = iSearchAdapterView;
        fatSecretGet = new FatSecretGet();
        mealDbHelper = new MealDbHelper(context);
        this.context = context;
    }

    public void searchFoodWithServings(boolean flag) {
        this.flag = flag;
        DoInBackGround();
    }

    private void DoInBackGround() {
        AsyncTask<Void, Void, FoodServing> doInBackGround = new AsyncTask<Void, Void, FoodServing>() {
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
                            } else {
                                iSearchAdapterView.setList(foods);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", String.valueOf(e));
                    return null;
                }
                return foodServing;
            }
        };
        doInBackGround.execute();
    }

    public void saveMeal() {
        ArrayList<FoodServing> foodServings = (ArrayList<FoodServing>) iSearchAdapterView.getList();
        ArrayList<RequestMeal> mealArrayList = new ArrayList<>();

        RequestMeal meal = null;
        for (FoodServing foodServing1 : foodServings) {
            String date = Utils.getCurrentDate();
            double calories = Double.parseDouble(foodServing1.getCalories());
            double carbohydrate = Double.parseDouble(foodServing1.getCarbohydrate());
            double cholesterol = Double.parseDouble(foodServing1.getCholesterol());
            String course = iSearchAdapterView.getMealTime();
            double fat = Double.parseDouble(foodServing1.getFat());
            String name = iSearchAdapterView.getMealName();
            double protein = Double.parseDouble(foodServing1.getProtein());
            double sodium = Double.parseDouble(foodServing1.getSodium());
            int userId = Integer.parseInt(context.getSharedPreferences(Constants.RDI_PREF_ID, context.MODE_PRIVATE)
                    .getString(Constants.UID,""));
            meal = new RequestMeal(
//                    "09-03-2016",
                    date,
                    calories,
                    carbohydrate,
                    cholesterol,
                    course,
                    fat,
                    name,
                    protein,
                    sodium,
                    userId);

//            Log.v(LOG_TAG, "date:"+date+"\ncalories:"+calories+"\ncarbs:"+carbohydrate+
//            "\ncholesterol:"+cholesterol+"\ncourse:"+course+"\nfat:"+fat+"\nname:"+name+"\nprotein:"+protein
//            +"\nsodium:"+sodium+"\nuserId:"+userId);
        }
        mealArrayList.add(meal);
        mealDbHelper.saveMeal(mealArrayList);
    }
}
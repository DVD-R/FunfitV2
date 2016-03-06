package com.funfit.usjr.thesis.funfitv2.mealPlan;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ocabafox on 3/7/2016.
 */
public interface MealService {

    @GET("/queryMealList")
    public void getMeal(@Query("userId") String id, Callback<List<MealModel>> mealModelCallback);
}

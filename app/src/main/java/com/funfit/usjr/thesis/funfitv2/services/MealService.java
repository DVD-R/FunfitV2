package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.maps.ResponseStatus;
import com.funfit.usjr.thesis.funfitv2.mealPlan.RequestMeal;
import com.funfit.usjr.thesis.funfitv2.mealPlan.ResponseMeal;
import com.funfit.usjr.thesis.funfitv2.model.RunCallback;
import com.funfit.usjr.thesis.funfitv2.services.SendRun;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by ocabafox on 3/7/2016.
 */
public interface MealService {

    @GET("/queryMealList")
    public void getMeal(@Query("userId") String id, Callback<List<ResponseMeal>> mealModelCallback);

    @POST("/saveMeal")
    public void postMeal(@Body RequestMeal sendMeal, Callback<ResponseMeal> mealModelCallback);
}

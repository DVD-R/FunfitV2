package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.mealPlan.RequestMeal;
import com.funfit.usjr.thesis.funfitv2.mealPlan.ResponseMeal;
import com.funfit.usjr.thesis.funfitv2.model.RequestRun;
import com.funfit.usjr.thesis.funfitv2.model.RunCallback;
import com.funfit.usjr.thesis.funfitv2.model.Runs;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface RunService {

    @GET("/queryRun")
    public void getRun(@Query("userId") String id, Callback<List<Runs>> runCallbackCallback);

    @POST("/saveRun")
    public void postRun(@Body RequestRun sendRun, Callback<Runs> runCallbackCallback);
}

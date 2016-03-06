package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.model.Weekly;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by ocabafox on 3/6/2016.
 */
public interface WeeklyService {

    @GET("LINK")
    public void getWeekly(Callback<List<Weekly>> weeklyCallback);
}

package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.model.Territory;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by victor on 2/21/2016.
 */
public interface TerritoryService {
    @Headers("Content-Type: application/json")
    @GET("/funfit-backend/getTerritory")
    void getAppInitialization(Callback<List<Territory>> responseJsonCallback);
}


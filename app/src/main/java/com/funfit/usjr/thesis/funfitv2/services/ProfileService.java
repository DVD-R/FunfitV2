package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.model.ProfileRequestJson;
import com.funfit.usjr.thesis.funfitv2.model.Rdi;
import com.funfit.usjr.thesis.funfitv2.model.ResponseJson;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
/**
 * Created by victor on 1/22/2016.
 */
public interface ProfileService {
    @Headers("Content-Type: application/json")
    @POST("/initiate")
    void getAppInitialization(@Body ProfileRequestJson profileRequestJson,
                              Callback<Rdi> responseJsonCallback);
}
package com.funfit.usjr.thesis.funfitv2.services;

import android.app.DownloadManager;

import com.funfit.usjr.thesis.funfitv2.model.CapturingModel;
import com.funfit.usjr.thesis.funfitv2.model.ProfileRequestJson;
import com.funfit.usjr.thesis.funfitv2.model.Territory;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by ocabafox on 2/9/2016.
 */
public interface CapturingService {

    @Headers("Content-Type: application/json")
    @POST("/captureTerritory")
    void captureTerritory(@Body CapturingModel capturingModel, Callback<List<Territory>> territoryList);
}

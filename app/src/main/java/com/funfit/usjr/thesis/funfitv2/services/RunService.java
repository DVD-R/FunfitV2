package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.model.RunCallback;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by ocabafox on 3/6/2016.
 */
public interface RunService {

    @POST("/saveRun")
    public void postRun(@Body SendRun sendRun, Callback<RunCallback> runCallbackCallback);
}

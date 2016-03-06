package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.model.RunCallback;
import com.funfit.usjr.thesis.funfitv2.model.RunModel;
import com.funfit.usjr.thesis.funfitv2.model.SendRun;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by ocabafox on 3/6/2016.
 */
public interface RunService {

    @POST("")
    public void postRun(@Body SendRun sendRun, Callback<RunCallback> runCallbackCallback);
}

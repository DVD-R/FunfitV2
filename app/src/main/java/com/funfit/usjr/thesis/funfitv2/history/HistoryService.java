package com.funfit.usjr.thesis.funfitv2.history;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by ocabafox on 1/8/2016.
 */
public interface HistoryService {
    @GET("/NewsObject.json")
    void getNewsGame(Callback<HistoryObject> callback);
}

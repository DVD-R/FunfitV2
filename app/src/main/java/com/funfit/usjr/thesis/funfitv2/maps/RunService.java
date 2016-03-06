package com.funfit.usjr.thesis.funfitv2.maps;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by ocabafox on 3/7/2016.
 */
public interface RunService {

    @POST("/saveRun")
    public void postRun(@Body SaveRun saveRun, Callback<ResponseStatus> responseStatusCallback);
}

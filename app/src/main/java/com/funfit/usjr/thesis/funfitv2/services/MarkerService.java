package com.funfit.usjr.thesis.funfitv2.services;




import com.funfit.usjr.thesis.funfitv2.model.MarkerModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;

/**
 * Created by ocabafox on 2/7/2016.
 */
public interface MarkerService {

    @GET("/getMarkers")
    void getMarker(Callback<List<MarkerModel>> markerModelCallback);
}

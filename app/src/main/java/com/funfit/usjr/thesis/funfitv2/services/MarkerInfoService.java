package com.funfit.usjr.thesis.funfitv2.services;


import com.funfit.usjr.thesis.funfitv2.model.MarkerInfoModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by ocabafox on 2/7/2016.
 */
public interface MarkerInfoService {

    @GET("/marker.json")
    void getMarkerInfo(Callback<List<MarkerInfoModel>> markerModelCallback);
}

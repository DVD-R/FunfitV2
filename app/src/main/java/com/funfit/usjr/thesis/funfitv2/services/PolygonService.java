package com.funfit.usjr.thesis.funfitv2.services;

import com.funfit.usjr.thesis.funfitv2.model.PolygonModel;
import com.google.android.gms.maps.model.Polygon;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;

/**
 * Created by ocabafox on 2/5/2016.
 */
public interface PolygonService {

    @GET("")
    public void getPolygon(@Body PolygonModel polygonModel, Callback<Polygon> polygonCallback);

}

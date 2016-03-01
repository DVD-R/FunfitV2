package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import com.funfit.usjr.thesis.funfitv2.model.EventModel;
import com.funfit.usjr.thesis.funfitv2.model.MarkerInfoModel;
import com.funfit.usjr.thesis.funfitv2.model.PolygonModel;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;

/**
 * Created by ocabafox on 3/1/2016.
 */
public interface EventService {

    @GET("/getEvents")
    void populateEvent(Callback<List<EventModel>> callback);
}

package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 2/7/2016.
 */
public class MarkerInfoModel {
    public final long marker_id;
    public final double lat;
    public final double lng;
    public final String name;
    public final long lvl;

    public MarkerInfoModel(long marker_id, double lat, double lng, String name, long lvl){
        this.marker_id = marker_id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.lvl = lvl;
    }
}

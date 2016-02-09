package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 2/7/2016.
 */
public final class MarkerModel {
    public final long id;
    public final double lat;
    public final double lng;
    public final String name;
    public final long lvl;
    public final String cluster_name;
    public final long faction;

    public MarkerModel(long id, double lat, double lng, String name, long lvl, String cluster_name, long faction){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.lvl = lvl;
        this.cluster_name = cluster_name;
        this.faction = faction;
    }
}
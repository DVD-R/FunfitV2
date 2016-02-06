package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 2/7/2016.
 */
public final class MarkerModel {
    public final long marker_id;
    public final double lng;
    public final double lat;
    public final String name;
    public final long lvl;
    public final Cluster_id cluster_id;
    public final Cluster_name cluster_name;

    public MarkerModel(long marker_id, double lng, double lat, String name, long lvl, Cluster_id cluster_id, Cluster_name cluster_name){
        this.marker_id = marker_id;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
        this.lvl = lvl;
        this.cluster_id = cluster_id;
        this.cluster_name = cluster_name;
    }

    public static final class Cluster_id {

        public Cluster_id(){
        }
    }

    public static final class Cluster_name {

        public Cluster_name(){
        }
    }
}
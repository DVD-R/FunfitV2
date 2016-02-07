package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 2/7/2016.
 */
public final class MarkerInfoModel {
    public final long id;
    public final double lat;
    public final double lng;
    public final String name;
    public final long lvl;
    public final Cluster_name cluster_name;
    public final Faction faction;

    public MarkerInfoModel(long id, double lat, double lng, String name, long lvl, Cluster_name cluster_name, Faction faction){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.lvl = lvl;
        this.cluster_name = cluster_name;
        this.faction = faction;
    }

    public static final class Cluster_name {

        public Cluster_name(){
        }
    }

    public static final class Faction {

        public Faction(){
        }
    }
}

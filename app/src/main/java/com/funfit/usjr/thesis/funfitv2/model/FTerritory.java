package com.funfit.usjr.thesis.funfitv2.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ocabafox on 2/22/2016.
 */
public class FTerritory {
    private String name;
    private String main_marker;
    private List<String> coordinates;
    private HashMap<String, Object> timestampConquered;
    private int level;
    private String user_owner;
    private String cluster_owner;
    private String locale;

    public FTerritory(){}

    public FTerritory(String name, String main_marker, List<String> coordinates,
                     HashMap<String, Object> timestampConquered, int level, String user_owner, String cluster_owner, String locale) {
        this.name = name;
        this.main_marker = main_marker;
        this.coordinates = coordinates;
        this.timestampConquered = timestampConquered;
        this.level = level;
        this.user_owner = user_owner;
        this.cluster_owner = cluster_owner;
        this.locale = locale;
    }

    public String getMain_marker() {
        return main_marker;
    }

    public String getLocale() {
        return locale;
    }

    public String getCluster_owner() {
        return cluster_owner;
    }

    public String getName() {
        return name;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public HashMap<String, Object> getTimestampConquered() {
        return timestampConquered;
    }

    public int getLevel() {
        return level;
    }

    public String getUser_owner() {
        return user_owner;
    }
}

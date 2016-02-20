package com.funfit.usjr.thesis.funfitv2.model;

import java.util.HashMap;

/**
 * Created by Dj on 2/20/2016.
 */
public class Territory {
    private String name;
    private String main_marker;
    private HashMap<String, Object> coordinates;
    private HashMap<String, Object> timestampConquered;
    private String level;
    private String user_owner;
    private String cluster_owner;
    private String locale;

    public Territory(String name, String main_marker, HashMap<String, Object> coordinates,
                     HashMap<String, Object> timestampConquered, String level, String user_owner, String cluster_owner, String locale) {
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

    public void setMain_marker(String main_marker) {
        this.main_marker = main_marker;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCluster_owner() {
        return cluster_owner;
    }

    public void setCluster_owner(String cluster_owner) {
        this.cluster_owner = cluster_owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Object> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(HashMap<String, Object> coordinates) {
        this.coordinates = coordinates;
    }

    public HashMap<String, Object> getTimestampConquered() {
        return timestampConquered;
    }

    public void setTimestampConquered(HashMap<String, Object> timestampConquered) {
        this.timestampConquered = timestampConquered;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUser_owner() {
        return user_owner;
    }

    public void setUser_owner(String user_owner) {
        this.user_owner = user_owner;
    }
}

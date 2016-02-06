package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by ocabafox on 2/7/2016.
 */
public final class MarkerModel {
    public final long id;
    public final String location;
    public final String name;
    public final String event;
    public final String status;
    public final String cluster;
    public final long invade;
    public final String eventTitle;

    public MarkerModel(long id, String location, String name, String event, String status, String cluster, long invade, String eventTitle){
        this.id = id;
        this.location = location;
        this.name = name;
        this.event = event;
        this.status = status;
        this.cluster = cluster;
        this.invade = invade;
        this.eventTitle = eventTitle;
    }
}

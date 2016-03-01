package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ocabafox on 3/1/2016.
 */
public final class EventModel implements Serializable {
    public final String eventName;
    public final String locationName;
    public final double latitude;
    public final double longitude;
    public final long eventDate;
    public final String vertices;
    public final long organizerId;
    public final long id;

    public EventModel(String eventName, String locationName, double latitude, double longitude, long eventDate, String vertices, long organizerId, long id){
        this.eventName = eventName;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventDate = eventDate;
        this.vertices = vertices;
        this.organizerId = organizerId;
        this.id = id;
    }
}
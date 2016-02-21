package com.funfit.usjr.thesis.funfitv2.maps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ocabafox on 2/20/2016.
 */
public class LocationData {
    private int id;
    private LatLng location;

    public LocationData(int id, LatLng location){
        this.location = location;
        this.id = id;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

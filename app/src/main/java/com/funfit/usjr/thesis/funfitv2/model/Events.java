package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

/**
 * Created by Dj on 1/22/2016.
 */
public class Events implements Serializable {
    private String eventName;
    private String location;
    private String reward;
    private String img_url;

    public Events(){

    }

    public Events(String eventName, String location, String reward, String img_url) {
        this.eventName = eventName;
        this.location = location;
        this.reward = reward;
        this.img_url = img_url;
    }

    public String getEventName() {
        return eventName;
    }

    public String getLocation() {
        return location;
    }

    public String getReward() {
        return reward;
    }

    public String getImgUrl() {
        return img_url;
    }
}

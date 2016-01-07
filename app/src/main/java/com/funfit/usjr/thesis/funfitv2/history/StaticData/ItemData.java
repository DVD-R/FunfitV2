package com.funfit.usjr.thesis.funfitv2.history.StaticData;

/**
 * Created by ocabafox on 1/8/2016.
 */
public class ItemData {
    private String username;
    private String event;
    private String area;
    private String timeDate;

    public ItemData(String username,String event,String area,String timeDate){

        this.username = username;
        this.event = event;
        this.area = area;
        this.timeDate = timeDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }
}

package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by Dj on 3/4/2016.
 */
public class Weekly {
    private String startDate;
    private String endDate;
    private String consumedCalories;
    private String burnedCalories;

    public Weekly(String startDate, String endDate, String consumedCalories, String burnedCalories) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.consumedCalories = consumedCalories;
        this.burnedCalories = burnedCalories;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getConsumedCalories() {
        return consumedCalories;
    }

    public void setConsumedCalories(String consumedCalories) {
        this.consumedCalories = consumedCalories;
    }

    public String getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(String burnedCalories) {
        this.burnedCalories = burnedCalories;
    }
}

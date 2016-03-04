package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by Dj on 3/4/2016.
 */
public class Weekly {
    private String startDate;
    private String endDate;
    private double consumedCalories;
    private double burnedCalories;

    public Weekly(String startDate, String endDate, double consumedCalories, double burnedCalories) {
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

    public double getConsumedCalories() {
        return consumedCalories;
    }

    public void setConsumedCalories(double consumedCalories) {
        this.consumedCalories = consumedCalories;
    }

    public double getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(double burnedCalories) {
        this.burnedCalories = burnedCalories;
    }
}

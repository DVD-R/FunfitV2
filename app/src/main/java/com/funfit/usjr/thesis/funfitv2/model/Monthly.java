package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by Dj on 3/4/2016.
 */
public class Monthly {
    private String month;
    private int year;
    private double consumedCalories;
    private double burnedCalories;

    public Monthly(String month, int year, double consumedCalories, double burnedCalories) {
        this.month = month;
        this.year = year;
        this.consumedCalories = consumedCalories;
        this.burnedCalories = burnedCalories;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

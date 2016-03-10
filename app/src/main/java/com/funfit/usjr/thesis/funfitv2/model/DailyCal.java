package com.funfit.usjr.thesis.funfitv2.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dj on 3/10/2016.
 */
public class DailyCal implements Serializable {
    private static final String LOG_TAG = DailyCal.class.getSimpleName();
    private String day;
    private String month;
    private double consumedCalories;
    private double burnedCalories;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public DailyCal(String day, String month,
                    double consumedCalories, double burnedCalories) {
        this.day = day;
        this.month = month;
        this.consumedCalories = consumedCalories;
        this.burnedCalories = burnedCalories;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

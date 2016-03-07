package com.funfit.usjr.thesis.funfitv2.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dj on 3/6/2016.
 */
public class WeeklyCal implements Serializable {
    private static final String LOG_TAG = WeeklyCal.class.getSimpleName();
    private String startDate;
    private String endDate;
    private double consumedCalories;
    private double burnedCalories;
    private String[] weeklyConsumedDay;
    private double[] weeklyConsumedValue;
    private String[] weeklyBurnedDay;
    private double[] weeklyBurnedValue;

    public WeeklyCal(String startDate, String endDate,
                     double consumedCalories, double burnedCalories,
                     HashMap<String, Double> weeklyConsumed, HashMap<String, Double> weeklyBurned) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.consumedCalories = consumedCalories;
        this.burnedCalories = burnedCalories;

        if (weeklyConsumed != null) {

            weeklyConsumedDay = new String[weeklyConsumed.size()];
            weeklyConsumedValue = new double[weeklyConsumed.size()];
            for (int x = 0; x < weeklyConsumed.size(); x++) {
                weeklyConsumedDay[x] = (String) weeklyConsumed.keySet().toArray()[x];
                String entrySet = weeklyConsumed.entrySet().toArray()[x]+"";
                weeklyConsumedValue[x] = Double.parseDouble(entrySet.substring(4,entrySet.length()-1));
            }
        }

        if (weeklyBurned != null) {
            weeklyBurnedDay = new String[weeklyBurned.size()];
            weeklyBurnedValue = new double[weeklyBurned.size()];
            for (int x = 0; x < weeklyBurned.size(); x++) {
                weeklyBurnedDay[x] = (String) weeklyBurned.keySet().toArray()[x];
                String entrySet = weeklyBurned.entrySet().toArray()[x]+"";
                weeklyBurnedValue[x] = Double.parseDouble(entrySet.split("=")[1]);
            }
        }
    }

    public String[] getWeeklyConsumedDay() {
        return weeklyConsumedDay;
    }

    public void setWeeklyConsumedDay(String[] weeklyConsumedDay) {
        this.weeklyConsumedDay = weeklyConsumedDay;
    }

    public double[] getWeeklyConsumedValue() {
        return weeklyConsumedValue;
    }

    public void setWeeklyConsumedValue(double[] weeklyConsumedValue) {
        this.weeklyConsumedValue = weeklyConsumedValue;
    }

    public String[] getWeeklyBurnedDay() {
        return weeklyBurnedDay;
    }

    public void setWeeklyBurnedDay(String[] weeklyBurnedDay) {
        this.weeklyBurnedDay = weeklyBurnedDay;
    }

    public double[] getWeeklyBurnedValue() {
        return weeklyBurnedValue;
    }

    public void setWeeklyBurnedValue(double[] weeklyBurnedValue) {
        this.weeklyBurnedValue = weeklyBurnedValue;
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

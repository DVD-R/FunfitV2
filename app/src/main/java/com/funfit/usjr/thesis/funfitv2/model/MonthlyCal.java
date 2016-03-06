package com.funfit.usjr.thesis.funfitv2.model;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Dj on 3/4/2016.
 */
public class MonthlyCal {
    private static final String LOG_TAG = MonthlyCal.class.getSimpleName();
    private String month;
    private int year;
    private double consumedCalories;
    private double burnedCalories;
    private int[] monthlyConsumedWeek;
    private double[] monthlyConsumedValue;
    private int[] monthlyBurnedWeek;
    private double[] monthlyBurnedValue;

    public MonthlyCal(String month, int year, double consumedCalories, double burnedCalories,
                      HashMap<Integer, Double> monthlyConsumed, HashMap<Integer, Double> monthlyBurned) {
        this.month = month;
        this.year = year;
        this.consumedCalories = consumedCalories;
        this.burnedCalories = burnedCalories;

        if (monthlyConsumed != null) {
            monthlyConsumedWeek = new int[monthlyConsumed.size()];
            monthlyConsumedValue = new double[monthlyConsumed.size()];
            for (int x = 0; x < monthlyConsumed.size(); x++) {
                monthlyConsumedWeek[x] = Integer.parseInt(monthlyConsumed.keySet().toArray()[x] + "");
                String entrySet = monthlyConsumed.entrySet().toArray()[x] + "";
                monthlyConsumedValue[x] = Double.parseDouble(entrySet.split("=")[1]);
            }
        }

        if (monthlyBurned != null) {
            monthlyBurnedWeek = new int[monthlyBurned.size()];
            monthlyBurnedValue = new double[monthlyBurned.size()];
            for (int x = 0; x < monthlyBurned.size(); x++) {
                monthlyBurnedWeek[x] = Integer.parseInt(monthlyBurned.keySet().toArray()[x] + "");
                String entrySet = monthlyBurned.entrySet().toArray()[x] + "";
                monthlyBurnedValue[x] = Double.parseDouble(entrySet.split("=")[1]);
            }
        }
    }

    public int[] getMonthlyConsumedWeek() {
        return monthlyConsumedWeek;
    }

    public void setMonthlyConsumedWeek(int[] monthlyConsumedWeek) {
        this.monthlyConsumedWeek = monthlyConsumedWeek;
    }

    public double[] getMonthlyConsumedValue() {
        return monthlyConsumedValue;
    }

    public void setMonthlyConsumedValue(double[] monthlyConsumedValue) {
        this.monthlyConsumedValue = monthlyConsumedValue;
    }

    public int[] getMonthlyBurnedWeek() {
        return monthlyBurnedWeek;
    }

    public void setMonthlyBurnedWeek(int[] monthlyBurnedWeek) {
        this.monthlyBurnedWeek = monthlyBurnedWeek;
    }

    public double[] getMonthlyBurnedValue() {
        return monthlyBurnedValue;
    }

    public void setMonthlyBurnedValue(double[] monthlyBurnedValue) {
        this.monthlyBurnedValue = monthlyBurnedValue;
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

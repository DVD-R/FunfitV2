package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

/**
 * Created by victor on 2/2/2016.
 */
public class Meal implements Serializable{

    private long meal_id;
    private String mName;
    private double fat;
    private double sodium;
    private double calories;
    private double cholesterol;
    private double carbohydrate;
    private double protein;
    private String mTime;

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Meal(){}

    public Meal(long meal_id, double fat, double sodium, double carbohydrate, double protein, double cholesterol, String mTime, String mName, double calories) {
        this.meal_id = meal_id;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.cholesterol = cholesterol;
        this.mTime = mTime;
        this.mName = mName;
        this.calories = calories;
    }

    public long getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(long meal_id) {
        this.meal_id = meal_id;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }
}
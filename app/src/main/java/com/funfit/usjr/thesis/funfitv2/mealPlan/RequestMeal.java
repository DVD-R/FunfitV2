package com.funfit.usjr.thesis.funfitv2.mealPlan;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RequestMeal implements Serializable {

    @JsonProperty
    private String date;
    @JsonProperty
    private double calories;
    @JsonProperty
    private double carbohydrate;
    @JsonProperty
    private double cholesterol;
    @JsonProperty
    private String course;
    @JsonProperty
    private double fat;
    @JsonProperty
    private String name;
    @JsonProperty
    private double protein;
    @JsonProperty
    private double sodium;
    @JsonProperty
    private int userId;

    public RequestMeal() {
    }

    public RequestMeal(String date, double calories, double carbohydrate, double cholesterol, String course, double fat,
                       String name, double protein, double sodium, int userId) {
        super();
        this.date = date;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.cholesterol = cholesterol;
        this.course = course;
        this.fat = fat;
        this.name = name;
        this.protein = protein;
        this.sodium = sodium;
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

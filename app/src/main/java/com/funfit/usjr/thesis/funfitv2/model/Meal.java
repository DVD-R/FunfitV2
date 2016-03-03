package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by victor on 2/2/2016.
 */
public class Meal{

    private long meal_id;
    private String name;
    private double fat;
    private double sodium;
    private double calories;
    private double cholesterol;
    private double carbohydrate;
    private double protein;
    private String course;

    public Meal(){}

    public Meal(long meal_id, String name, double fat, double sodium, double calories,
                double cholesterol, double carbohydrate, double protein, String course) {
        this.meal_id = meal_id;
        this.name = name;
        this.fat = fat;
        this.sodium = sodium;
        this.calories = calories;
        this.cholesterol = cholesterol;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.course = course;
    }

    public long getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(long meal_id) {
        this.meal_id = meal_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
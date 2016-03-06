package com.funfit.usjr.thesis.funfitv2.mealPlan;

/**
 * Created by ocabafox on 3/7/2016.
 */
public final class MealModel {
    public final String date;
    public final long calories;
    public final long carbohydrate;
    public final long cholesterol;
    public final String course;
    public final long fat;
    public final String name;
    public final long protein;
    public final long sodium;

    public MealModel(String date, long calories, long carbohydrate, long cholesterol, String course, long fat, String name, long protein, long sodium){
        this.date = date;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.cholesterol = cholesterol;
        this.course = course;
        this.fat = fat;
        this.name = name;
        this.protein = protein;
        this.sodium = sodium;
    }
}

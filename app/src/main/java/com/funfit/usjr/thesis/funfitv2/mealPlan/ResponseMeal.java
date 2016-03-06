package com.funfit.usjr.thesis.funfitv2.mealPlan;

import java.io.Serializable;

public class ResponseMeal implements Serializable{

	private String date;
	private double calories;
	private double carbohydrate;
	private double cholesterol;
	private String course;
	private double fat;
	private String name;
	private double protein;
	private double sodium;
	
	public ResponseMeal(){}

	public ResponseMeal(String date, double calories, double carbohydrate, double cholesterol, String course, double fat,
			String name, double protein, double sodium) {
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

}

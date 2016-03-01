package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

/**
 * Created by victor on 1/12/2016.
 */
public class Food implements Serializable {
    private String food_id;
    private String food_name;
    private boolean isSelected;

    public Food(){

    }

    public Food(String food_id, String food_name) {
        this.food_id = food_id;
        this.food_name = food_name;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}

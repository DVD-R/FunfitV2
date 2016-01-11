package com.funfit.usjr.thesis.funfitv2.model;

import java.io.Serializable;

/**
 * Created by victor on 1/12/2016.
 */
public class Food implements Serializable {
    private String food_description;
    private String food_id;
    private String food_name;
    private String food_type;
    private String food_url;

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
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

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getFood_url() {
        return food_url;
    }

    public void setFood_url(String food_url) {
        this.food_url = food_url;
    }
}

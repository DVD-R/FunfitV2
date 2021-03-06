package com.funfit.usjr.thesis.funfitv2.views;

import android.content.Context;

import com.funfit.usjr.thesis.funfitv2.model.FoodServing;

import java.util.List;

/**
 * Created by victor on 1/12/2016.
 */
public interface ISearchAdapterView {
    public void sendList(List<FoodServing> items);
    public int getFoodId();
    public Context getContext();
    public void setList(List<FoodServing> items);
    public List<FoodServing> getList();
    public String getMealName();
    public String getMealTime();
}

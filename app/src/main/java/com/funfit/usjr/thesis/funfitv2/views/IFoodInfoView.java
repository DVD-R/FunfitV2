package com.funfit.usjr.thesis.funfitv2.views;

import com.funfit.usjr.thesis.funfitv2.model.FoodServing;

import java.util.List;

/**
 * Created by victor on 1/13/2016.
 */
public interface IFoodInfoView {
    public void populateViews();
    public void setUpListener();
    public void setSpinnerItem(List<String> items);
    public List<FoodServing> getFoodInfoList();
    public void updateNutritionInfo(int position);
    public int getPosition();
}

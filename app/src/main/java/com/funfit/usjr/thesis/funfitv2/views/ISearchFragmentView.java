package com.funfit.usjr.thesis.funfitv2.views;

import com.funfit.usjr.thesis.funfitv2.model.FoodServing;

import java.util.List;

/**
 * Created by victor on 1/12/2016.
 */
public interface ISearchFragmentView {
    public void setItem(List<FoodServing> items);
    public Long getFoodId();

}

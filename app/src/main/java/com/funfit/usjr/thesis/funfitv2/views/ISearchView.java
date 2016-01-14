package com.funfit.usjr.thesis.funfitv2.views;

import com.funfit.usjr.thesis.funfitv2.model.Food;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/12/2016.
 */
public interface ISearchView {
    public void getFood(List<Food> items);
    public void mProgressBarGone();
    public void mProgressInit();
    public String getNewText();
}

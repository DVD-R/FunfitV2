package com.funfit.usjr.thesis.funfitv2.mealPlan;

import com.funfit.usjr.thesis.funfitv2.views.IMealPlanFragmentView;

/**
 * Created by victor on 1/15/2016.
 */
public class MealPlanPresenter {
    private IMealPlanFragmentView iMealPlanFragmentView;

    public MealPlanPresenter(IMealPlanFragmentView iMealPlanFragmentView){
        this.iMealPlanFragmentView = iMealPlanFragmentView;
    }

    public void displayBreakfast(){
        iMealPlanFragmentView.displayBreakfast();
    }

    public void displayLunch(){ iMealPlanFragmentView.displayLunch();}
}
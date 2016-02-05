package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.dataManager.MealDbAdapter;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.views.IMealPlanFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/15/2016.
 */
public class MealPlanPresenter {
    private IMealPlanFragmentView iMealPlanFragmentView;
    private double breakfastTotalkCal;
    private double lunchTotalkCal;
    private double dinnerTotalkCal;
    private double snackTotalkCal;
    private MealDbAdapter mealDbAdapter;

    public MealPlanPresenter(IMealPlanFragmentView iMealPlanFragmentView){
        this.iMealPlanFragmentView = iMealPlanFragmentView;
        this.mealDbAdapter = new MealDbAdapter(iMealPlanFragmentView.getContxt());
    }

    //Collapse/Expand commands<-----------------
    public void displayBreakfast(){iMealPlanFragmentView.displayBreakfast();}

    public void displayLunch(){ iMealPlanFragmentView.displayLunch();}

    public void displayDinner(){iMealPlanFragmentView.displaDinner();}

    public void displaySnack(){iMealPlanFragmentView.displaySnack();}
    //Collapse/Expand commands<-----------------

    public void queryMealList(){
        List<Meal> mealList= mealDbAdapter.getMeals();
        iMealPlanFragmentView.setMealList(mealList);
    }

    public void checkTimeValidity(){
        for(int i = 0; i < iMealPlanFragmentView.getMealList().size(); i++) {
            switch (iMealPlanFragmentView.getMealList().get(i).getmTime()) {
                case "Breakfast":
                    iMealPlanFragmentView.unhideBreakfast();
                    break;
                case "Lunch":
                    iMealPlanFragmentView.unhideLunch();
                    break;
                case "Dinner":
                    iMealPlanFragmentView.unhideDinner();
                    break;
                case "Snack":
                    iMealPlanFragmentView.unhideSnack();
                    break;
            }
        }
    }

    //OPEN DATABASE CONNECTION FOR LOCAL PERSISTENCE<----------
    public void openDb(){
        mealDbAdapter.openDb();
    }
    //OPEN DATABASE CONNECTION FOR LOCAL PERSISTENCE<----------

    //CLOSE DATABASE CONNECTION FOR LOCAL PERSISTENCE<---------
    public void closeDb(){
        mealDbAdapter.closeDb();
    }
    //CLOSE DATABASE CONNECTION FOR LOCAL PERSISTENCE<---------

}
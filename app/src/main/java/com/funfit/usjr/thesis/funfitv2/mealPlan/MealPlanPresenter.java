package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.views.IMealPlanFragmentView;

import java.util.List;

/**
 * Created by victor on 1/15/2016.
 */
public class MealPlanPresenter {
    private static final String LOG_TAG = MealPlanPresenter.class.getSimpleName();
    private IMealPlanFragmentView iMealPlanFragmentView;
    private double breakfastTotalkCal;
    private double lunchTotalkCal;
    private double dinnerTotalkCal;
    private double snackTotalkCal;
    private MealFirebaseHelper mealDbAdapter;

    public MealPlanPresenter(IMealPlanFragmentView iMealPlanFragmentView){
        this.iMealPlanFragmentView = iMealPlanFragmentView;
        this.mealDbAdapter = new MealFirebaseHelper(iMealPlanFragmentView.getContext());
    }

    //Collapse/Expand commands<-----------------
    public void displayBreakfast(){iMealPlanFragmentView.displayBreakfast();}

    public void displayLunch(){ iMealPlanFragmentView.displayLunch();}

    public void displayDinner(){iMealPlanFragmentView.displayDinner();}

    public void displaySnack(){iMealPlanFragmentView.displaySnack();}
    //Collapse/Expand commands<-----------------

    public void queryMealList(){
        iMealPlanFragmentView.setMealList(mealDbAdapter.getFirebaseMeals());
    }

    public void checkCourseValidity(){
        for(int i = 0; i < iMealPlanFragmentView.getMealList().size(); i++) {
            Log.v(LOG_TAG, iMealPlanFragmentView.getMealList().get(i).getCourse());
            switch (iMealPlanFragmentView.getMealList().get(i).getCourse()) {
                case "Breakfast":
                    iMealPlanFragmentView.unhideBreakfast();
                    Log.v(LOG_TAG, "breakfast");
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

}
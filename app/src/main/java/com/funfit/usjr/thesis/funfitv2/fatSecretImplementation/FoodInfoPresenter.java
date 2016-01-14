package com.funfit.usjr.thesis.funfitv2.fatSecretImplementation;

import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.views.IFoodInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/13/2016.
 */
public class FoodInfoPresenter {

    private IFoodInfoView iFoodInfoView;
    private List<String> newEntry;

    public FoodInfoPresenter(IFoodInfoView iFoodInfoView){
        this.iFoodInfoView = iFoodInfoView;
        newEntry = new ArrayList<String>();
    }

    public void onResume(){
        setSpinnerItem();
        iFoodInfoView.populateViews();
        iFoodInfoView.setUpListener();
    }

    private void setSpinnerItem(){
        List<FoodServing> foodServings = iFoodInfoView.getFoodInfoList();
        for (int i=0; i < foodServings.size(); i++){
            newEntry.add(foodServings.get(i).getMeasurement_description());
        }
        iFoodInfoView.setSpinnerItem(newEntry);
    }

    public void updateNutritionInfo(){
        iFoodInfoView.updateNutritionInfo(iFoodInfoView.getPosition());
    }

}

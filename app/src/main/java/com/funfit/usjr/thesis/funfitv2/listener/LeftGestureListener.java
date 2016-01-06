package com.funfit.usjr.thesis.funfitv2.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.funfit.usjr.thesis.funfitv2.healthPreference.HealthPreferenceActivity;
import com.funfit.usjr.thesis.funfitv2.mealPlan.MealPlanActivity;

/**
 * Created by victor on 1/6/2016.
 */
public class LeftGestureListener extends GestureDetector.SimpleOnGestureListener {
    Activity activity;

    public LeftGestureListener(Activity activity){
        this.activity = activity;


    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        if (event2.getX() < event1.getX()){
            Intent intent = new Intent(activity, HealthPreferenceActivity.class);
            activity.startActivity(intent);
        }
        return true;
    }
}

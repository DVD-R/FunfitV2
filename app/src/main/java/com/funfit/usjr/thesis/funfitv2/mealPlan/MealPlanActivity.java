package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.listener.LeftGestureListener;
import com.funfit.usjr.thesis.funfitv2.listener.RightGestureListener;
import com.funfit.usjr.thesis.funfitv2.pieChart.PieChartFragment;

public class MealPlanActivity extends AppCompatActivity {

    GestureDetectorCompat gestureDetectorCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        gestureDetectorCompat = new GestureDetectorCompat(this,new LeftGestureListener(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PieChartFragment()).commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
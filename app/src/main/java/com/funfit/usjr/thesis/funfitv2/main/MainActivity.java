package com.funfit.usjr.thesis.funfitv2.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.mealPlan.MealPlanActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new MealPlanActivity()).commit();
    }
}
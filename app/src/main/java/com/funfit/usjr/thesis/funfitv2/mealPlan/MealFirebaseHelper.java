package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.firebase.client.Firebase;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.Meal;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2/2/2016.
 */
public class MealFirebaseHelper {

    private SharedPreferences userPref;
    private Firebase mFirebaseMeals;

    public MealFirebaseHelper(Context context) {
        userPref = context.getSharedPreferences(Constants.USER_PREF_ID, context.MODE_PRIVATE);
        mFirebaseMeals = new Firebase(Constants.FIREBASE_URL_MEALS)
                .child(userPref.getString(Constants.PROFILE_EMAIL, ""))
                .child(Utils.getCurrentDate());  // day of week if weekly, date if monthly
    }

    public void saveMeal(ArrayList<Meal> mealArrayList) {

        for (Meal meal : mealArrayList) {
            mFirebaseMeals
                    .child(meal.getCourse())
                    .child(meal.getName().replace(" ","_")).setValue(meal);
        }
    }

    public Firebase getFirebaseMeals() {
        return mFirebaseMeals;
    }
}
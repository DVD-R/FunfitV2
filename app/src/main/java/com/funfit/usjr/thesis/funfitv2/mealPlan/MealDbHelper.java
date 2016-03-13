package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.services.MealService;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by victor on 2/2/2016.
 */
public class MealDbHelper {

    private static final String LOG_TAG = MealDbHelper.class.getSimpleName();
    private SharedPreferences pref;
    MealService mealService;

    public MealDbHelper(Context context) {
        pref = context.getSharedPreferences(Constants.RDI_PREF_ID, context.MODE_PRIVATE);


        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Constants.DBROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();
        mealService = restAdapter.create(MealService.class);

    }

    public void saveMeal(ArrayList<RequestMeal> mealArrayList) {

        for (RequestMeal meal : mealArrayList) {
            mealService.postMeal(meal, new Callback<ResponseMeal>() {
                @Override
                public void success(ResponseMeal runCallback, Response response) {
                    Log.v(LOG_TAG, "SUCCESS!!!: "+response.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.v(LOG_TAG, "ERROR!!!: "+error.toString());
                }
            });
        }
    }

    public MealService getMealService() {
        return mealService;
    }
}
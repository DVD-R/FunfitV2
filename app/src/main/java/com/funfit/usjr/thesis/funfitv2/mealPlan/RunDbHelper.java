package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.Runs;
import com.funfit.usjr.thesis.funfitv2.services.MealService;
import com.funfit.usjr.thesis.funfitv2.services.RunService;
import com.funfit.usjr.thesis.funfitv2.services.SendRun;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by Dj on 3/7/2016.
 */
public class RunDbHelper {

    private static final String LOG_TAG = RunDbHelper.class.getSimpleName();
    private SharedPreferences pref;
    RunService runService;

    public RunDbHelper(Context context) {
        pref = context.getSharedPreferences(Constants.RDI_PREF_ID, context.MODE_PRIVATE);


        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Constants.DBROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();
        runService = restAdapter.create(RunService.class);

    }

    public void saveRun(ArrayList<SendRun> runArrayList) {

        for (SendRun run : runArrayList) {
            runService.postRun(run, new Callback<Runs>() {
                @Override
                public void success(Runs runCallback, Response response) {
                    Log.v(LOG_TAG, response.toString());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    public RunService getRunService() {
        return runService;
    }
}
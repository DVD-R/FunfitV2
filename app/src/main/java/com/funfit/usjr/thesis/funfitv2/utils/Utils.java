package com.funfit.usjr.thesis.funfitv2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;

import java.util.Calendar;

/**
 * Created by Dj on 2/4/2016.
 */
public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    public static String getCluster(Context context){
        SharedPreferences pref = context.getSharedPreferences(Constants.USER_PREF_ID, context.MODE_PRIVATE);
        return pref.getString(Constants.PROFILE_CLUSTER,null);
    }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    //weight(kg), time(hour), speed(km/h)
    public static int getCaloriesBurned(int weight, long time, float speed){
        //millisecond to hour
        time = (int) ((time / (1000*60*60)) % 24);
        return (int)((0.0215 * (speed*3) - 0.1765 * (speed*2) + 0.8710 * (speed) + 1.4577) * weight * time);
    }

    public static int getCurrentDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}

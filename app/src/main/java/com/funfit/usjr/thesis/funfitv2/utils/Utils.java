package com.funfit.usjr.thesis.funfitv2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.funfit.usjr.thesis.funfitv2.R;

/**
 * Created by Dj on 2/4/2016.
 */
public class Utils {
    public static String IMPULSE = "impulse";
    public static String VELOCITY = "velocity";

    public static int getClusterAccent(Context context){
        SharedPreferences pref = context.getSharedPreferences("USER_DATA_PREF", context.MODE_PRIVATE);
        if(pref.getString(IMPULSE,null)!=null){
            return R.color.impulse;
        }else
            return R.color.velocity;
    }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}

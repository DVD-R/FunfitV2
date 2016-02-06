package com.funfit.usjr.thesis.funfitv2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;

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
}

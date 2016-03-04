package com.funfit.usjr.thesis.funfitv2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    //weight(kg), time(millisecond), distance(cm)
    public static double getCaloriesBurned(double weight, long time, double distance){
        //millisecond to hour
        time = (int) ((time / (1000*60*60)) % 24);
        //cm to km
        distance = distance / 100000;
        double speed = distance / time;
        return ((0.0215 * (speed*3) - 0.1765 * (speed*2) + 0.8710 * (speed) + 1.4577) * weight * time);
    }

    public static int getCurrentDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return sdf.format(new Date());
    }

    public static int getWeekOfYear(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat w = new SimpleDateFormat("w", Locale.US);
        Date sd = sdf.parse(date);
        return Integer.parseInt((String) w.format(sd));
    }

    public static String getDay(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        return (String) android.text.format.DateFormat.format("dd", sd);
    }

    public static String getMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        return (String) android.text.format.DateFormat.format("MMM", sd);
    }

    public static int getYear(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        return Integer.parseInt((String)android.text.format.DateFormat.format("yyyy", sd));
    }

    public static String getFirstDay(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(sd);
        cal.set(Calendar.DAY_OF_WEEK, 1);

        return (String) android.text.format.DateFormat.format("dd-MM-yyyy", cal);
    }

    public static String getLastDay(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(sd);
        cal.set(Calendar.DAY_OF_WEEK, 7);

        return (String) android.text.format.DateFormat.format("dd-MM-yyyy", cal);
    }


    public static int getMonthOfYear(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        return Integer.parseInt((String) android.text.format.DateFormat.format("MM", sd));
    }

    public static double checkWeight(String weight) {
        String[] data = weight.split(" ");
        if(data[1].equals("kg")){
            return Double.parseDouble(data[0]);
        }else { //lbs
            return Double.parseDouble(data[0]) / 2.2;
        }
    }

    public static double roundOneDecimal(double d){
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.valueOf(twoDForm.format(d));
    }
}

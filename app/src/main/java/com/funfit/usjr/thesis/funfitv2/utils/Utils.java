package com.funfit.usjr.thesis.funfitv2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

    public static String getCluster(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.USER_PREF_ID, context.MODE_PRIVATE);
        return pref.getString(Constants.PROFILE_CLUSTER, null);
    }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    //weight(kg), time(millisecond), distance(cm)
    public static double getCaloriesBurned(double weight, double time, double distance) {
        //millisecond to hour
        double ftime = roundTwo((time / 1000) / 3600);
        //cm to km
        distance = roundTwo(distance / 100000);
        double speed = roundTwo(distance / ftime);
        double burn = (0.0215 * Math.pow(speed, 3) - 0.1765 * Math.pow(speed, 2) + 0.8710 * speed + 1.4577) * weight * ftime;
        return burn;
    }

    public static double roundTwo(double num) {
        DecimalFormat f = new DecimalFormat("##.00");
        return Double.parseDouble(f.format(num));
    }

    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayOfWeek(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat w = new SimpleDateFormat("EEE", Locale.US);
        Date sd = sdf.parse(date);
        return (String) w.format(sd);
    }

    public static Date getDateValue(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return sdf.parse(date);
    }

    public static Date getDateVal(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        return sdf.parse(date);
    }

    public static Date getMonthValue(String month, int year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        Date date = sdf.parse("01-" + month + "-" + year);
        return date;
    }

    public static String getCurrentDate() {
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

        return Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", sd));
    }

    public static int getYearNumber(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        return Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", sd));
    }

    public static int getMonthNumber(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy", Locale.US);
        Date sd = sdf.parse(date);

        return Integer.parseInt((String) android.text.format.DateFormat.format("MM", sd));
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

    public static int[] getWeeksOfMonth(String sMonth, int year) throws ParseException {
        Date monthDate = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(sMonth);
        Calendar mCal = Calendar.getInstance();
        mCal.setTime(monthDate);
        int month = mCal.get(Calendar.MONTH);


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int ndays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks[] = new int[ndays];
        for (int i = 0; i < ndays; i++) {
            weeks[i] = cal.get(Calendar.WEEK_OF_YEAR);
            cal.add(Calendar.DATE, 1);
        }
        return weeks;
    }

    public static String getDateFromWeekNumber(int no, int year) {
        String date;
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.WEEK_OF_YEAR, no);

        date = new SimpleDateFormat("dd").format(now.getTime());

        now.set(Calendar.DAY_OF_WEEK, 7);
        return date + " - " + new SimpleDateFormat("dd").format(now.getTime());
    }

//    public static long getTimestamp(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        return (long)Math.floor(calendar.getTimeInMillis()/2/2);
//    }

    public static double checkWeight(String weight) {
        String[] data = weight.split(" ");
        if (data[1].equals("kg")) {
            return Double.parseDouble(data[0]);
        } else { //lbs
            return Double.parseDouble(data[0]) / 2.2;
        }
    }

    public static double roundOneDecimal(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.valueOf(twoDForm.format(d));
    }

    public static long generateRunId(SharedPreferences rdi) {
        int runId = rdi.getInt(Constants.RUNID, 100);
        rdi.edit().putInt(Constants.RUNID, runId + 1).commit();
        return runId;
    }

    public static int getDayOfMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat w = new SimpleDateFormat("F", Locale.US);
        Date sd = sdf.parse(date);
        return Integer.parseInt((String) w.format(sd));
    }
}

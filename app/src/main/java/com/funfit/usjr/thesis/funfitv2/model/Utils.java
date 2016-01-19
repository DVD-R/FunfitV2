package com.funfit.usjr.thesis.funfitv2.model;

/**
 * Created by Dj on 1/19/2016.
 */
public class Utils {
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}

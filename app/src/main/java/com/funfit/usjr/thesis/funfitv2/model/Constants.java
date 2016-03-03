package com.funfit.usjr.thesis.funfitv2.model;

import com.funfit.usjr.thesis.funfitv2.BuildConfig;

/**
 * Created by Dj on 1/13/2016.
 */
public final class Constants {

    //Constants for Firebase URL
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String LOGINBG_IMG_URL = "http://djunabel.com/images/pics/554_djuna-bel-nike-db8.jpg";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";

    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATION_MEALS = "meals";
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_MEALS = FIREBASE_URL + "/" + FIREBASE_LOCATION_MEALS;

    public static final String GOOGLE_PROVIDER = "google";
    public static final String KEY_GOOGLE_EMAIL = "GOOGLE_EMAIL";
    public static final String PROVIDER_DATA_DISPLAY_NAME = "displayName";

    public static final String FACEBOOK_PROVIDER = "facebook";
    public static final String KEY_FACEBOOK_EMAIL = "FACEBOOK_EMAIL";
    public static final String FB_EMAIL = "email";

    //Shared Pref Data
    public static final String USER_PREF_ID = "user_info";
    public static final String PROFILE_IMG_URL = "img_profile";
    public static final String PROFILE_FNAME = "profile_fname";
    public static final String PROFILE_LNAME = "profile_lname";
    public static final String PROFILE_EMAIL = "profile_email";
    public static final String PROFILE_GENDER = "profile_gender";
    public static final String PROFILE_DOB = "profile_dob";
    public static final String PROFILE_WEIGHT = "profile_weight";
    public static final String PROFILE_HEIGHT = "profile_height";
    public static final String PROFILE_ACTIVITY_LEVEL = "profile_activity_level";
    public static final String PROFILE_CLUSTER = "profile_cluster";
    public static final String GCM_KEY = "gcm_key";

    //Shared Pref Date
    public static final String DATE_PREF_ID = "date_info";
    public static final String WEEK_MONTH = "week_month";
    public static final String DAY_DATE = "day_date";

    //Shared RDI result
    public static final String RDI_PREF_ID = "rdi_info";
    public static final String RDI = "recommended_daily_intake";
    public static final String UID = "userID";

    public final static String SENDER_ID = "209690052574";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
}

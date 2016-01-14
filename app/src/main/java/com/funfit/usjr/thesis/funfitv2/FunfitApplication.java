package com.funfit.usjr.thesis.funfitv2;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Dj on 1/12/2016.
 */
public class FunfitApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

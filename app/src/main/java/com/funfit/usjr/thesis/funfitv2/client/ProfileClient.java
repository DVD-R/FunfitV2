package com.funfit.usjr.thesis.funfitv2.client;

import com.funfit.usjr.thesis.funfitv2.services.ProfileService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by victor on 1/22/2016.
 */
public class ProfileClient {
    public static ProfileService profileService;
    private static final String ROOT = "http://192.168.254.104:8081";
    static {
        setupRestClient();
    }

    private ProfileClient(){}
    public static ProfileService get(){return profileService;}

    private static void setupRestClient(){

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        profileService = restAdapter.create(ProfileService.class);
    }
}
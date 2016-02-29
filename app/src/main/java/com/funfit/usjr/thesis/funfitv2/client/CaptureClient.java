package com.funfit.usjr.thesis.funfitv2.client;

import com.funfit.usjr.thesis.funfitv2.services.CapturingService;
import com.funfit.usjr.thesis.funfitv2.services.ProfileService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by victor on 3/1/2016.
 */
public class CaptureClient {
    public static CapturingService capturingService;
    private static final String ROOT = "https://funfitv2-backend.herokuapp.com";
    static {
        setupRestClient();
    }

    private CaptureClient(){}
    public static CapturingService get(){return capturingService;}
    private static void setupRestClient(){

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        capturingService = restAdapter.create(CapturingService.class);
    }
}

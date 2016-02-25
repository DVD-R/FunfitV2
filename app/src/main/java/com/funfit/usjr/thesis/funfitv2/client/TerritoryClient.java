package com.funfit.usjr.thesis.funfitv2.client;

import com.funfit.usjr.thesis.funfitv2.services.ProfileService;
import com.funfit.usjr.thesis.funfitv2.services.TerritoryService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by victor on 2/21/2016.
 */
public class TerritoryClient {
    public static TerritoryService territoryService;
    private static final String ROOT = "https://funfitv2-backend.herokuapp.com";
    static {
        setupRestClient();
    }

    private TerritoryClient(){}
    public static TerritoryService get(){return territoryService;}

    private static void setupRestClient(){

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        territoryService = restAdapter.create(TerritoryService.class);
    }
}

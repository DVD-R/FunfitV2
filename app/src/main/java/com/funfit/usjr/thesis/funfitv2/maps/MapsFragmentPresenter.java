package com.funfit.usjr.thesis.funfitv2.maps;

import android.os.AsyncTask;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.client.CaptureClient;
import com.funfit.usjr.thesis.funfitv2.client.TerritoryClient;
import com.funfit.usjr.thesis.funfitv2.model.CapturingModel;
import com.funfit.usjr.thesis.funfitv2.model.Territory;
import com.funfit.usjr.thesis.funfitv2.views.IMapFragmentView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by victor on 1/23/2016.
 */
public class MapsFragmentPresenter {

    private IMapFragmentView iMapFragmentView;
    public MapsFragmentPresenter(IMapFragmentView iMapFragmentView){
        this.iMapFragmentView = iMapFragmentView;
    }

    public void populateTerritory(){
        DoInBackgroundTerritoryService();
    }



    private void DoInBackgroundTerritoryService(){
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                queryTerritory();
                return null;
            }
        };
        asyncTask.execute();
    }

    private void queryTerritory(){
        TerritoryClient.get().getAppInitialization(new Callback<List<Territory>>() {
            @Override
            public void success(List<Territory> listTerritory, Response response) {
                if (listTerritory.size() != 0) {
                    iMapFragmentView.setEndcodedPolylineList(listTerritory);
                    iMapFragmentView.populateTerritory();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Query Territory", String.valueOf(error));
            }
        });
    }


    public void captureTerritory(){
        AsyncTask<Void,Void,Void> capture = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Territory();
                return null;
            }
        };
        capture.execute();
    }

    private void Territory(){
        CapturingModel capturingModel = new CapturingModel();
        capturingModel.setFaction_description(iMapFragmentView.getFactionDescription());
        capturingModel.setTerritoryId(iMapFragmentView.getTerritoryId());
        capturingModel.setUserId(iMapFragmentView.getUserId());
        CaptureClient.get().captureTerritory(capturingModel, new Callback<List<Territory>>() {
            @Override
            public void success(List<Territory> listTerritory, Response response) {
                if(listTerritory != null) {
                    iMapFragmentView.setEndcodedPolylineList(listTerritory);
                    iMapFragmentView.populateTerritory();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
package com.funfit.usjr.thesis.funfitv2.main;

import android.os.AsyncTask;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.client.ProfileClient;
import com.funfit.usjr.thesis.funfitv2.model.ProfileRequestJson;
import com.funfit.usjr.thesis.funfitv2.model.ResponseJson;
import com.funfit.usjr.thesis.funfitv2.views.IMainView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by victor on 1/22/2016.
 */
public class MainPresenter {

        private IMainView iMainView;
        private ResponseJson responseJson;
    public MainPresenter(IMainView iMainView){
            this.iMainView = iMainView;
    }


    public void onResume()
    {
//            iMainView.initProgressDialog();
            //DoInBackground();
    }

    private void DoInBackground(){
        AsyncTask<Void,Void,ResponseJson> asyncTask = new AsyncTask<Void, Void, ResponseJson>() {
            @Override
            protected ResponseJson doInBackground(Void... params) {
                run();
                return responseJson;
            }
        };
        asyncTask.execute();
    }

    private void run(){
        ProfileRequestJson profileRequestJson = new ProfileRequestJson();
        profileRequestJson.setFirstname("Victor");
        profileRequestJson.setLastname("Andales");
        profileRequestJson.setAge(22);
        profileRequestJson.setGender("Male");
        profileRequestJson.setActivitylevel(iMainView.getActivityLevel());
        profileRequestJson.setHeight(Double.parseDouble(iMainView.getHeight()));
        profileRequestJson.setWeight(Double.parseDouble(iMainView.getWeight()));
        ProfileClient.get().getAppInitialization(profileRequestJson, new Callback<List<ResponseJson>>() {
            @Override
            public void success(List<ResponseJson> responseJsons, Response response) {
                List<String> endcodedPolyline = new ArrayList<String>();
               for(ResponseJson res: responseJsons){
                   endcodedPolyline.add(res.getEncodePolyline());
               }
                iMainView.setEndcodedPolylineList(endcodedPolyline);
                if (endcodedPolyline.size() !=0)
                    iMainView.sendEncodePolyline();
                    iMainView.hideProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Main presenter: ", String.valueOf(error));
            }
        });
    }
}
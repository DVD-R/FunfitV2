package com.funfit.usjr.thesis.funfitv2.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.client.ProfileClient;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.ProfileRequestJson;
import com.funfit.usjr.thesis.funfitv2.model.Rdi;
import com.funfit.usjr.thesis.funfitv2.model.ResponseJson;
import com.funfit.usjr.thesis.funfitv2.views.IMainView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private SharedPreferences sharedPreferences;
    public MainPresenter(IMainView iMainView){
        this.iMainView = iMainView;
    }


    public void onResume()
    {
//            iMainView.initProgressDialog();
        sharedPreferences = iMainView.getContxt().getSharedPreferences(Constants.RDI_PREF_ID, Context.MODE_PRIVATE);
        DoInBackground();
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

    private void run() {
        ProfileClient.get().getAppInitialization(iMainView.getProfileRequestJson(), new Callback<Rdi>() {
            @Override
            public void success(Rdi rdi, Response response) {
                sharedPreferences.edit().putString(Constants.RDI, String.format("%.2f", rdi.getRdi())).apply();
                sharedPreferences.edit().putString(Constants.UID, String.valueOf(rdi.getUserId())).apply();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Main presenter: ", String.valueOf(error));
            }
        });
    }

    public int calculateAge(String DOB) {
        String[] dOBSplit = DOB.split("/");
        int year = Integer.parseInt(dOBSplit[2].toString());
        int month = Integer.parseInt(dOBSplit[1].toString());
        int day = Integer.parseInt(dOBSplit[0].toString());
        Calendar cal = new GregorianCalendar(year, month, day);
        Calendar now = new GregorianCalendar();
        int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
                || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) > now
                .get(Calendar.DAY_OF_MONTH))) {
            res--;
        }
        return res;
    }

    public void setProfileRequestJson(){
        ProfileRequestJson profileRequestJson = new ProfileRequestJson();
        profileRequestJson.setUserId(String.valueOf((int) Math.floor(System.currentTimeMillis() / 1000L)));
        profileRequestJson.setFirstname(iMainView.getFirstName());
        profileRequestJson.setLastname(iMainView.getLastName());
        profileRequestJson.setAge(iMainView.getAge());
        profileRequestJson.setGender(iMainView.getGender());
        profileRequestJson.setActivitylevel(iMainView.getActivityLevel());
        profileRequestJson.setHeight(Double.parseDouble(iMainView.getHeight()));
        profileRequestJson.setWeight(Double.parseDouble(iMainView.getWeight()));
        profileRequestJson.setEmail(iMainView.getEmail());
        profileRequestJson.setFaction_description(iMainView.getfactionDescription());
        profileRequestJson.setGcmKey(iMainView.getGcmKey());
        iMainView.setProfileRequestJson(profileRequestJson);
    }
}
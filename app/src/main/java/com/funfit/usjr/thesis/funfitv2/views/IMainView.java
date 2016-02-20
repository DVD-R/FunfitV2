package com.funfit.usjr.thesis.funfitv2.views;

import android.content.Context;

import com.funfit.usjr.thesis.funfitv2.model.ProfileRequestJson;

import java.util.List;

/**
 * Created by victor on 1/22/2016.
 */
public interface IMainView {
    public String getHeight();
    public String getWeight();
    public String getActivityLevel();
    public String getFirstName();
    public String getLastName();
    public int getAge();
    public String getGender();
    public String getEmail();
    public String getfactionDescription();
    public String getGcmKey();
    public Context getContxt();
    public void setProfileRequestJson(ProfileRequestJson profileRequestJson);
    public ProfileRequestJson getProfileRequestJson();
    public void sendEncodePolyline();
    public void setEndcodedPolylineList(List<String> encodePolyline);
    public void initProgressDialog();
    public void hideProgressDialog();
}

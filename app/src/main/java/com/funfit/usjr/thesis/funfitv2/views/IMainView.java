package com.funfit.usjr.thesis.funfitv2.views;

import java.util.List;

/**
 * Created by victor on 1/22/2016.
 */
public interface IMainView {
    public String getHeight();
    public String getWeight();
    public String getActivityLevel();
    public void sendEncodePolyline();
    public void setEndcodedPolylineList(List<String> encodePolyline);
    public void initProgressDialog();
    public void hideProgressDialog();
}

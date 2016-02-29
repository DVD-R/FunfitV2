package com.funfit.usjr.thesis.funfitv2.views;

import com.funfit.usjr.thesis.funfitv2.model.Territory;

import java.util.List;

/**
 * Created by victor on 1/23/2016.
 */
public interface IMapFragmentView {
    public void populateTerritory();
    public void setEndcodedPolylineList(List<Territory> listTerritory);
    public String getFactionDescription();
    public int getUserId();
    public int getTerritoryId();
}

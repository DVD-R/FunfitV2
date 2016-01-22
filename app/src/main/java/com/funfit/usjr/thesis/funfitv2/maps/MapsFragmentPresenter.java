package com.funfit.usjr.thesis.funfitv2.maps;

import com.funfit.usjr.thesis.funfitv2.views.IMapFragmentView;

/**
 * Created by victor on 1/23/2016.
 */
public class MapsFragmentPresenter {

    private IMapFragmentView iMapFragmentView;
    public MapsFragmentPresenter(IMapFragmentView iMapFragmentView){
        this.iMapFragmentView = iMapFragmentView;
    }

    public void populateTerritory(){
        iMapFragmentView.populateTerritory();
    }

}

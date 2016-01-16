package com.funfit.usjr.thesis.funfitv2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.HistoryEventCoordinates;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by victor on 1/16/2016.
 */
public class HistoryViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback{

    protected GoogleMap mGoogleMap;
    protected HistoryEventCoordinates mHistoryEventCoordinates;
    private Context mContext;
    TextView mTitle;
    MapView mHistoryMapView;
    TextView mDescription;

    public HistoryViewHolder(Context context, View itemView){
        super(itemView);
        mContext = context;
//        ButterKnife.bind(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.titleTxt);
        mHistoryMapView = (MapView) itemView.findViewById(R.id.historyMapView);
        mDescription = (TextView) itemView.findViewById(R.id.descriptionTxt);

        mHistoryMapView.onCreate(null);
        mHistoryMapView.getMapAsync(this);
    }

    public void setHistoryEventLocation(HistoryEventCoordinates historyEventCoordinates){
            mHistoryEventCoordinates = historyEventCoordinates;

        //If the map is ready, update its content
        if (mGoogleMap != null){
            updateHistoryMapContents();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        MapsInitializer.initialize(mContext);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        //If we have map data, update the map content.
        if (mHistoryEventCoordinates != null){
            updateHistoryMapContents();
        }
    }

    protected void updateHistoryMapContents(){
        //Since the mapView is re-used, need to remove pre-existing mapView features.
        mGoogleMap.clear();

        //Update the mapView feature data and camera position.
        mGoogleMap.addMarker(new MarkerOptions().position(mHistoryEventCoordinates.center));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mHistoryEventCoordinates.center, 16f);
        mGoogleMap.moveCamera(cameraUpdate);
    }

}

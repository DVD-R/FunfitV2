package com.funfit.usjr.thesis.funfitv2.fragmentDialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.maps.MapsFragment;
import com.funfit.usjr.thesis.funfitv2.model.MarkerInfoModel;
import com.funfit.usjr.thesis.funfitv2.services.MarkerInfoService;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by ocabafox on 2/7/2016.
 */
public class markerDialogFragment extends DialogFragment implements MapsFragment.MarkerInterface {
    private static final String ROOT = "http://192.168.1.44:8081/funfit-backend";
    Button button;
    LatLng location;
    List<LatLng> getLocation = new ArrayList<LatLng>();
    MarkerInfoModel markerInfoModel;
    MarkerInfoService markerInfoService;
    View view;
    private int count = 0;
    MapsFragment mapsFragment;
    public markerDialogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_marker_dialog, container, false);
        mapsFragment = new MapsFragment();
        button = (Button) view.findViewById(R.id.btnInvade);
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                new LoadAsyntask().execute();

                Log.i("giatay", "From MarkerDialogFragment: " + getLocation.get(0));

                Intent intent = new Intent();
                intent.putExtra("location", (Serializable) getLocation);
                intent.putExtra("true",true);

                getTargetFragment().onActivityResult(getTargetRequestCode(), MapsFragment.REQUEST_CODE, intent);
                onStop();
            }
        });
        getDialog().setTitle("Marker Title");
        return view;
    }


    @Override
    public void getData(ArrayList<LatLng> location) {
        getLocation = location;
    }

    class LoadAsyntask extends AsyncTask<Void, Void, MarkerInfoModel> {
        @Override
        protected MarkerInfoModel doInBackground(Void... params) {
            setup();
            return markerInfoModel;
        }
    }

    public void setup() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT) // address sa data
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        markerInfoService = restAdapter.create(MarkerInfoService.class);

        markerInfoService.getMarkerInfo(new Callback<List<MarkerInfoModel>>() {
            @Override
            public void success(List<MarkerInfoModel> markerInfoModels, Response response) {
                for(int checker = 0;markerInfoModels.size()>checker; checker++){
                    location = new LatLng(markerInfoModels.get(checker).lat,markerInfoModels.get(checker).lng);
                    for(int checker2 = 0;getLocation.size() > checker2; checker2++){
                        LatLng location2 = new LatLng(getLocation.get(checker2).latitude,getLocation.get(checker2).longitude);
                        if(location.equals(location2)){
                            Toast.makeText(getActivity(), "Thrid "+getLocation.size(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


}

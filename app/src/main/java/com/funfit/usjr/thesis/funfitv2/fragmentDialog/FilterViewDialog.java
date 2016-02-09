package com.funfit.usjr.thesis.funfitv2.fragmentDialog;

import android.content.Intent;
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
import com.funfit.usjr.thesis.funfitv2.model.MarkerModel;
import com.funfit.usjr.thesis.funfitv2.services.MarkerInfoService;
import com.funfit.usjr.thesis.funfitv2.services.MarkerService;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by ocabafox on 2/8/2016.
 */
public class FilterViewDialog extends DialogFragment {
    private static final String ROOT = "http://192.168.1.44:8081/funfit-backend/";
    MarkerModel markerModel;
    MarkerService markerService;
    View view;

    MapsFragment mapsFragment;

    Button faction;
    @Bind(R.id.Area_conquered)
    Button conquered;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter_view_dialog, container, false);
        ButterKnife.bind(this, view);

        mapsFragment = new MapsFragment();

        getDialog().setTitle("Simple Dialog");

        return view;
    }

    @OnClick(R.id.Faction)
    public void faction(){
        Intent intent2 = new Intent();
        intent2.putExtra("filter", "faction");
        intent2.putExtra("true",true);

        getTargetFragment().onActivityResult(getTargetRequestCode(), MapsFragment.REQUEST_CODE, intent2);
        onStop();
    }

    @OnClick(R.id.Area_conquered)
    public void conquered(){
        Intent intent1 = new Intent();
        intent1.putExtra("filter","conquered");

        getTargetFragment().onActivityResult(getTargetRequestCode(), MapsFragment.REQUEST_CODE, intent1);
        onStop();
    }

//    class LoadAsyntask extends AsyncTask<Void, Void, MarkerModel> {
//        @Override
//        protected MarkerModel doInBackground(Void... params) {
////            setup();
//            return markerModel;
//        }
//    }

//    public void setup() {
//        RestAdapter.Builder builder = new RestAdapter.Builder()
//                .setEndpoint(ROOT) // address sa data
//                .setClient(new OkClient(new OkHttpClient()))
//                .setLogLevel(RestAdapter.LogLevel.FULL);
//
//        RestAdapter restAdapter = builder.build();
//        markerService = restAdapter.create(MarkerService.class);
//
//        markerService.getMarker(new Callback<List<MarkerModel>>() {
//            @Override
//            public void success(List<MarkerModel> markerModels, Response response) {
//
//                for (int x = 0; markerModels.size() > x; x++) {
//                    POSITION = new LatLng(markerModels.get(x).lat, markerModels.get(x).lng);
//                    //myMap.addMarker(new MarkerOptions().position(POSITION).title("test "+x));
//
//
//                    myMap.addMarker(new MarkerOptions()
//                            .position(POSITION)
//                            .title(markerModels.get(x).name)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
//
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
//    }
}

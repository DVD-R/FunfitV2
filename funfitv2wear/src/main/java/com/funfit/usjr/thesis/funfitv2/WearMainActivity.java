package com.funfit.usjr.thesis.funfitv2;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.DismissOverlayView;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ocabafox on 2/6/2016.
 */
public class WearMainActivity extends FragmentActivity
        implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationSource,
        LocationListener,
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {

    private DismissOverlayView mDismissOverlay;
    private GoogleApiClient mGoogleApiClient;
    private TextView mMessageView;
    private OnLocationChangedListener mMapLocationListener = null;

    // location accuracy settings
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    GoogleMap myMap;
    PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        //mMessageView = (TextView) findViewById(R.id.message_text);

        final FrameLayout topFrameLayout = (FrameLayout) findViewById(R.id.root_container);
        final FrameLayout mapFrameLayout = (FrameLayout) findViewById(R.id.map_container);
        arrayPoints = new ArrayList<LatLng>();

        topFrameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Call through to super implementation and apply insets
                insets = topFrameLayout.onApplyWindowInsets(insets);

                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                // Add Wearable insets to FrameLayout container holding map as margins
                params.setMargins(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());
                mapFrameLayout.setLayoutParams(params);

                return insets;
            }
        });

        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.intro_text);
        mDismissOverlay.showIntroIfNecessary();

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setLocationSource(this);
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMapLongClickListener(this);
        myMap = map;
    }

    public void showMyLocation(View view) {
        if (mGoogleApiClient.isConnected()) {
            String msg = "Location = "
                    + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Implementation of {@link LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
        //mMessageView.setText("Location = " + location);
        if (mMapLocationListener != null) {
            mMapLocationListener.onLocationChanged(location);

            LatLng line = new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(line, 16);
            myMap.animateCamera(cameraUpdate);

            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.GREEN);
            polylineOptions.width(4);
            arrayPoints.add(line);
            polylineOptions.addAll(arrayPoints);
            myMap.addPolyline(polylineOptions);
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);  // LocationListener

        StaticData staticData = new StaticData();

        PolygonOptions polygonOptions1 = new PolygonOptions();
        polygonOptions1.addAll(staticData.colon1());
        polygonOptions1.strokeColor(getResources().getColor(R.color.filter_impulse));
        polygonOptions1.strokeWidth(7);
        polygonOptions1.fillColor(getResources().getColor(R.color.filter_impulse));
        myMap.addPolygon(polygonOptions1);

        PolygonOptions polygonOptions2 = new PolygonOptions();
        polygonOptions2.addAll(staticData.colon2());
        polygonOptions2.strokeColor(getResources().getColor(R.color.filter_velocity));
        polygonOptions2.strokeWidth(7);
        polygonOptions2.fillColor(getResources().getColor(R.color.filter_velocity));
        myMap.addPolygon(polygonOptions2);


        PolygonOptions polygonOptions4 = new PolygonOptions();
        polygonOptions4.addAll(staticData.USJRbasak());
        polygonOptions4.strokeColor(getResources().getColor(R.color.filter_impulse));
        polygonOptions4.strokeWidth(7);
        polygonOptions4.fillColor(getResources().getColor(R.color.filter_impulse));
        myMap.addPolygon(polygonOptions4);

        PolygonOptions polygonOptions5 = new PolygonOptions();
        polygonOptions5.addAll(staticData.AYALA1());
        polygonOptions5.strokeColor(getResources().getColor(R.color.filter_impulse));
        polygonOptions5.strokeWidth(7);
        polygonOptions5.fillColor(getResources().getColor(R.color.filter_impulse));
        myMap.addPolygon(polygonOptions5);

        PolygonOptions polygonOptions6 = new PolygonOptions();
        polygonOptions6.addAll(staticData.AYALA2());
        polygonOptions6.strokeColor(getResources().getColor(R.color.filter_impulse));
        polygonOptions6.strokeWidth(7);
        polygonOptions6.fillColor(getResources().getColor(R.color.filter_impulse));
        myMap.addPolygon(polygonOptions6);

        PolygonOptions polygonOptions7 = new PolygonOptions();
        polygonOptions7.addAll(staticData.AYALA3());
        polygonOptions7.strokeColor(getResources().getColor(R.color.filter_velocity));
        polygonOptions7.strokeWidth(7);
        polygonOptions7.fillColor(getResources().getColor(R.color.filter_velocity));
        myMap.addPolygon(polygonOptions7);

        PolygonOptions polygonOptions8 = new PolygonOptions();
        polygonOptions8.addAll(staticData.AYALA4());
        polygonOptions8.strokeColor(getResources().getColor(R.color.filter_impulse));
        polygonOptions8.strokeWidth(7);
        polygonOptions8.fillColor(getResources().getColor(R.color.filter_impulse));
        myMap.addPolygon(polygonOptions8);

    }


    @Override
    public void onConnectionSuspended(int cause) {
        // Do nothing
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        return false;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mMapLocationListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mMapLocationListener = null;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mDismissOverlay.show();
    }
}
package com.funfit.usjr.thesis.funfitv2.maps;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.distance.DistanceCalculation;
import com.funfit.usjr.thesis.funfitv2.views.IMapFragmentView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, IMapFragmentView {

    MapView mMapView;
    private GoogleMap myMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 5 secs */
    private ArrayList<LatLng> arrayPoints = null;
    private boolean checkClick = false;
    private final static int ALPHA_ADJUSTMENT = 0x77000000;
    private MapsFragmentPresenter mapsFragmentPresenter;
    DistanceCalculation distanceCalculation;
    PolylineOptions polylineOptions;
    LatLng newLatLng;
    Location location;
    private String OVAL_POLYGON;
    private boolean mBroadcastInfoRegistered;
    private List<String> polylineList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        //instantiate DistanceCalculation
        distanceCalculation = new DistanceCalculation();

//        ButterKnife.inject(getActivity(), view);
        mMapView = (MapView) view.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        arrayPoints = new ArrayList<LatLng>();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        myMap = mMapView.getMap();


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        myMap.setMyLocationEnabled(true);

        mapsFragmentPresenter = new MapsFragmentPresenter(this);
        connectClient();


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        connectClient();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mBroadcastInfoRegistered){
            getActivity().registerReceiver(encodedPolylineBroadcast, new IntentFilter(getString(R.string.broadcast_encodedpolyline)));
            mBroadcastInfoRegistered = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

        if (!mBroadcastInfoRegistered){
            getActivity().unregisterReceiver(encodedPolylineBroadcast);
            mBroadcastInfoRegistered = false;
        }
    }


    private BroadcastReceiver encodedPolylineBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> encodedPolylineList = (List<String>) intent.getSerializableExtra("encodedPolyLine");
            polylineList = encodedPolylineList;
            if (polylineList.size() != 0)
                mapsFragmentPresenter.populateTerritory();
        }
    };


    private void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            myMap.animateCamera(cameraUpdate);
            myMap.setMyLocationEnabled(true);
            polylineOptions = new PolylineOptions();
            //arrayPoints.add(latLng);

            myMap.setOnMapClickListener(this);
            myMap.setOnMapLongClickListener(this);
            myMap.setOnMarkerClickListener(this);
            startLocationUpdates();
        } else {
            Log.e("Location Service: ", "GPS not connected!");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("Connection Service: ", "Location Suspend");
    }

    @Override
    public void onLocationChanged(Location location) {
        newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 18);
        myMap.animateCamera(cameraUpdate);
        myMap.setMyLocationEnabled(true);

//            polylineOptions = new PolylineOptions();
//            polylineOptions.color(Color.GREEN);
//            polylineOptions.width(4);
//            arrayPoints.add(newLatLng);
//            polylineOptions.addAll(arrayPoints);
//            myMap.addPolyline(polylineOptions);



        //get distance

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Log.w("Location Service: ", "Sorry. Location services not available to you");
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (checkClick == false) {
            myMap.addMarker(new MarkerOptions().position(latLng).title("This is my empire"));

            Log.i("location", latLng.toString());
//            if(arrayPoints.size()>=2){
//                //Log.i("distance","Distance: "+distanceCalculation.CalculationByDistance(arrayPoints.get(0),arrayPoints.get(1)));
//            }

            // settin polyline in the map
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.GREEN);
            polylineOptions.width(4);
            arrayPoints.add(latLng);
            polylineOptions.addAll(arrayPoints);
            myMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        myMap.clear();
        arrayPoints.clear();
        checkClick = false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (arrayPoints.get(0).equals(marker.getPosition())) {
            System.out.println("********First Point choose************");
            countPolygonPoints();
        }
        return false;
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
        }

        return false;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void countPolygonPoints() {
        float distanceInMeters;
        double tempSpeed = 0;
        double averageSpeed = 0;
        if (arrayPoints.size() >= 5) {
            checkClick = true;
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(arrayPoints);
            polygonOptions.strokeColor(Color.BLUE);
            polygonOptions.strokeWidth(7);
            polygonOptions.fillColor(Color.CYAN);
            Polygon polygon = myMap.addPolygon(polygonOptions);

            for (int x = 0; arrayPoints.size() - 1 > x; x++) {
                arrayPoints.get(x);
                Location loc1 = new Location("");
                loc1.setLatitude(arrayPoints.get(x).latitude);
                loc1.setLongitude(arrayPoints.get(x).longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(arrayPoints.get(x + 1).latitude);
                loc2.setLongitude(arrayPoints.get(x + 1).longitude);

                distanceInMeters = loc1.distanceTo(loc2);


                Location location = new Location("");
                location.setSpeed(distanceInMeters/5);

                tempSpeed = tempSpeed + location.getSpeed();

                averageSpeed = tempSpeed / arrayPoints.size() - 1;

                Log.i("distanceTo","Distance in meters: "+distanceInMeters+" Speed: "+location.getSpeed());
            }
            Log.i("speed","Speed: "+averageSpeed);
        }
    }

    @Override
    public void populateTerritory() {
//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(10.290060, 123.862453)).zoom(16).build();
//            myMap.animateCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));
//
//        for (String polyline: polylineList) {
//            List<LatLng> oval = PolyUtil.decode(polyline);
//                myMap.addPolygon(new PolygonOptions()
//                        .addAll(oval)
//                        .fillColor(Color.BLUE - ALPHA_ADJUSTMENT)
//                        .strokeColor(Color.BLUE)
//                        .strokeWidth(5));
//            Log.i("Polyline: ", polyline);
//            }
        }


        // Simplified oval polygon
//        tolerance = 10; // meters
//        List simplifiedOval= PolyUtil.simplify(oval, tolerance);
//        mMap.addPolygon(new PolygonOptions()
//                .addAll(simplifiedOval)
//                .fillColor(Color.YELLOW - ALPHA_ADJUSTMENT)
//                .strokeColor(Color.YELLOW)
//                .strokeWidth(5));
}



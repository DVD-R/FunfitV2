package com.funfit.usjr.thesis.funfitv2.maps;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.distance.DistanceCalculation;
import com.funfit.usjr.thesis.funfitv2.fragmentDialog.markerDialogFragment;
import com.funfit.usjr.thesis.funfitv2.mealPlan.RunDbHelper;
import com.funfit.usjr.thesis.funfitv2.model.CapturingModel;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.FTerritory;
import com.funfit.usjr.thesis.funfitv2.model.MarkerModel;
import com.funfit.usjr.thesis.funfitv2.model.RequestRun;
import com.funfit.usjr.thesis.funfitv2.model.Runs;
import com.funfit.usjr.thesis.funfitv2.model.Territory;
import com.funfit.usjr.thesis.funfitv2.services.CapturingService;
import com.funfit.usjr.thesis.funfitv2.services.MarkerService;
import com.funfit.usjr.thesis.funfitv2.services.RunService;
import com.funfit.usjr.thesis.funfitv2.services.SendRun;
import com.funfit.usjr.thesis.funfitv2.utils.StopWatch;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.views.IMapFragmentView;
import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.maps.android.PolyUtil;
import com.squareup.okhttp.OkHttpClient;

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

public class MapsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, IMapFragmentView, OnMapClickListener {
    private static final String LOG_TAG = MapsFragment.class.getSimpleName();
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_CODE2 = 30;
    MapView mMapView;
    private GoogleMap myMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 5 secs */
    private ArrayList<LatLng> arrayPoints = null;
    private ArrayList<LatLng> receivedMarkerPosition;
    private boolean checkClick = false;
    private final static int ALPHA_ADJUSTMENT = 0x77000000;
    private MapsFragmentPresenter mapsFragmentPresenter;
    DistanceCalculation distanceCalculation;
    PolylineOptions polylineOptions;
    LatLng newLatLng;
    Location location;
    private String OVAL_POLYGON;
    private boolean mBroadcastInfoRegistered;
    private List<Territory> listTerritories;
    markerDialogFragment dialogFragment;
    private boolean flag;
    //Polygon checker
    ArrayList<LatLng> clickedMarker;

    //boolean controller.
    private boolean mapController = false;

    private String filter;
    private int territoryId;
    //Capturing
    CapturingModel capturingModel;
    CapturingService capturingService;

    //Get all location
    private ArrayList<LatLng> getAllLocation;
    private ArrayList<LatLng> saveLocation = null;
    float getDistanceInMeters = 0;
    private SharedPreferences userData, rdi;

    ProgressDialog pd;

    boolean checker = false;
    float distance = 0;
    float totalDistanceInMeters = 0;

    StopWatch timer = new StopWatch();
    private RunDbHelper mRunHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        ButterKnife.bind(this, view);
        //instantiate DistanceCalculation
        distanceCalculation = new DistanceCalculation();
        userData = getActivity().getSharedPreferences(Constants.USER_PREF_ID, getActivity().MODE_PRIVATE);
        rdi = getActivity().getSharedPreferences(Constants.RDI_PREF_ID, getActivity().MODE_PRIVATE);
        rdi.edit().putString(Constants.UID,"1457565832").commit();
        mRunHelper = new RunDbHelper(getActivity());

        mMapView = (MapView) view.findViewById(R.id.mapView);
        receivedMarkerPosition = new ArrayList<LatLng>();
        getAllLocation = new ArrayList<LatLng>();
        saveLocation = new ArrayList<LatLng>();
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately
        clickedMarker = new ArrayList<LatLng>();
        arrayPoints = new ArrayList<LatLng>();
        mBroadcastInfoRegistered = false;
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

        connectClient();
        pd = new ProgressDialog(getContext());
        pd.setTitle("Checking location...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        mapsFragmentPresenter = new MapsFragmentPresenter(this);

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
//        mapsFragmentPresenter.populateTerritory();
//        queryTerritories();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void populateTerritory() {
        myMap.clear();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()
                )).zoom(16).build();
        myMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        LatLng start = new LatLng(location.getLatitude(), location.getLongitude());

        for (Territory territory : listTerritories) {

            List<LatLng> oval = PolyUtil.decode(territory.getEncoded_polyline());

            LatLng end = null;
            for (LatLng lng : oval) {
                end = new LatLng(lng.latitude, lng.longitude);

                if (territory.getStatus().equals("uncharted") && territory.getFaction_description() == null) {
                    if (distanceCalculation.distanceLocation(start, end) < 50000) {
                        myMap.addPolygon(new PolygonOptions()
                                .addAll(oval)
                                .fillColor(Color.LTGRAY - ALPHA_ADJUSTMENT)
                                .strokeColor(Color.GRAY)
                                .strokeWidth(5));
                    }
                } else if (territory.getStatus().equals("captured") && territory.getFaction_description().equals("velocity")) {
                    if (distanceCalculation.distanceLocation(start, end) < 50000) {
                        myMap.addPolygon(new PolygonOptions()
                                .addAll(oval)
                                .fillColor(Color.BLUE - ALPHA_ADJUSTMENT)
                                .strokeColor(Color.BLUE)
                                .strokeWidth(5));
                    }
                } else if (territory.getStatus().equals("captured") && territory.getFaction_description().equals("impulse")) {
                    if (distanceCalculation.distanceLocation(start, end) < 50000) {
                        myMap.addPolygon(new PolygonOptions()
                                .addAll(oval)
                                .fillColor(Color.RED - ALPHA_ADJUSTMENT)
                                .strokeColor(Color.RED)
                                .strokeWidth(5));
                    }
                }
            }
        }
    }

    @Override
    public void setEndcodedPolylineList(List<Territory> listTerritories) {
        this.listTerritories = listTerritories;
    }

    @Override
    public String getFactionDescription() {
        return userData.getString(Constants.PROFILE_CLUSTER, null);
    }

    @Override
    public int getUserId() {
        return Integer.parseInt(rdi.getString(Constants.UID, null));
    }

    @Override
    public int getTerritoryId() {
        return territoryId;
    }

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

            case REQUEST_CODE:
                mapController = data.getExtras().containsKey("true");
                receivedMarkerPosition = (ArrayList<LatLng>) data.getExtras().getSerializable("location");

                break;

            case REQUEST_CODE2:
//                mapController = data.getExtras().containsKey("true");
//                receivedMarkerPosition = (ArrayList<LatLng>) data.getExtras().getSerializable("location");
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            try {
                mapsFragmentPresenter.populateTerritory();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            polylineOptions = new PolylineOptions();
            //arrayPoints.add(latLng);
            myMap.setOnMarkerClickListener(this);
            myMap.setOnMapClickListener(this);
            //CustomMarker();
            startLocationUpdates();
            //dialogFragment.setTargetFragment(this, REQUEST_CODE);
            if (pd != null) {
                pd.dismiss();
                timer.start();
            }
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
//        long totalTime = 0;
//        int test = 0;
//        totalTime = timer.getElapsedTimeMilli();
//        Log.i("Time", "Time " + totalTime);
//
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//        polylineOptions = new PolylineOptions();
//        polylineOptions.color(Color.GREEN);
//        polylineOptions.width(4);
//        arrayPoints.add(latLng);
//        polylineOptions.addAll(arrayPoints);
//        myMap.addPolyline(polylineOptions);
//
//
//        List<LatLng> oval = null;
//
//        try {
//
//            for (Territory territory : listTerritories) {
//                oval = PolyUtil.decode(territory.getEncoded_polyline());
//            }
//
//            for (Territory territory : listTerritories) {
//                oval = PolyUtil.decode(territory.getEncoded_polyline());
//
//                LatLng end = null;
//
//                for (LatLng lng : oval) {
//                    end = new LatLng(lng.latitude, lng.longitude);
//
//                    getDistanceInMeters = distanceCalculation.distanceLocation(latLng, end);
//                    Log.i("ilhanan", "distance: " + getDistanceInMeters);
//
//                    Log.i("Check:", latLng + ":" + end);
//
//                    if (getDistanceInMeters < 50) {
//
//                        double containLat = lng.latitude;
//                        double containLong = lng.longitude;
//
//                        LatLng containLocation = new LatLng(containLat, containLong);
//
//                        if (saveLocation.contains(containLocation)) {
//                            if (saveLocation.size() > 4) {
//                                Log.i("testCapture", "first index " + saveLocation.get(0));
//                                Log.i("testCapture", "last index " + end);
////                        Log.i("testCapture", " size: "+saveLocation.get(saveLocation.size()));
//                                if (saveLocation.get(0).equals(end)) {
//                                    Log.i("id", territory.getId() + "");
//                                    territoryId = territory.getId();
//                                    mapsFragmentPresenter.captureTerritory();
//
//                                    for (int x = 0; arrayPoints.size() - 1 > x; x++) {
//                                        LatLng first = new LatLng(arrayPoints.get(x).latitude, arrayPoints.get(x).longitude);
//                                        LatLng second = new LatLng(arrayPoints.get(x + 1).latitude, arrayPoints.get(x + 1).longitude);
//                                        distance = distanceCalculation.distanceLocation(first, second);
//                                        totalDistanceInMeters = totalDistanceInMeters + distance;
//                                    }
////                                    totalDistanceInMeters
//
//                                    Log.i("testCapture", "YES!! Captured");
//                                    Log.i("testCapture", "Total Distance: " + totalDistanceInMeters);
//                                    Log.i("testCapture", "Total Time: " + totalTime);
//                                    timer.stop();
//
//                                    if (test == 0) {
//
//                                        Log.i("testCapture", "I'm in");
//                                        setup(Utils.getCurrentDate(), totalTime, (long)totalDistanceInMeters,
//                                                Utils.generateRunId(rdi), Long.parseLong(rdi.getString(Constants.UID, "")));
//                                        test = 1;
//                                    }
//                                }
//                            }
//                        } else {
//                            double getLat = lng.latitude;
//                            double getLong = lng.longitude;
//
//                            LatLng convertSaveLocation = new LatLng(getLat, getLong);
//
//                            saveLocation.add(convertSaveLocation);
//                            Log.i("testCapture", "inner " + lng);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//
//        }
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

    public void countPolygonPoints(long duration, float speed, float distance) {
        float distanceInMeters;
        double tempSpeed = 0;
        double averageSpeed = 0;
        boolean checkMarker;
        if (arrayPoints.size() >= 5) {
            checkClick = true;
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(arrayPoints);
            polygonOptions.strokeColor(Color.BLUE);
            polygonOptions.strokeWidth(7);
            polygonOptions.fillColor(Color.BLUE);
            Polygon polygon = myMap.addPolygon(polygonOptions);

            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.GREEN);
            polylineOptions.width(4);
            arrayPoints.add(newLatLng);
            polylineOptions.addAll(arrayPoints);
            myMap.addPolyline(polylineOptions);

            //Algo
            for (int controler = 0; receivedMarkerPosition.size() > 0; controler++) {
                checkMarker = PolyUtil.containsLocation(receivedMarkerPosition.get(controler), arrayPoints, false);
                if (checkMarker) {
                    SharedPreferences userPref = getActivity().getSharedPreferences(Constants.USER_PREF_ID, Context.MODE_PRIVATE);
                    String weight = userPref.getString(Constants.PROFILE_WEIGHT, null);

                    Utils.getCaloriesBurned((Integer.parseInt(weight)), duration, speed);
                    mapController = false;


                    new AlertDialog.Builder(getActivity())
                            .setMessage("congratulations you have completed run")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    //send data
                } else {
                    //Marker not in circle
                }
            }
        }
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
    public boolean onMarkerClick(Marker marker) {
        int count = 0;

        //FragmentManager fm = getFragmentManager();
        dialogFragment = new markerDialogFragment();
        dialogFragment.setTargetFragment(this, REQUEST_CODE);
        dialogFragment.show(getFragmentManager(), "Sample Fragment");


        getLocation();
        //getData(marker.getPosition());
        clickedMarker.add(marker.getPosition());
        return false;
    }

    public void getLocation() {
        (dialogFragment).getData(clickedMarker);
    }

    public interface MarkerInterface {
        public void getData(ArrayList<LatLng> location);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        long totalTime = 0;
        int test = 0;
        totalTime = timer.getElapsedTimeMilli();
        Log.i("Time", "Time " + totalTime);

//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.GREEN);
        polylineOptions.width(4);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        myMap.addPolyline(polylineOptions);


        List<LatLng> oval = null;

        try {

            for (Territory territory : listTerritories) {
                oval = PolyUtil.decode(territory.getEncoded_polyline());
            }

            for (Territory territory : listTerritories) {
                oval = PolyUtil.decode(territory.getEncoded_polyline());

                LatLng end = null;

                for (LatLng lng : oval) {
                    end = new LatLng(lng.latitude, lng.longitude);

                    getDistanceInMeters = distanceCalculation.distanceLocation(latLng, end);
                    Log.i("ilhanan", "distance: " + getDistanceInMeters);

                    Log.i("Check:", latLng + ":" + end);

                    if (getDistanceInMeters < 50) {

                        double containLat = lng.latitude;
                        double containLong = lng.longitude;

                        LatLng containLocation = new LatLng(containLat, containLong);

                        if (saveLocation.contains(containLocation)) {
                            if (saveLocation.size() > 4) {
                                Log.i("testCapture", "first index " + saveLocation.get(0));
                                Log.i("testCapture", "last index " + end);
//                        Log.i("testCapture", " size: "+saveLocation.get(saveLocation.size()));
                                if (saveLocation.get(0).equals(end)) {
                                    Log.i("id", territory.getId() + "");
                                    territoryId = territory.getId();
                                    mapsFragmentPresenter.captureTerritory();

                                    for (int x = 0; arrayPoints.size() - 1 > x; x++) {
                                        LatLng first = new LatLng(arrayPoints.get(x).latitude, arrayPoints.get(x).longitude);
                                        LatLng second = new LatLng(arrayPoints.get(x + 1).latitude, arrayPoints.get(x + 1).longitude);
                                        distance = distanceCalculation.distanceLocation(first, second);
                                        totalDistanceInMeters = totalDistanceInMeters + distance;
                                    }
//                                    totalDistanceInMeters

                                    Log.i("testCapture", "YES!! Captured");
                                    Log.i("testCapture", "Total Distance: " + totalDistanceInMeters);
                                    Log.i("testCapture", "Total Time: " + totalTime);
                                    timer.stop();

                                    if (test == 0) {

                                        Log.i("testCapture", "I'm in");
                                        setup(Utils.getCurrentDate(), totalTime, (long)totalDistanceInMeters,
                                                Utils.generateRunId(rdi), Long.parseLong(rdi.getString(Constants.UID, "")));
                                        test = 1;
                                    }
                                }
                            }
                        } else {
                            double getLat = lng.latitude;
                            double getLong = lng.longitude;

                            LatLng convertSaveLocation = new LatLng(getLat, getLong);

                            saveLocation.add(convertSaveLocation);
                            Log.i("testCapture", "inner " + lng);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void setup(String date, long time, long distance, long run_id, long user_id) {
        RequestRun sendRun = new RequestRun(run_id, date, distance, time, user_id);

        Log.i(LOG_TAG, "Added");

        mRunHelper.saveRun(sendRun);
    }

}
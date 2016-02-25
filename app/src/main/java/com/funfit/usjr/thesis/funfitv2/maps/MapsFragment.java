package com.funfit.usjr.thesis.funfitv2.maps;


import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.distance.DistanceCalculation;
import com.funfit.usjr.thesis.funfitv2.fragmentDialog.FilterViewDialog;
import com.funfit.usjr.thesis.funfitv2.fragmentDialog.markerDialogFragment;
import com.funfit.usjr.thesis.funfitv2.model.CapturingModel;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.FTerritory;
import com.funfit.usjr.thesis.funfitv2.model.MarkerModel;
import com.funfit.usjr.thesis.funfitv2.model.Territory;
import com.funfit.usjr.thesis.funfitv2.services.CapturingService;
import com.funfit.usjr.thesis.funfitv2.services.MarkerService;
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
    FilterViewDialog filterViewDialog;
    private boolean flag;
    //Populate marker from database using webservice
    private static final String ROOT = "http://172.20.10.3:8081/funfit-backend";
    //Send Captured Data
    private static final String CAPTUREDROOT = "http://192.168.1.44:8081";
    private MarkerModel markerModel;
    private MarkerService markerService;
    private ArrayList<MarkerModel> arrayMarker;
    LatLng POSITION, ADDPOSITION;
    ArrayList<Marker> markers = new ArrayList<>();

    boolean mBroadcastIsRegistered;

    //Polygon checker
    ArrayList<LatLng> clickedMarker;

    //boolean controller.
    private boolean mapController = false;

    private String filter;

    //Capturing
    CapturingModel capturingModel;
    CapturingService capturingService;

    //Get all location
    private ArrayList<LatLng> getAllLocation;
    private ArrayList<LatLng> saveLocation = null;
    float getDistanceInMeters = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        ButterKnife.bind(this, view);
        //instantiate DistanceCalculation
        distanceCalculation = new DistanceCalculation();

        mMapView = (MapView) view.findViewById(R.id.mapView);
        receivedMarkerPosition = new ArrayList<LatLng>();
        getAllLocation = new ArrayList<LatLng>();
        saveLocation = new ArrayList<LatLng>();
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately
        clickedMarker = new ArrayList<LatLng>();
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

        connectClient();

        //mapsFragmentPresenter = new MapsFragmentPresenter(this);

        queryTerritories();
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
        if (!mBroadcastInfoRegistered) {
            getActivity().registerReceiver(encodedPolylineBroadcast, new IntentFilter(getString(R.string.broadcast_encodedpolyline)));
            mBroadcastInfoRegistered = true;
            myMap = mMapView.getMap();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        if (!mBroadcastInfoRegistered) {
            getActivity().unregisterReceiver(encodedPolylineBroadcast);
            mBroadcastInfoRegistered = false;
        }
    }


    private BroadcastReceiver encodedPolylineBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Territory> listTerritory = (List<Territory>) intent.getSerializableExtra("encodedPolyLine");
            listTerritories = listTerritory;
            if (flag) {
                if (listTerritories.size() != 0)
                    mapsFragmentPresenter.populateTerritory();
            }
        }
    };

    @Override
    public void populateTerritory() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()
                )).zoom(16).build();
        myMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        for (Territory territory : listTerritories) {
            List<LatLng> oval = PolyUtil.decode(territory.getEncoded_polyline());

            if (territory.getStatus().equals("uncharted") && territory.getFaction_description() == null) {
                myMap.addPolygon(new PolygonOptions()
                        .addAll(oval)
                        .fillColor(Color.LTGRAY - ALPHA_ADJUSTMENT)
                        .strokeColor(Color.GRAY)
                        .strokeWidth(5));
            } else if (territory.getStatus().equals("owned") && territory.getFaction_description().equals("velocity")) {
                myMap.addPolygon(new PolygonOptions()
                        .addAll(oval)
                        .fillColor(Color.BLUE - ALPHA_ADJUSTMENT)
                        .strokeColor(Color.BLUE)
                        .strokeWidth(5));
            } else if (territory.getStatus().equals("owned") && territory.getFaction_description().equals("impulse")) {
                myMap.addPolygon(new PolygonOptions()
                        .addAll(oval)
                        .fillColor(Color.RED - ALPHA_ADJUSTMENT)
                        .strokeColor(Color.RED)
                        .strokeWidth(5));
            }
        }
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
                if (listTerritories.size() != 0)
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
//        float getDistanceInMeters = 0;
//        float tempSpeed = 0;
//        float speed = 0;
//        float distance = 0;
//        long tempDuration = 0;
//        long duration = 0;
//
//        if (mapController) {
//
//            newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 16);
//            myMap.animateCamera(cameraUpdate);
//            myMap.setMyLocationEnabled(true);
//
//            // settin polyline in the map
//            polylineOptions = new PolylineOptions();
//            polylineOptions.color(Color.GREEN);
//            polylineOptions.width(4);
//            arrayPoints.add(newLatLng);
//            polylineOptions.addAll(arrayPoints);
//            myMap.addPolyline(polylineOptions);
//
//            for (int x = 0; arrayPoints.size() - 1 > x; x++) {
//                arrayPoints.get(x);
//                Location loc1 = new Location("");
//                loc1.setLatitude(arrayPoints.get(x).latitude);
//                loc1.setLongitude(arrayPoints.get(x).longitude);
//
//                Location loc2 = new Location("");
//                loc2.setLatitude(arrayPoints.get(x + 1).latitude);
//                loc2.setLongitude(arrayPoints.get(x + 1).longitude);
//
//                getDistanceInMeters = getDistanceInMeters + loc1.distanceTo(loc2);
//
//                float displaySpeed = location.getSpeed();
//
//
//            }
//            tempSpeed = tempSpeed + location.getSpeed();
//            duration = duration + location.getTime();
//
//            Log.i("distanceTo", "Distance in meters: " + getDistanceInMeters + " Speed: " + location.getSpeed());
////        }
//
//            Log.i("distanceTo", "Distance in meters: " + getDistanceInMeters);
//            if (getDistanceInMeters > 200) {
//                int controller = arrayPoints.size() - 1;
//                Log.i("distanceTo", "distance: " + distanceCalculation.CalculationByDistance(arrayPoints.get(0), arrayPoints.get(controller)));
//
//                Location loc1 = new Location("");
//                loc1.setLatitude(arrayPoints.get(0).latitude);
//                loc1.setLongitude(arrayPoints.get(0).longitude);
//
//                Location loc2 = new Location("");
//                loc2.setLatitude(arrayPoints.get(controller).latitude);
//                loc2.setLongitude(arrayPoints.get(controller).longitude);
//
//                float distanceCondition = loc1.distanceTo(loc2);
//
//                if (distanceCondition < 20) {
//                    Log.i("location1", "Distance of: " + distanceCalculation.CalculationByDistance(arrayPoints.get(0), arrayPoints.get(controller)));
//
//                    //speed
//                    speed = tempSpeed / (arrayPoints.size() + 1);
//
//                    //distance
//                    for (int calculate = 0; arrayPoints.size() - 1 > calculate; calculate++) {
//                        Location start = new Location("");
//                        loc1.setLatitude(arrayPoints.get(0).latitude);
//                        loc1.setLongitude(arrayPoints.get(0).longitude);
//
//                        Location end = new Location("");
//                        loc2.setLatitude(arrayPoints.get(controller).latitude);
//                        loc2.setLongitude(arrayPoints.get(controller).longitude);
//
//                        distance = distance + start.distanceTo(end);
//                    }
//
//                    arrayPoints.add(arrayPoints.get(0));
//
//                    // long duration, float speed, float distance
//                    countPolygonPoints(duration, speed, distance);
//                }
//            }
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

    @OnClick(R.id.fab_run)
    public void runFab() {
        filterViewDialog = new FilterViewDialog();
        filterViewDialog.setTargetFragment(this, REQUEST_CODE2);
        filterViewDialog.show(getFragmentManager(), "Filter Sample Fragment");
    }

    @OnClick(R.id.fab_search)
    public void searchFab() {

    }

    private void queryTerritories() {
        final Firebase territoryFirebase = new Firebase(Constants.FIREBASE_URL_TERRITORIES);

        territoryFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    myMap.clear();
                    FTerritory fTerritory = postSnapshot.getValue(FTerritory.class);

                    ArrayList<LatLng> getTerritoryForPolygon = new ArrayList<LatLng>();
                    LatLng start = new LatLng(location.getLatitude(), location.getLongitude());

                    String[] getEndLocation = fTerritory.getMain_marker().trim().split(",");
                    double latitude = Double.parseDouble(getEndLocation[0]);
                    double longitude = Double.parseDouble(getEndLocation[1]);

                    LatLng end = new LatLng(latitude, longitude);

                    //Note: 5KM is 5000m
                    if (distanceCalculation.distanceLocation(start, end) < 50000) {
                        Log.i("distance", "aa " + distanceCalculation.distanceLocation(start, end));
                        for (int x = 0; fTerritory.getCoordinates().size() > x; x++) {
                            String[] latlong = fTerritory.getCoordinates().get(x).trim().split(",");
                            double distanceLatitude = Double.parseDouble(latlong[0]);
                            double distanceLongitude = Double.parseDouble(latlong[1]);

                            LatLng distanceLatLng = new LatLng(distanceLatitude, distanceLongitude);

                            getAllLocation.add(distanceLatLng);
                            getTerritoryForPolygon.add(distanceLatLng);
                        }

                        if (fTerritory.getLevel() > 0) {
                            PolygonOptions polygonOptions1 = new PolygonOptions();
                            polygonOptions1.addAll(getTerritoryForPolygon);
                            polygonOptions1.strokeColor(getResources().getColor(R.color.filter_impulse));
                            polygonOptions1.strokeWidth(7);
                            polygonOptions1.fillColor(getResources().getColor(R.color.filter_impulse));
                            myMap.addPolygon(polygonOptions1);
                        } else if (fTerritory.getLevel() < 0) {
                            PolygonOptions polygonOptions1 = new PolygonOptions();
                            polygonOptions1.addAll(getTerritoryForPolygon);
                            polygonOptions1.strokeColor(getResources().getColor(R.color.filter_velocity));
                            polygonOptions1.strokeWidth(7);
                            polygonOptions1.fillColor(getResources().getColor(R.color.filter_velocity));
                            myMap.addPolygon(polygonOptions1);
                        } else {
                            PolygonOptions polygonOptions1 = new PolygonOptions();
                            polygonOptions1.addAll(getTerritoryForPolygon);
                            polygonOptions1.strokeColor(getResources().getColor(R.color.grey));
                            polygonOptions1.strokeWidth(7);
                            polygonOptions1.fillColor(getResources().getColor(R.color.grey));
                            myMap.addPolygon(polygonOptions1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, firebaseError.toString());
            }
        });
    }


    @Override
    public void onMapClick(LatLng latLng) {
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.GREEN);
        polylineOptions.width(4);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        myMap.addPolyline(polylineOptions);

        Log.i("ilhanan", "length: " + getAllLocation);
        for (int look = 0; getAllLocation.size() > look; look++) {

            getDistanceInMeters = distanceCalculation.distanceLocation(latLng, getAllLocation.get(look));
            Log.i("ilhanan", "distance: " + getDistanceInMeters);

            if (getDistanceInMeters < 50) {

                double containLat = getAllLocation.get(look).latitude;
                double containLong = getAllLocation.get(look).longitude;

                LatLng containLocation = new LatLng(containLat, containLong);

                if (saveLocation.contains(containLocation)) {
                    if (saveLocation.size() > 4) {
                        Log.i("testCapture", "first index " + saveLocation.get(0));
                        Log.i("testCapture", "last index " + getAllLocation.get(look));
                        if (saveLocation.get(0).equals(getAllLocation.get(look))) {
                            Log.i("testCapture", "YES!! Captured");
                            capturedTerritory(saveLocation);
                        }
                    }
                } else {
                    double getLat = getAllLocation.get(look).latitude;
                    double getLong = getAllLocation.get(look).longitude;

                    LatLng convertSaveLocation = new LatLng(getLat, getLong);

                    saveLocation.add(convertSaveLocation);
                    Log.i("testCapture", "inner " + getAllLocation.get(look));
                }
            }
        }
    }

    private List<String> conCoordinates;

    private void capturedTerritory(ArrayList<LatLng> coordinates) {
        final Firebase territoryFirebase = new Firebase(Constants.FIREBASE_URL_TERRITORIES);
        conCoordinates = new ArrayList<>();

        for (int x = 0; x < coordinates.size(); x++) {
            conCoordinates.add(String.format("%.6f",coordinates.get(x).latitude) + ", " + String.format("%.6f",coordinates.get(x).longitude));
        }
        Log.v(LOG_TAG,conCoordinates.size()+"");

//        while (!conCoordinates.isEmpty()) {
            territoryFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    DataSnapshot postSnapshot = findTerritory(snapshot, conCoordinates);
                    if (checkCoordinates(postSnapshot.getValue(FTerritory.class).getCoordinates())) {
                        new Firebase(Constants.FIREBASE_URL_TERRITORIES + "/" + postSnapshot.getKey())
                                .setValue(updateTerritoryModel(postSnapshot.getValue(FTerritory.class)));
                    } else
                        Log.v(LOG_TAG, "UNFINISHED RUN!");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(LOG_TAG, firebaseError.toString());
                }
            });
//        }
    }

    private FTerritory updateTerritoryModel(FTerritory territory) {
        SharedPreferences userPref = getActivity().getSharedPreferences(Constants.USER_PREF_ID, Context.MODE_PRIVATE);
        String email = userPref.getString(Constants.PROFILE_EMAIL, null);
        String cluster = userPref.getString(Constants.PROFILE_CLUSTER, null);

        //uncaptured territory
        if (territory.getLevel() == 0) {
            Log.v(LOG_TAG,"Scout");
            territory.setUser_owner(email);
            territory.setCluster_owner(cluster);
            if (cluster.equals("velocity"))
                territory.setLevel(territory.getLevel() + 1);
            else
                territory.setLevel(territory.getLevel() - 1);
        }
        //fortify
        else if (cluster.equals(territory.getCluster_owner())) {
            Log.v(LOG_TAG,"Fortify");
            territory.setUser_owner(email);
            if (territory.getLevel() < 10)
                territory.setLevel(territory.getLevel() + 1);
        }
        //envade
        else if (!cluster.equals(territory.getCluster_owner())) {
            Log.v(LOG_TAG,"Envade");
            if(territory.getLevel()==1){
                territory.setUser_owner(email);
                territory.setCluster_owner(cluster);
            }else{
                if (territory.getLevel() <= 10)
                    territory.setLevel(territory.getLevel() - 1);
            }
        }
        return territory;
    }

    private DataSnapshot findTerritory(DataSnapshot snapshot, List<String> conCoordinates) {
        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            FTerritory territory = postSnapshot.getValue(FTerritory.class);
            for (int x = 0; x < territory.getCoordinates().size(); x++) {
                if (territory.getCoordinates().get(x).equals(conCoordinates.get(0)))
                    return postSnapshot;
            }
        }
        return null;
    }

    private boolean checkCoordinates(List<String> fCoordinates) {
        int cnt = 0;
        for (int x = 0; x < fCoordinates.size(); x++) {
            for (int y = 0; y < conCoordinates.size(); y++) {
                if (fCoordinates.get(x).equals(conCoordinates.get(y))) {
//                    conCoordinates.remove(y);
                    cnt++;
                    break;
                }
            }
        }

        Log.v(LOG_TAG,conCoordinates.toString()+", "+fCoordinates.toString());
        Log.v(LOG_TAG,cnt+", "+fCoordinates.size());
        if (cnt == fCoordinates.size())
            return true;
        else
            return false;
    }
}
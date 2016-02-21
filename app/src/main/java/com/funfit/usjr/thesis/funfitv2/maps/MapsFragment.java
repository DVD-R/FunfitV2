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

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.distance.DistanceCalculation;
import com.funfit.usjr.thesis.funfitv2.fragmentDialog.FilterViewDialog;
import com.funfit.usjr.thesis.funfitv2.fragmentDialog.markerDialogFragment;
import com.funfit.usjr.thesis.funfitv2.model.CapturingModel;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
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
        LocationListener, GoogleMap.OnMarkerClickListener, IMapFragmentView {
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        ButterKnife.bind(this, view);
        //instantiate DistanceCalculation
        distanceCalculation = new DistanceCalculation();

        mMapView = (MapView) view.findViewById(R.id.mapView);
        receivedMarkerPosition = new ArrayList<LatLng>();
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
        if (!mBroadcastInfoRegistered){
            getActivity().registerReceiver(encodedPolylineBroadcast, new IntentFilter(getString(R.string.broadcast_encodedpolyline)));
            mBroadcastInfoRegistered = true;
            myMap = mMapView.getMap();

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


        for (Territory territory: listTerritories) {
            List<LatLng> oval = PolyUtil.decode(territory.getEncoded_polyline());

            if (territory.getStatus().equals("uncharted") && territory.getFaction_description() == null) {
                myMap.addPolygon(new PolygonOptions()
                        .addAll(oval)
                        .fillColor(Color.LTGRAY - ALPHA_ADJUSTMENT)
                        .strokeColor(Color.GRAY)
                        .strokeWidth(5));
            }else if(territory.getStatus().equals("owned") && territory.getFaction_description().equals("velocity")){
                myMap.addPolygon(new PolygonOptions()
                        .addAll(oval)
                        .fillColor(Color.BLUE - ALPHA_ADJUSTMENT)
                        .strokeColor(Color.BLUE)
                        .strokeWidth(5));
            }else if(territory.getStatus().equals("owned") && territory.getFaction_description().equals("impulse")){
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
            }catch (Exception e){
                e.printStackTrace();
            }

            polylineOptions = new PolylineOptions();
            //arrayPoints.add(latLng);
            myMap.setOnMarkerClickListener(this);
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
        float getDistanceInMeters = 0;
        float tempSpeed = 0;
        float speed = 0;
        float distance = 0;
        long tempDuration = 0;
        long duration = 0;

        if (mapController) {

            newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, 16);
            myMap.animateCamera(cameraUpdate);
            myMap.setMyLocationEnabled(true);

            // settin polyline in the map
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.GREEN);
            polylineOptions.width(4);
            arrayPoints.add(newLatLng);
            polylineOptions.addAll(arrayPoints);
            myMap.addPolyline(polylineOptions);

            for (int x = 0; arrayPoints.size() - 1 > x; x++) {
                arrayPoints.get(x);
                Location loc1 = new Location("");
                loc1.setLatitude(arrayPoints.get(x).latitude);
                loc1.setLongitude(arrayPoints.get(x).longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(arrayPoints.get(x + 1).latitude);
                loc2.setLongitude(arrayPoints.get(x + 1).longitude);

                getDistanceInMeters = getDistanceInMeters + loc1.distanceTo(loc2);

                float displaySpeed = location.getSpeed();


            }
            tempSpeed = tempSpeed + location.getSpeed();
            duration = duration + location.getTime();

            Log.i("distanceTo", "Distance in meters: " + getDistanceInMeters + " Speed: " + location.getSpeed());
//        }

            Log.i("distanceTo", "Distance in meters: " + getDistanceInMeters);
            if (getDistanceInMeters > 200) {
                int controller = arrayPoints.size() - 1;
                Log.i("distanceTo", "distance: " + distanceCalculation.CalculationByDistance(arrayPoints.get(0), arrayPoints.get(controller)));

                Location loc1 = new Location("");
                loc1.setLatitude(arrayPoints.get(0).latitude);
                loc1.setLongitude(arrayPoints.get(0).longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(arrayPoints.get(controller).latitude);
                loc2.setLongitude(arrayPoints.get(controller).longitude);

                float distanceCondition = loc1.distanceTo(loc2);

                if (distanceCondition < 20) {
                    Log.i("location1", "Distance of: " + distanceCalculation.CalculationByDistance(arrayPoints.get(0), arrayPoints.get(controller)));

                    //speed
                    speed = tempSpeed / (arrayPoints.size() + 1);

                    //distance
                    for(int calculate = 0; arrayPoints.size() - 1 > calculate; calculate++){
                        Location start = new Location("");
                        loc1.setLatitude(arrayPoints.get(0).latitude);
                        loc1.setLongitude(arrayPoints.get(0).longitude);

                        Location end = new Location("");
                        loc2.setLatitude(arrayPoints.get(controller).latitude);
                        loc2.setLongitude(arrayPoints.get(controller).longitude);

                        distance = distance + start.distanceTo(end);
                    }

                    arrayPoints.add(arrayPoints.get(0));

                    // long duration, float speed, float distance
                    countPolygonPoints(duration,speed,distance);
                }
            }
        }
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

                    Utils.getCaloriesBurned((Integer.parseInt(weight)),duration,speed);
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
    public void runFab(){
        filterViewDialog = new FilterViewDialog();
        filterViewDialog.setTargetFragment(this, REQUEST_CODE2);
        filterViewDialog.show(getFragmentManager(), "Filter Sample Fragment");
    }

    @OnClick(R.id.fab_search)
    public void searchFab(){

    }
}




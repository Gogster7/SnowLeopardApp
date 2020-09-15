package org.kashmirworldfoundation.snowleopardapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.ArrayList;

public class GoogleMapActivity extends AppCompatActivity implements  OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener {

    // Need:
    //      implementation 'com.google.android.gms:play-services-maps:17.0.0'
    //      implementation 'com.google.android.gms:play-services-location:17.0.0'


    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 111;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Polyline llHistoryPolyline;
    private ArrayList<LatLng> latLonHistory = new ArrayList<>(); // A history of out LL locations
    private boolean zooming = false;
    private float oldZoom;
    private double longitudeD;
    private double latitudeD;
    private double elevationD;


    private Marker currentLocation;
    private RadioButton r;
    private RadioButton r2;
    private RadioButton r3;


    //for add Potential Location marker
    private ArrayList<String> locationList;
    private ArrayList<Marker> setLocationList;
    private Marker setLocation;
    private double tempLongitude;
    private double tempLatitude;

    private EditText LatitudeInput;
    private EditText LongitudeInput;
    private EditText ElevationInput;


    //for Set Iinput
    private double LatitudeInputD;
    private double LongitudeInputD;
    private double ElevationInputD;

    private LatLng tempLatLng;
    private Button saveInput;
    private Button mapDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        r=findViewById(R.id.radioButtonCurrentLID);
        r2=findViewById(R.id.radioButtonCurrentOrganizationLID2);
        r3=findViewById(R.id.radioButtonCurrentPotentialLID);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        locationList=new ArrayList<>();
        setLocationList=new ArrayList<>();
        LatitudeInput=findViewById(R.id.MapLatitudeInputId);
        LongitudeInput=findViewById(R.id.MapLongitudeInputId);
        ElevationInput=findViewById(R.id.MapElevationInputId);


        saveInput=findViewById(R.id.MapSetInPutId);
        saveInput.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                getInputLocation();
            }
        });

        mapDownload=findViewById(R.id.downloadMapButoonId);
        mapDownload.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                mapStyleSet();


            }
        });

    }
    private void mapStyleSet() {
        // Set up download interaction. Display a dialog
        // when the user clicks download button and require
        // a user-provided region name
        AlertDialog.Builder builder = new AlertDialog.Builder(GoogleMapActivity.this);



        builder.setTitle("Save the offline map in Mapbox. \n Please choose a Map Style")
                .setItems(R.array.MapStyle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Intent i= new Intent(getApplicationContext(), OfflineManagerActivity.class);
                        i.putExtra("FromGoogleMap","True");

                        switch(which){
                            case 0:
                                i.putExtra("Style","Streets");
                                break;
                            case 1:
                                i.putExtra("Style","OutDoors");
                                break;
                            case 2:
                                i.putExtra("Style","Satellite Streets");
                                break;
                            default:
                                Log.d(TAG, "onClick: !!!!!!!default"+which);

                        }


                        startActivity(i);
                    }
                });



        // Display the dialog
        builder.show();
    }

    /**
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
        //zooming = true;

        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);

        if (checkPermission()) {
            setupLocationListener();
            setupZoomListener();
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        setupLocationListener();
                    } else {
                        Toast.makeText(this, "Location Permission not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    //initial state is visible
    private int flagC=0;
    public void radioClickedCurrent(View view)
    {
        if(flagC==0){
            r.setChecked(true);
            currentLocation.setVisible(true);
            flagC++;
        }else{
            r.setChecked(false);
            currentLocation.setVisible(false);
            flagC=0;
        }
    }

    //initial state is invisible
    private int flagL=1;
    public void radioClickedOrganization(View view)
    {
        if(flagL==1){
            r2.setChecked(true);
            //need to change to organization
            //currentLocation.setVisible(true);
            flagL--;
        }else{
            r2.setChecked(false);
            //need to change to organization
            //currentLocation.setVisible(false);
            flagL=1;
        }
    }

    //initial state is invisible
    private int flagP=1;
    public void radioClickedPotential(View view)
    {
        if(flagP==1){
            r3.setChecked(true);
            //need to change to potential
            // currentLocation.setVisible(true);
            flagP--;
        }else{
            r3.setChecked(false);
            //need to change to potential
            //currentLocation.setVisible(false);
            flagP=1;
        }
    }




    //for info window
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onInfoWindowClick: !!!!");
    }




    private void setupLocationListener() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocListener(this);
        ;




        //minTime	    long: minimum time interval between location updates, in milliseconds
        //minDistance	float: minimum distance between location updates, in meters
        if (checkPermission() && locationManager != null) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

        }
        Log.d(TAG, "setupLocationListener: !!");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null && locationListener != null)
            locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission() && locationManager != null && locationListener != null)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, locationListener);
    }

    private void setupZoomListener() {
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (zooming) {
                    Log.d(TAG, "onCameraIdle: DONE ZOOMING: " + mMap.getCameraPosition().zoom);
                    zooming = false;
                    oldZoom = mMap.getCameraPosition().zoom;
                }
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (mMap.getCameraPosition().zoom != oldZoom) {
                    Log.d(TAG, "onCameraMove: ZOOMING: " + mMap.getCameraPosition().zoom);
                    zooming = true;
                }
            }
        });
    }

    public void getInputLocation(){
        LatitudeInputD=Double.parseDouble(LatitudeInput.getText().toString());
        LongitudeInputD=Double.parseDouble(LongitudeInput.getText().toString());
        ElevationInputD=Double.parseDouble(ElevationInput.getText().toString());
        tempLatLng = new LatLng(LatitudeInputD, LongitudeInputD);
        //add marker and get the marker
        Marker tempLocation=mMap.addMarker(new MarkerOptions().alpha(0.5f).position(tempLatLng).title("My Current Location").snippet(latitudeD+" , "+longitudeD).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        tempLocation.setDraggable(true);

//        Marker tempLocation=addMarker(mMap,latitudeD,longitudeD,"title")
        setLocationList.add(tempLocation);

        Log.d(TAG, "getInputLocation: !!!!"+tempLocation);


    }

    public void addInputLocation(){
        Marker tempLocation=mMap.addMarker(new MarkerOptions().alpha(0.5f).position(tempLatLng).title("My Current Location").snippet(latitudeD+" , "+longitudeD));


        // final boolean add = setLocationList.add(new Marker().setPosition(tempLongitude));
    }

    public void updateLocation(Location location) {
        ///////
        latitudeD=location.getLatitude();
        longitudeD=location.getLongitude();
        GetElevationGoogleMaps getElecation=new GetElevationGoogleMaps();
        elevationD=getElecation.GetElevation(longitudeD,latitudeD);
        Log.d(TAG, "updateLocation: !!"+latitudeD);

        LatitudeInput.setText(String.valueOf(latitudeD));
        LongitudeInput.setText(String.valueOf(longitudeD));
        ElevationInput.setText(String.valueOf(elevationD));



        ///////
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latLonHistory.add(latLng); // Add the LL to our location history

        if (llHistoryPolyline != null) {
            llHistoryPolyline.remove(); // Remove old polyline
        }


        if (latLonHistory.size() == 1) { // First update
            currentLocation=mMap.addMarker(new MarkerOptions().alpha(0.5f).position(latLng).title("My Current Location").snippet(latitudeD+" , "+longitudeD));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            zooming = true;
            return;
        }

        if (latLonHistory.size() > 1) { // Second (or more) update
            PolylineOptions polylineOptions = new PolylineOptions();

            for (LatLng ll : latLonHistory) {
                polylineOptions.add(ll);
            }

            llHistoryPolyline = mMap.addPolyline(polylineOptions);
            llHistoryPolyline.setEndCap(new RoundCap());
            llHistoryPolyline.setWidth(8);
            llHistoryPolyline.setColor(Color.BLUE);

        }

        if (!zooming)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f", position.latitude,
                        position.longitude));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                position.latitude,
                position.longitude));

        //update drag value to display
        LatitudeInput.setText(String.valueOf(position.latitude));
        LongitudeInput.setText(String.valueOf(position.longitude));

        GetElevationGoogleMaps getElecation=new GetElevationGoogleMaps();
        Double TempElevationD=getElecation.GetElevation(position.longitude,position.latitude);
        ElevationInput.setText(String.valueOf(TempElevationD));

    }

    private Marker addMarker(GoogleMap map, double lat, double lon,
                             int title, int snippet) {
        Marker tempLocation=map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(getString(title))
                .snippet(getString(snippet))
                .draggable(true));
        return tempLocation;
    }


}
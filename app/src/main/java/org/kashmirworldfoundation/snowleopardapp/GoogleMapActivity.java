
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;


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
    private LatLng nTempLatLng;
    private Button saveInput;
    private Button mapDownload;
    private Button markerDownload;
    private Marker selectMarker;
    private String tempMarkerName="";

    private CollectionReference collectionReference;
    private int count;
    private int size;
    private Member mem;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    private ArrayList<org.kashmirworldfoundation.snowleopardapp.Marker> downloadMarkerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        r=findViewById(R.id.radioButtonCurrentLID);
        r2=findViewById(R.id.radioButtonCurrentOrganizationLID2);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Marker");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        locationList=new ArrayList<>();
        setLocationList=new ArrayList<>();
        downloadMarkerList=new ArrayList<>();
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

        markerDownload=findViewById(R.id.downloadMarkerId);
        markerDownload.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                downloadMarker();


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
                        Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
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
            for(int i=0;i<setLocationList.size();i++){
                setLocationList.get(i).setVisible(true);
            }

            //need to change to organization
            //currentLocation.setVisible(true);
            flagL--;
        }else{
            r2.setChecked(false);
            for(int i=0;i<setLocationList.size();i++){
                setLocationList.get(i).setVisible(false);
            }
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

        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GoogleMapActivity.this);
        builder.setTitle("Do you want to delete this marker?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String markerId = marker.getTag().toString();
                        marker.remove();

                      db.collection("Marker").document(markerId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        // Do nothing here
                    }
                });
        builder.create();
        builder.show();

    }



    private void setupLocationListener() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocListener(this);

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
        String MarkerId= UUID.randomUUID().toString();

        //get the marker title
        AlertDialog.Builder builder = new AlertDialog.Builder(GoogleMapActivity.this);

        final EditText markerName = new EditText(GoogleMapActivity.this);
        markerName.setHint("Marker Name");

        // Build the dialog box
        builder.setTitle("Add new marker")
                .setView(markerName)
                .setMessage("Please enter the marker name.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempMarkerName = markerName.getText().toString();
                        //add marker and get the marker
                        Marker tempLocation=mMap.addMarker(new MarkerOptions().alpha(0.5f).position(tempLatLng).title(tempMarkerName).snippet(latitudeD+" , "+longitudeD).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                        tempLocation.setDraggable(true);
                        tempLocation.setTag(MarkerId);

                        setLocationList.add(tempLocation);

                        Log.d(TAG, "getInputLocation: !!!!"+tempLocation);

                        saveToFirebase(tempLocation.getTag().toString());

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Display the dialog
        builder.show();
    }

    public void addInputLocation(){
        Marker tempLocation=mMap.addMarker(new MarkerOptions().alpha(0.5f).position(tempLatLng).title("My Current Location").snippet(latitudeD+" , "+longitudeD));
    }

    public void updateLocation(Location location) {
        latitudeD=location.getLatitude();
        longitudeD=location.getLongitude();
        GetElevationGoogleMaps getElecation=new GetElevationGoogleMaps();
        elevationD=getElecation.GetElevation(longitudeD,latitudeD);

        LatitudeInput.setText(String.valueOf(latitudeD));
        LongitudeInput.setText(String.valueOf(longitudeD));
        ElevationInput.setText(String.valueOf(elevationD));

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


        org.kashmirworldfoundation.snowleopardapp.Marker newMarker= new org.kashmirworldfoundation.snowleopardapp.Marker();
        newMarker.setAuthor(user.getUid());
        newMarker.setLatitude(String.valueOf(marker.getPosition().latitude));
        newMarker.setLongitude(String.valueOf(marker.getPosition().longitude));
        newMarker.setElevation(String.valueOf(TempElevationD));

        newMarker.setOrg("Organization/seT4g7oPQmtwncKuY5kV");
        newMarker.setUid(marker.getTag().toString());
        newMarker.setMarkerName(marker.getTitle());

        db.collection("Marker").document(marker.getTag().toString())
                .set(newMarker, SetOptions.merge());
    }

    public void downloadMarker(){
        db.collection("Member").document(fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    mem=task.getResult().toObject(Member.class);
                    collectionReference.whereEqualTo("org",mem.getOrg()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                size = task.getResult().size();
                                for (DocumentSnapshot objectDocumentSnapshot: task.getResult()){

                                    org.kashmirworldfoundation.snowleopardapp.Marker marker = objectDocumentSnapshot.toObject(org.kashmirworldfoundation.snowleopardapp.Marker.class);

                                    downloadMarkerList.add(marker);
                                    count++;
                                    if(count==size){
//                                        update();
                                        Log.d(TAG, "Download success: !!!!"+size);
                                        afterDownloadMarker(downloadMarkerList);

                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    public void afterDownloadMarker(ArrayList<org.kashmirworldfoundation.snowleopardapp.Marker> downloadMarkerList){
        for(org.kashmirworldfoundation.snowleopardapp.Marker m:downloadMarkerList){
           Double markerLat= Double.parseDouble(m.getLatitude());
           Double markerLon= Double.parseDouble(m.getLongitude());

            nTempLatLng = new LatLng(markerLat, markerLon);
            Marker tempLocation=mMap.addMarker(new MarkerOptions().alpha(0.5f).position(nTempLatLng).title(m.getMarkerName()).snippet(markerLat+" , "+markerLon).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));



            tempLocation.setDraggable(true);
            tempLocation.setTag(m.getUid());

            setLocationList.add(tempLocation);
        }
    }



    private Marker addMarker(GoogleMap map, double lat, double lon,
                             int title, int snippet) {
        Marker tempLocation=map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(getString(title))
                .snippet(getString(snippet))
                .draggable(true));


        return tempLocation;
    }

    private void saveToFirebase(String markerId){

        final Member mem =new Member();

        db.collection("Member").whereEqualTo("email",user.getEmail());

        Log.d(TAG, "saveToFirebase: initial"+user.getUid());


        org.kashmirworldfoundation.snowleopardapp.Marker newMarker= new org.kashmirworldfoundation.snowleopardapp.Marker();
        newMarker.setAuthor(user.getUid());
        newMarker.setLatitude(LatitudeInput.getText().toString());
        newMarker.setLongitude(LongitudeInput.getText().toString());
        newMarker.setElevation(ElevationInput.getText().toString());
        newMarker.setOrg("Organization/seT4g7oPQmtwncKuY5kV");
        newMarker.setUid(markerId);
        newMarker.setMarkerName(tempMarkerName);

        db.collection("Marker").document(markerId)
                .set(newMarker, SetOptions.merge());
    }


}

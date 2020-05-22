package org.kashmirworldfoundation.snowleopardapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class CreateStation extends AppCompatActivity {
    private static final String TAG = "Activity";
    private EditText stationIdInput;
    private EditText WatershedInput;
    private EditText LatitudeInput;
    private EditText LongitudeInput;
    private EditText ElevationInput;
    private EditText cameraIDInput;
    private double latitude;
    private double longitude;
    private String stationId;
    private String watershedid;
    private String latitudeS;
    private String longitudeS;
    private String altitude;
    private String cameraId;

    private String terrain;
    private String habitat;
    private String lureType;
    private String substrate;
    private String potential;

    private static final int LOCATION_REQUEST = 111;
    //private boolean isEmpty;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_station);

        stationIdInput=findViewById(R.id.stationId);
        WatershedInput=findViewById(R.id.watershedid);
        LatitudeInput=findViewById(R.id.nId);
        LongitudeInput=findViewById(R.id.eId);
        ElevationInput=findViewById(R.id.elevationId);
        cameraIDInput=findViewById(R.id.cameraId);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();
        determineLocation();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_station_menu, menu);
        return true;
    }

    public void getInput(){
        Log.d(TAG, "In getInput ");
        stationId=stationIdInput.getText().toString();
        watershedid=WatershedInput.getText().toString();
        latitudeS=LatitudeInput.getText().toString();
        longitudeS=LatitudeInput.getText().toString();
        altitude=ElevationInput.getText().toString();
        cameraId=cameraIDInput.getText().toString();
    }

    //lure type
    public void radioClicked_SkunkFishOil(View v) {
        lureType = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "LureType You Selected " + lureType, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_CastorFishOil(View v) {
        lureType = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "LureType You Selected " + lureType, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_FishOil(View v) {
        lureType = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "LureType You Selected " + lureType, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_None(View v) {
        lureType = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "LureType You Selected " + lureType, Toast.LENGTH_SHORT).show();
    }

    //habitat type
    public void radioClicked_Scrub(View v) {
        habitat = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Habitat You Selected " + habitat, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Forest(View v) {
        habitat = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Habitat You Selected " + habitat, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Pasture(View v) {
        habitat = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Habitat You Selected " + habitat, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Barren(View v) {
        habitat = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Habitat You Selected " + habitat, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Agric(View v) {
        habitat = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Habitat You Selected " + habitat, Toast.LENGTH_SHORT).show();
    }

    //terrain type
    public void radioClicked_Ridge(View v) {
        terrain = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + terrain, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Cliff(View v) {
        terrain = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + terrain, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Draw(View v) {
        terrain = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + terrain, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Vally(View v) {
        terrain = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + terrain, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Saddle(View v) {
        terrain = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + terrain, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_plateau(View v) {
        terrain = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + terrain, Toast.LENGTH_SHORT).show();
    }

    //substrate type
    public void radioClicked_sand(View v) {
        substrate = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + substrate, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_soil(View v) {
        substrate = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + substrate, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_rock(View v) {
        substrate = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + substrate, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_snow(View v) {
        substrate = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + substrate, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_vegetation(View v) {
        substrate = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + substrate, Toast.LENGTH_SHORT).show();
    }

    //potential
    public void radioClicked_Good(View v) {
        potential = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + potential, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Medium(View v) {
        potential = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + potential, Toast.LENGTH_SHORT).show();
    }

    public void radioClicked_Poor(View v) {
        potential = ((RadioButton) v).getText().toString();
        Toast.makeText(this, "Terrain You Selected " + potential, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        Log.d(TAG, "onOptionsItemSelected: "+item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(this,"back",Toast.LENGTH_SHORT).show();
                finish();
                return super.onOptionsItemSelected(item);

            case R.id.saveId:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.kwflogo);

                final Date currentTime = Calendar.getInstance().getTime();


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(EditProfileActivity.this, "OK", Toast.LENGTH_SHORT).show();
                        createToast(CreateStation.this,"Create Station Successful :"+currentTime.toString(), Toast.LENGTH_LONG);
                        getInput();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(CreateStation.this, "CANCEL", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setTitle("Save the Station?");
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            default:
                Toast.makeText(this, "Pick the unknown item", Toast.LENGTH_LONG).show();
        }

        return true;



    }

    private void determineLocation() {
        Log.d(TAG, "determineLocation: !!!!"+checkPermission());
        if (checkPermission()) {
            Log.d(TAG, "determineLocation: 22222");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                                LatitudeInput.setText(String.valueOf(latitude));
                                Log.d(TAG, "determineLocation: 333"+latitude);
                                LongitudeInput.setText(String.valueOf(longitude));
                                Log.d(TAG, "determineLocation: 44444"+longitude);
                            }
                        }
                    });
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

    public static void createToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, "" + message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(Color.parseColor("#008577"));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(140, 75, 140, 75);
        tv.setTextColor(0xFFFFFFFF);
        toast.show();
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
                        determineLocation();
                    } else {
                        Toast.makeText(this, "Location Permission not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }




}

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
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class CreateStationActivity extends AppCompatActivity {
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
    private double altitude;
    private String cameraId;

    private String terrain;
    private String habitat;
    private String lureType;
    private String substrate;
    private String potential;

    private static final String ELEVATION_API_KEY="AIzaSyBh-rFSAH9QqPtUrLXcT5Z0c2ZQiJUWkTc";
    private static final int LOCATION_REQUEST = 111;
    //private boolean isEmpty;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_station);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
                        createToast(CreateStationActivity.this,"Create Station Successful :"+currentTime.toString(), Toast.LENGTH_LONG);
                        getInput();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(CreateStationActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
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


    private double getElevationFromGoogleMaps(double longitude, double latitude) {
        double result = Double.NaN;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String url = "https://maps.googleapis.com/maps/api/elevation/"
                + "xml?locations=" + String.valueOf(latitude)
                + "," + String.valueOf(longitude)
                + "&key="
                + ELEVATION_API_KEY;
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int r = -1;
                StringBuffer respStr = new StringBuffer();
                while ((r = instream.read()) != -1)
                    respStr.append((char) r);
                String tagOpen = "<elevation>";
                String tagClose = "</elevation>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    //result = (double)(Double.parseDouble(value)*3.2808399); // convert from meters to feet
                    result=(double)Double.parseDouble(value);
                }
                instream.close();
            }
        } catch (ClientProtocolException e) {}
        catch (IOException e) {}

        return result;
    }


    private void determineLocation() {
        if (checkPermission()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Add a marker at current location
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                                LatitudeInput.setText(String.valueOf(latitude));
                                LongitudeInput.setText(String.valueOf(longitude));
                                altitude=location.getAltitude();

                               /*data from excel for testing
                               double tempAltitude= getElevationFromGoogleMaps(74.74472,36.17607);
                               */
                                double tempAltitude= getElevationFromGoogleMaps(longitude,latitude);
                                ElevationInput.setText(String.valueOf(tempAltitude));
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

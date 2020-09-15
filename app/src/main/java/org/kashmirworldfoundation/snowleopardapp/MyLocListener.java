package org.kashmirworldfoundation.snowleopardapp;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocListener implements LocationListener {

    private GoogleMapActivity googleMapActivity;
    private static final String TAG = "MyLocListener";

    MyLocListener(GoogleMapActivity googleMapActivity) {
        this.googleMapActivity = googleMapActivity;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location);
        googleMapActivity.updateLocation(location);
    }
}
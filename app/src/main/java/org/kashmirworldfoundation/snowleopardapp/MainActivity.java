package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.kashmirworldfoundation.snowleopardapp.Fragment.AddFragment;
import org.kashmirworldfoundation.snowleopardapp.Fragment.ProfileFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ListFragment listFragment;
    private AddFragment addFragment;
    private ProfileFragment profileFragment;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static final String TAG = "MainActivity";
    public Double latitudeD;
    private Double longitudeD;
    private Double altitudeD;

    private String latitude;
    private String longitude;
    private String altitude;


    private static final String ELEVATION_API_KEY="AIzaSyBh-rFSAH9QqPtUrLXcT5Z0c2ZQiJUWkTc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        listFragment = new ListFragment();
        addFragment = new AddFragment();
        profileFragment = new ProfileFragment();

        tabLayout.setupWithViewPager(viewPager);


        // Setting up Tabs
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(listFragment, "List");
        viewPagerAdapter.addFragment(addFragment, "Add");
        viewPagerAdapter.addFragment(profileFragment, "Profile");
        viewPager.setAdapter(viewPagerAdapter);
        // Icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_add);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_profile);


        tabLayout.getTabAt(0);
        // Badges
        //BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        //badgeDrawable.setVisible(true);
        //badgeDrawable.setNumber(10);
        //exceptions will be thrown if provider is not permitted.


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
    }





    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }


    private void determineLocation() {
        if (checkPermission()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitudeD=location.getLatitude();
                                longitudeD=location.getLongitude();
                                latitude=String.valueOf(latitudeD);
                                longitude=String.valueOf(longitudeD);
                                altitudeD= getElevationFromGoogleMaps(longitudeD,latitudeD);
                                altitude=String.valueOf(altitudeD);
                                setArgument();
                            }
                        }
                    });
        }

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


    //send the argument to add fragment
    private void setArgument(){
        Bundle bundle = new Bundle();
        bundle.putString("latitude", latitude);
        bundle.putString("longitude", longitude);
        bundle.putString("altitude", altitude);
        addFragment.setArguments(bundle);
        addFragment.refresh();
    }

    //ask user to get the location premission and check
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
                        determineLocation();
                    } else {
                        Toast.makeText(this, "Location Permission not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}

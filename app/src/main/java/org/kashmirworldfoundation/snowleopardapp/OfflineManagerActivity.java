package org.kashmirworldfoundation.snowleopardapp;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.kashmirworldfoundation.snowleopardapp.R;

import com.google.gson.JsonElement;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.tilequery.MapboxTilequery;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import android.content.ContextWrapper;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * Download, view, navigate to, and delete an offline region.
 */
public class OfflineManagerActivity extends AppCompatActivity implements
        OnMapReadyCallback, OnLocationClickListener, PermissionsListener, OnCameraTrackingChangedListener,MapboxMap.OnMapClickListener {

    private static final String TAG = "OffManActivity";

    // JSON encoding/decoding
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";


    private LocationEngine locationEngine;
    private OfflineManagerActivity.LocationChangeListeningActivityLocationCallback callback =
            new OfflineManagerActivity.LocationChangeListeningActivityLocationCallback(this);

    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 10000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 3;

    // UI elements
    private MapView mapView;
    private MapboxMap map;
    private ProgressBar progressBar;
    private Button downloadButton;
    private Button listButton;

    private boolean isEndNotified;
    private int regionSelected;

    // Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;


    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private boolean isInTrackingMode;


    private int styleChooseNumber=0;
    private static Style styleSet;
    private static Double currentLatitude;
    private static Double currentLongitude;
    private static String stringAltitude;
    private static Double currentAltitude;

    private boolean fromGoogleMap=false;

    private static final String RESULT_GEOJSON_SOURCE_ID = "RESULT_GEOJSON_SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, "sk.eyJ1IjoiZ2FtbW1raW1vIiwiYSI6ImNrY3oyajlneTBmcXYzMXBnY3liaXNqeHgifQ._vcEPg9q9DI4miZ83e9U8g");

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_offline_manager);

        // Set up the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);




        Intent intent=getIntent();

        final String inputStyle;

        String mapStyle=intent.getStringExtra("Style");
        Log.d(TAG, "onCreate: !!!"+mapStyle);

        if(mapStyle.equals("Streets")){
            inputStyle=Style.MAPBOX_STREETS;
            styleChooseNumber=1;
        }else if(mapStyle.equals("OutDoors")){
            inputStyle=Style.OUTDOORS;
            styleChooseNumber=2;
        }else{
            inputStyle=Style.SATELLITE_STREETS;
            styleChooseNumber=3;
        }


    //change the style by floating button after the map create
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(styleChooseNumber==1){
                    mapboxMap.setStyle(Style.OUTDOORS);
                    styleChooseNumber=2;
                }else if(styleChooseNumber==2){
                    mapboxMap.setStyle(Style.SATELLITE_STREETS);
                    styleChooseNumber=3;
                }else if(styleChooseNumber==3){
                    mapboxMap.setStyle(Style.MAPBOX_STREETS);
                    styleChooseNumber=1;
                }

            }
        });


        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                OfflineManagerActivity.this.mapboxMap = mapboxMap;
                mapboxMap.addOnMapClickListener(OfflineManagerActivity.this);

                map = mapboxMap;
                mapboxMap.setStyle(inputStyle, new Style.OnStyleLoaded(){
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Assign progressBar for later use
                        progressBar = findViewById(R.id.progress_bar);

                        // Set up the offlineManager
                        offlineManager = OfflineManager.getInstance(OfflineManagerActivity.this);

                        // Bottom navigation bar button clicks are handled here.
                        // Download offline button
                        downloadButton = findViewById(R.id.download_button);
                        downloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadRegionDialog();
                            }
                        });

                        // List offline regions
                        listButton =  findViewById(R.id.list_button);
                        listButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadedRegionList();
                            }
                        });

                        styleSet=style;
                        enableLocationComponent(styleSet);


                        if(intent.hasExtra("FromGoogleMap")){
                            Log.d(TAG, "onStyleLoaded: !!!! from google map");
                            fromGoogleMap=true;
                            downloadRegionDialog();
                        }





                    }
                });
            }
        });
    }



    /**
     * Use the Java SDK's MapboxTilequery class to build a API request and use the API response
     *
     * @param point where the Tilequery API should query Mapbox's "mapbox.mapbox-terrain-v2" tileset
     *              for elevation data.
     */
    //////************
    private static void makeElevationRequestToTilequeryApi(@NonNull final Style style, @NonNull LatLng point) {
        MapboxTilequery elevationQuery = MapboxTilequery.builder()
                .accessToken("sk.eyJ1IjoiZ2FtbW1raW1vIiwiYSI6ImNrY3oyajlneTBmcXYzMXBnY3liaXNqeHgifQ._vcEPg9q9DI4miZ83e9U8g")
                .tilesetIds("mapbox.mapbox-terrain-v2")
                .query(Point.fromLngLat(point.getLongitude(), point.getLatitude()))
                .geometry("polygon")
                .layers("contour")
                .build();

        elevationQuery.enqueueCall(new Callback<FeatureCollection>() {
            @Override
            public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {

                if (response.body().features() != null) {
                    List<Feature> featureList = response.body().features();

                    String listOfElevationNumbers = "";



                    // Build a list of the elevation numbers in the response.
                    for (Feature singleFeature : featureList) {
                        listOfElevationNumbers = listOfElevationNumbers + singleFeature.getStringProperty("ele") + ", ";

                        stringAltitude=singleFeature.getStringProperty("ele");
                    }


                } else {
                    String noFeaturesString = "no features";
                    Timber.d(noFeaturesString);
                    //Toast.makeText(OfflineManagerActivity.this, noFeaturesString, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeatureCollection> call, Throwable throwable) {
                Timber.d("Request failed: %s", throwable.getMessage());
                // Toast.makeText(OfflineManagerActivity.this,R.string.elevation_tilequery_api_response_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);

    }

    private void AskForBackToGoogleMAP(){
        AlertDialog.Builder builder = new AlertDialog.Builder(OfflineManagerActivity.this);
        builder.setTitle("Save the offlineMap success\nBack to Google map page?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onBackPressed();
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

    /**
     * This method handles click events for SymbolLayer symbols.
     *
     * @param screenPoint the point on screen clicked
     */
    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (!features.isEmpty()) {
            Feature feature = features.get(0);

            StringBuilder stringBuilder = new StringBuilder();

            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    stringBuilder.append(String.format("%s - %s", entry.getKey(), entry.getValue()));
                    stringBuilder.append(System.getProperty("line.separator"));
                }

            }
        } else {
            Toast.makeText(this, "query_feature_no_properties_found", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        Log.d(TAG, "onMapClick: !"+point.getLatitude());
        Log.d(TAG, "onMapClick: !"+point.getLongitude());
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }


    private static class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<OfflineManagerActivity> activityWeakReference;

        LocationChangeListeningActivityLocationCallback(OfflineManagerActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }


        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            OfflineManagerActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();
                location.getLongitude();
                location.getLatitude();

                if (location == null) {
                    return;
                }

                Log.d(TAG, "onSuccess longtitude: "+location.getLongitude());
                Log.d(TAG, "onSuccess Latitude: "+location.getLatitude());

                currentLatitude=location.getLatitude();
                currentLongitude=location.getLongitude();


                LatLng point=new LatLng();
                point.setLatitude(currentLatitude);
                point.setLongitude(currentLongitude);

                //use this function to get altitude
                makeElevationRequestToTilequeryApi(styleSet, point);

                // Create a Toast which displays the new location's coordinates
                Toast toast=Toast.makeText(activity, "New Location ; currentLatitude :" +currentLatitude+"currentLongitude : "+currentLongitude +"Altitude : "+stringAltitude,

                        Toast.LENGTH_SHORT);


                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 50);

                toast.show();

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            OfflineManagerActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Override Activity lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void downloadRegionDialog() {
        // Set up download interaction. Display a dialog
        // when the user clicks download button and require
        // a user-provided region name
        AlertDialog.Builder builder = new AlertDialog.Builder(OfflineManagerActivity.this);

        final EditText regionNameEdit = new EditText(OfflineManagerActivity.this);
        regionNameEdit.setHint("Map Title");

        // Build the dialog box
        builder.setTitle("Save the map")
                .setView(regionNameEdit)
                .setMessage("Please enter the map title.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regionName = regionNameEdit.getText().toString();
                        // Require a region name to begin the download.
                        // If the user-provided string is empty, display
                        // a toast message and do not begin download.
                        if (regionName.length() == 0) {
                            Toast.makeText(OfflineManagerActivity.this, "0---", Toast.LENGTH_SHORT).show();
                        } else {
                            // Begin download process
                            try{
                                downloadRegion(regionName);
                            }catch (Exception e){
                                System.err.println(e);
                            }

                        }
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

    private void downloadRegion(final String regionName) {
        // Define offline region parameters, including bounds,
        // min/max zoom, and metadata

        // Start the progressBar
        startProgress();

        // Create offline definition using the current
        // style and boundaries of visible map area

        map.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                String styleUrl = style.getUri();
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

                //calsulate and resize the bounds
                double latNorth=bounds.getLatNorth();
                double latSouth=bounds.getLatSouth();

                double lonEast=bounds.getLonEast();
                double lonWest=bounds.getLonWest();
                Log.d(TAG, "onStyleLoaded: getLatNorth"+latNorth);
                Log.d(TAG, "onStyleLoaded: getLatSouth"+latSouth);
                Log.d(TAG, "onStyleLoaded: getLonEast"+lonEast);
                Log.d(TAG, "onStyleLoaded: getLonWest"+lonWest);

                double halfLatitude=(latNorth+latSouth)/2;
                double halfLongitude=(lonEast+lonWest)/2;
                double newLatNorth=(halfLatitude+latNorth)/2;
                double newLatSouth=(halfLatitude+latSouth)/2;
                double newLonEast=(halfLongitude+lonEast)/2;
                double newLonWest=(halfLongitude+lonWest)/2;


                ///if the style is normal
                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .include(new LatLng(latNorth, lonEast)) // Northeast
                        .include(new LatLng(latSouth,lonWest)) // Southwest
                        .build();

                //resize the bound if the style is satellite
                if(styleChooseNumber==3){
                    latLngBounds = new LatLngBounds.Builder()
                            .include(new LatLng(newLatNorth, newLonEast)) // Northeast
                            .include(new LatLng(newLatSouth,newLonWest)) // Southwest
                            .build();
                }


                double minZoom = map.getCameraPosition().zoom;
                double maxZoom = map.getMaxZoomLevel();
                float pixelRatio = OfflineManagerActivity.this.getResources().getDisplayMetrics().density;


                //for normal street map and outdoormap
                OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                        styleUrl, latLngBounds, minZoom, maxZoom, pixelRatio);

                //for satellite, need to resize later, if the tile number is greater than 6000
                if(styleChooseNumber==3){
                    definition = new OfflineTilePyramidRegionDefinition(styleUrl, latLngBounds, 7, 25, pixelRatio);
                    Log.d(TAG, "onStyleLoaded: style = 2");
                }

                // Build a JSONObject using the user-defined offline region title,
                // convert it into string, and use it to create a metadata variable.
                // The metadata variable will later be passed to createOfflineRegion()
                byte[] metadata;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(JSON_FIELD_REGION_NAME, regionName);
                    String json = jsonObject.toString();
                    metadata = json.getBytes(JSON_CHARSET);

                } catch (Exception exception) {
                    Timber.e("Failed to encode metadata: %s", exception.getMessage());
                    metadata = null;
                }

                // Create the offline region and launch the download
                offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        Timber.d( "Offline region created: %s" , regionName);
                        OfflineManagerActivity.this.offlineRegion = offlineRegion;
                        launchDownload();
                    }

                    @Override
                    public void onError(String error) {
                        Timber.e( "Error: %s" , error);
                    }
                });
            }
        });

        if(fromGoogleMap==true){
            AskForBackToGoogleMAP();
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
//                    .foregroundDrawable(R.drawable.kwflogo)
                    .build();

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(this);

            // Add the camera tracking listener. Fires if the map camera is manually moved.
            locationComponent.addOnCameraTrackingChangedListener(this);

            findViewById(R.id.back_to_camera_tracking_mode2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isInTrackingMode) {
                        isInTrackingMode = true;
                        locationComponent.setCameraMode(CameraMode.TRACKING);
                        locationComponent.zoomWhileTracking(16f);
                        Toast.makeText(OfflineManagerActivity.this, "A",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OfflineManagerActivity.this, "B",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });


            initLocationEngine();



        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public void onLocationComponentClick() {
        if (locationComponent.getLastKnownLocation() != null) {
            Toast.makeText(this, String.format("current location",
                    locationComponent.getLastKnownLocation().getLatitude(),
                    locationComponent.getLastKnownLocation().getLongitude()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCameraTrackingDismissed() {
        isInTrackingMode = false;
    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {
        // Empty on purpose
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "2", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private void launchDownload() {
        // Set up an observer to handle download progress and
        // notify the user when the region is finished downloading
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {
                // Compute a percentage

                double percentage = status.getRequiredResourceCount() >= 0
                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                        0.0;
                if (status.isComplete()) {
                    // Download complete
                    endProgress("end");
                    return;
                } else if (status.isRequiredResourceCountPrecise()) {
                    // Switch to determinate state
                    setPercentage((int) Math.round(percentage));
                }

                // Log what is being currently downloaded
                Timber.d("%s/%s resources; %s bytes downloaded.",
                        String.valueOf(status.getCompletedResourceCount()),
                        String.valueOf(status.getRequiredResourceCount()),
                        String.valueOf(status.getCompletedResourceSize()));
            }

            @Override
            public void onError(OfflineRegionError error) {
                Timber.e("onError reason: %s", error.getReason());
                Timber.e("onError message: %s", error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {
                Timber.e("Mapbox tile count limit exceeded: %s", limit);
            }
        });

        // Change the region state
        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }

    private void downloadedRegionList() {
        // Build a region list when the user clicks the list button

        // Reset the region selected int to 0
        regionSelected = 0;

        // Query the DB asynchronously
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(final OfflineRegion[] offlineRegions) {
                // Check result. If no regions have been
                // downloaded yet, notify user and return
                if (offlineRegions == null || offlineRegions.length == 0) {
                    Toast.makeText(getApplicationContext(), "no regions", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add all of the region names to a list
                ArrayList<String> offlineRegionsNames = new ArrayList<>();
                for (OfflineRegion offlineRegion : offlineRegions) {
                    offlineRegionsNames.add(getRegionName(offlineRegion));
                }
                final CharSequence[] items = offlineRegionsNames.toArray(new CharSequence[offlineRegionsNames.size()]);

                // Build a dialog containing the list of regions
                AlertDialog dialog = new AlertDialog.Builder(OfflineManagerActivity.this)
                        .setTitle("Map offline data")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Track which region the user selects
                                regionSelected = which;
                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Toast.makeText(OfflineManagerActivity.this, items[regionSelected], Toast.LENGTH_LONG).show();

                                // Get the region bounds and zoom
                                LatLngBounds bounds = (offlineRegions[regionSelected].getDefinition()).getBounds();
                                double regionZoom = (offlineRegions[regionSelected].getDefinition()).getMinZoom();

                                // Create new camera position
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(bounds.getCenter())
                                        .zoom(regionZoom)
                                        .build();

                                // Move camera to new position
                                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }
                        })
                        .setNeutralButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Make progressBar indeterminate and
                                // set it to visible to signal that
                                // the deletion process has begun
                                progressBar.setIndeterminate(true);
                                progressBar.setVisibility(View.VISIBLE);

                                // Begin the deletion process
                                offlineRegions[regionSelected].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                                    @Override
                                    public void onDelete() {
                                        // Once the region is deleted, remove the
                                        // progressBar and display a toast
                                        progressBar.setVisibility(View.INVISIBLE);
                                        progressBar.setIndeterminate(false);
                                        Toast.makeText(getApplicationContext(), "delete",
                                                Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        progressBar.setIndeterminate(false);
                                        Timber.e( "Error: %s", error);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // When the user cancels, don't do anything.
                                // The dialog will automatically close
                            }
                        }).create();
                dialog.show();

            }

            @Override
            public void onError(String error) {
                Timber.e( "Error: %s", error);
            }
        });
    }

    private String getRegionName(OfflineRegion offlineRegion) {
        // Get the region name from the offline region metadata
        String regionName;

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            Timber.e("Failed to decode metadata: %s", exception.getMessage());
            regionName = String.format("Region Name", offlineRegion.getID());
        }
        return regionName;
    }

    // Progress bar methods
    private void startProgress() {
        // Disable buttons
        downloadButton.setEnabled(false);
        listButton.setEnabled(false);

        // Start and show the progress bar
        isEndNotified = false;
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setPercentage(final int percentage) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }

    private void endProgress(final String message) {
        // Don't notify more than once
        if (isEndNotified) {
            return;
        }

        // Enable buttons
        downloadButton.setEnabled(true);
        listButton.setEnabled(true);

        // Stop and hide the progress bar
        isEndNotified = true;
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);

        // Show a toast
        Toast.makeText(OfflineManagerActivity.this, message, Toast.LENGTH_LONG).show();
    }


}
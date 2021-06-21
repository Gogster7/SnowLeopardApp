package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class StationListActivity extends AppCompatActivity implements View.OnClickListener {
    // objects
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private View Station_List;

    private static final String TAG = "ListFragment";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private RecyclerView recyclerView;
    private Station_ListAdapter listFragmentAdapter;
    private ArrayList<CameraStation> CStationArrayList;
    private ArrayList<String> CPathList =new ArrayList<>();
    private int WRITE_FILE=1;
    private com.google.android.material.floatingactionbutton.FloatingActionButton tocsv;
    private int pos;


    private ArrayList<CameraStation> CStations= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_station__list);
        super.onCreate(savedInstanceState);
        recyclerView= findViewById(R.id.recyler2);
        CStationArrayList= new ArrayList<>();

        tocsv = findViewById(R.id.CSVb);
        tocsv.bringToFront();

        tocsv.setOnClickListener(v -> export());


        // Add data from Firebase on the the Arrays
        new StationAsyncTaskA(this,loadstudy()).execute();

    }


    public void onClick(View v) {

        pos =recyclerView.getChildLayoutPosition(v);
        CameraStation selectiion=CStationArrayList.get(pos);
        selectiion.getaName();
        Log.e("value", selectiion.getaName());
        Intent i= new Intent(getApplication().getApplicationContext(), ExpandActivity.class);

        i.putExtra("stationz",selectiion);
        i.putExtra("pathRecord",CPathList.get(pos));
        startActivity(i);

    }



    //SationAsyncTask would update this
    public void updateStationList(ArrayList<CameraStation> s,ArrayList<String> s2){
        CStationArrayList.clear();
        CStationArrayList.addAll(s);
        CPathList.clear();
        CPathList.addAll(s2);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        listFragmentAdapter=new Station_ListAdapter(CStationArrayList,this);
        recyclerView.setAdapter(listFragmentAdapter);
        LinearLayoutManager a=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(a);


    }


    @Override
    public void onResume(){
        super.onResume();
    }

    public void export() {
        File file= new File(Environment.DIRECTORY_DOWNLOADS);
        Uri uri= Uri.fromFile(file);
        createFile(uri);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public static StationListActivity newInstance() {
        return new StationListActivity();
    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
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
    private void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "kwf.csv");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, WRITE_FILE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK && requestCode==WRITE_FILE){
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i("SD CARD", "Uri: " + uri.toString());
                editDocument(uri);
            }
        }
    }
    private void editDocument(Uri uri) {
        StringBuilder csv = new StringBuilder();

        csv.append("Station Id ,Camera Id ,Author ,Org ,Posted ,watershed Id ,Lattitude ,LongitudeS ,Altitude , Habitat, Terrain, LureType ,Substrate ,Potential ,Notes \n");
        Iterator<CameraStation> i =CStationArrayList.iterator();
        while (i.hasNext()){
            CameraStation current = i.next();
            csv.append(current.getStationId()+ ",");
            csv.append(current.getCameraId()+" ,");
            csv.append(current.getaName()+" ,");
            csv.append(current.getPosted().toDate().toString()+" ,");
            csv.append(current.getWatershedid() +" ,");
            csv.append(current.getLatitudeS() + " ,");
            csv.append(current.getLongitudeS()+" ,");
            csv.append(current.getAltitude() + " ,");
            csv.append(current.getHabitat() + " ,");
            csv.append(current.getTerrain() + " ,");
            csv.append(current.getLureType() + " ,");
            csv.append(current.getSubstrate() + " ,");
            csv.append(current.getPotential() + " ,");
            csv.append(current.getNotes() + " ,\n");


        }
        try {
            ParcelFileDescriptor pfd = getApplicationContext().getContentResolver().openFileDescriptor(uri,"w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(csv.toString().getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String loadstudy(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("Study",null);
    }
}

/**
 * put in floating action button for downloading
 */


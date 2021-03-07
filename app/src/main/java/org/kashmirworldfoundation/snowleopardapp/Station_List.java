package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.kashmirworldfoundation.snowleopardapp.Fragment.ListFragmentAdapter;
import org.kashmirworldfoundation.snowleopardapp.Fragment.StationAsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class Station_List extends AppCompatActivity implements View.OnClickListener {
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
    private int WRITE_FILE=1;
    private com.google.android.material.floatingactionbutton.FloatingActionButton tocsv;
    private int pos;
    boolean Available= false;
    boolean Readable= false;
    private FileOutputStream fstream;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseAuth FireAuth;

    private ArrayList<CameraStation> CStations= new ArrayList<>();
    private Member mem;
    private String Org;

    private int count;
    private int size;


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
        Intent i= new Intent(getApplication().getApplicationContext(), Expand.class);

        i.putExtra("stationz",selectiion);
        startActivity(i);

    }



    //SationAsyncTask would update this
    public void updateStationList(ArrayList<CameraStation> s){
        CStationArrayList.addAll(s);
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

    public static Station_List newInstance() {
        return new Station_List();
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


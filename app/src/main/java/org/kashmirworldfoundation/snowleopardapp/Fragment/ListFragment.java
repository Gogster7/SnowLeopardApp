package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileOutputStream;

import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.widget.TextView;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.kashmirworldfoundation.snowleopardapp.CameraStation;

import org.kashmirworldfoundation.snowleopardapp.Login;
import org.kashmirworldfoundation.snowleopardapp.R;
import org.kashmirworldfoundation.snowleopardapp.Station_List;
import org.kashmirworldfoundation.snowleopardapp.Study;


import java.util.ArrayList;


import android.widget.Toast;

import com.google.gson.Gson;

/**
 * put in floating action button for downloading
 */

public class ListFragment extends Fragment implements View.OnClickListener  {

    // objects
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private View ListFragment;

    private static final String TAG = "ListFragment";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private RecyclerView recyclerView;
    private ListFragmentAdapter listFragmentAdapter;
    private ArrayList<Study> CStationArrayList;
    private ArrayList<CameraStation> CStationArrayList2;
    private int WRITE_FILE=1;

    private int pos;
    boolean Available= false;
    boolean Readable= false;
    private FileOutputStream fstream;

    private int option=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {;

        ListFragment=inflater.inflate(R.layout.activity_listfragment_recycler, container, false);
        recyclerView=ListFragment.findViewById(R.id.recyler);
        CStationArrayList= new ArrayList<>();






        // Add data from Firebase on the the Arrays
        new StationAsyncTask(this).execute();

        return ListFragment;
    }

    @Override

    public void onClick(View v) {


        pos =recyclerView.getChildLayoutPosition(v);
        Study selectiion=CStationArrayList.get(pos);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Study",selectiion.getTittle());

        editor.apply();
        startActivity(new Intent(getContext(), Station_List.class));



    }



    //SationAsyncTask would update this
    public void updateStationList(ArrayList<Study> s){
        CStationArrayList.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        listFragmentAdapter=new ListFragmentAdapter(CStationArrayList,this);
        recyclerView.setAdapter(listFragmentAdapter);
        LinearLayoutManager a=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(a);


    }


    @Override
    public void onResume(){
        super.onResume();
    }


    private boolean checkPermission() {

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
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

    public static ListFragment newInstance() {
        return new ListFragment();
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




}

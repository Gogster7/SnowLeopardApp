package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.kashmirworldfoundation.snowleopardapp.Add_Prey;
import org.kashmirworldfoundation.snowleopardapp.Expand;
import org.kashmirworldfoundation.snowleopardapp.MyDateTypeAdapter;
import org.kashmirworldfoundation.snowleopardapp.Prey;
import org.kashmirworldfoundation.snowleopardapp.PreyExpand;
import org.kashmirworldfoundation.snowleopardapp.R;
import org.kashmirworldfoundation.snowleopardapp.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PreyFragment extends Fragment implements View.OnClickListener{

    // objects
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private View PreyFragment;

    private static final String TAG = "ListFragment";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private RecyclerView recyclerView;
    private PreyFragmentAdapter preyFragmentAdapter;
    private ArrayList<Prey> CPreyArrayList;
    private FloatingActionButton AddPrey,UploadPrey;
    private int WRITE_FILE=1;

    private int pos;
    boolean Available= false;
    boolean Readable= false;
    private FileOutputStream fstream;
    private FirebaseFirestore db;
    private int option=0;
    private FirebaseStorage fStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {;

        PreyFragment =inflater.inflate(R.layout.fragment_prey, container, false);
        recyclerView=PreyFragment.findViewById(R.id.recyler3);
        CPreyArrayList= new ArrayList<>();
        AddPrey=PreyFragment.findViewById(R.id.PreyAdd);

        UploadPrey=PreyFragment.findViewById(R.id.PreyUpload);
        db= FirebaseFirestore.getInstance();
        fStorage=FirebaseStorage.getInstance();
        AddPrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), Add_Prey.class));
            }
        });

        UploadPrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils utils= new Utils();
                int counter=0;
                CollectionReference collection = db.collection("CameraStation");
                ArrayList<Prey> preys=utils.getprey(getContext());
                for(Prey prey:preys){
                    DocumentReference doc = collection.document();
                    String path = doc.getId();
                    postpic(prey.getPic(),path);
                    prey.setPic(path+"/image");
                    FirebaseFirestore db= FirebaseFirestore.getInstance();

                    db.collection("Prey").document(path).set(prey);
                    counter+=1;
                }
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
                String json =gson.toJson(new ArrayList<Prey>());
                String counters = String.valueOf(counter);
                editor.putString("prey",json);
                editor.apply();
                Toast.makeText(getContext(), counters + " Prey uploaded",Toast.LENGTH_LONG).show();


            }
        });

        // Add data from Firebase on the the Arrays
        new PreyAsyncTask(this).execute();

        return PreyFragment;
    }
    /*
    @Override
    public void onClick(View v) {
        pos =recyclerView.getChildLayoutPosition(v);
        Prey selectiion=CPreyArrayList.get(pos);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Prey",selectiion.getPrey());

        editor.apply();
        startActivity(new Intent(getContext(), Station_List.class));



    }

*/

    //SationAsyncTask would update this
    public void updatePreyList(ArrayList<Prey> s){
        CPreyArrayList.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        preyFragmentAdapter=new PreyFragmentAdapter(CPreyArrayList,this);
        recyclerView.setAdapter(preyFragmentAdapter);
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

    public static PreyFragment newInstance() {
        return new PreyFragment();
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


    @Override
    public void onClick(View v) {
        pos =recyclerView.getChildLayoutPosition(v);
        Prey selectiion=CPreyArrayList.get(pos);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        Intent I= new Intent(getContext(), PreyExpand.class);
        I.putExtra("Prey",selectiion);
        startActivity(I);

    }
    private void postpic(String uri,String Path){
        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(uri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datan = baos.toByteArray();
            StorageReference profile = fStorage.getReference(Path + "/image");
            UploadTask uploadTask = profile.putBytes(datan);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

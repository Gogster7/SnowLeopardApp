package org.kashmirworldfoundation.snowleopardapp.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import org.kashmirworldfoundation.snowleopardapp.R;


import java.util.Calendar;
import java.util.Date;

import android.content.SharedPreferences;


import android.provider.ContactsContract;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.kashmirworldfoundation.snowleopardapp.CameraStation;
import org.kashmirworldfoundation.snowleopardapp.Member;
import org.kashmirworldfoundation.snowleopardapp.MyDateTypeAdapter;
import org.kashmirworldfoundation.snowleopardapp.R;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class AddFragment  extends Fragment implements View.OnClickListener{
    private static final String TAG = "Add fragment";
    private EditText stationIdInput;
    private EditText WatershedInput;
    private EditText LatitudeInput;
    private EditText LongitudeInput;
    private EditText ElevationInput;
    private EditText cameraIDInput;
    private String stationId;
    private String watershedid;
    private String latitudeS;
    private String longitudeS;
    private String altitudeS;
    private String altitude;
    private String cameraId;
    private String terrain;
    private String habitat;
    private String lureType;
    private String substrate;
    private String potential;

    private static final int LOCATION_REQUEST = 111;
    private View fragmentView ;
    private Button save;
    private Date currentTime;

    private TextView  netStatus;
    private FirebaseFirestore db;
    private Integer counter=0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_add, container, false);
        stationIdInput=fragmentView.findViewById(R.id.stationId);
        WatershedInput=fragmentView.findViewById(R.id.watershedid);
        LatitudeInput=fragmentView.findViewById(R.id.nId);
        LongitudeInput=fragmentView.findViewById(R.id.eId);
        ElevationInput=fragmentView.findViewById(R.id.elevationId);
        cameraIDInput=fragmentView.findViewById(R.id.cameraId);
        netStatus=fragmentView.findViewById(R.id.createStationNetStatusId);
        save=fragmentView.findViewById(R.id.saveButtonId);
        db=FirebaseFirestore.getInstance();
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog();
                Member me = loaduser();
                saveDialog();
                getInput();
                CameraStation cam = new CameraStation();
                Date currentTime = Calendar.getInstance().getTime();
                cam.setPotential(potential);
                cam.setLureType(lureType);
                cam.setAltitude(altitudeS);
                cam.setLongitudeS(longitudeS);
                cam.setLatitudeS(latitudeS);
                cam.setStationId(stationId);
                cam.setSubstrate(substrate);
                cam.setTerrain(terrain);
                cam.setWatershedid(watershedid);
                cam.setHabitat(habitat);
                cam.setOrg(me.getOrg());
                cam.setAuthor(loaduid());
                cam.setCameraId(cameraId);
                cam.setPosted(new Timestamp(currentTime));
                cam.setPic(me.getProfile());

                ArrayList<CameraStation> list = load();

                list.add(cam);
                save(list);
                refresh();
            }
        });

        //get the bundle and the argument from mainActivity
        Bundle b= this.getArguments();
        if(b!=null){
            latitudeS=b.getString("latitude");
            longitudeS=b.getString("longitude");
            altitudeS=b.getString("altitude");
        }else{
            Log.d(TAG, "Bundle is null");
        }

        //initial the radiobutton Group
        onClick(fragmentView);

        //check net status
        doNetCheck();

        return fragmentView;
    }
    public static AddFragment newInstance() {
        return new AddFragment();
    }


    //onStart the fragment for set Text
    @Override
    public void onStart(){
        super.onStart();
        this.LongitudeInput.setText(longitudeS);
        this.LatitudeInput.setText(latitudeS);
        this.ElevationInput.setText(altitudeS);
    }

    @Override
    public void onResume(){
        super.onResume();
        this.LongitudeInput.setText(longitudeS);
        this.LatitudeInput.setText(latitudeS);
        this.ElevationInput.setText(altitudeS);
    }

    //refresh the fragment
    public void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(AddFragment.this).attach(AddFragment.this).commit();
    }


    //for radio button
    @Override
    public void onClick(View v) {
        RadioGroup radioGroup01 = (RadioGroup) fragmentView .findViewById(R.id.radioGroup01);
        radioGroup01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch(checkedId) {
                    case R.id.radioButtonSkunkId:
                        lureType = "Skunk + Fish Oil";
                        Toast.makeText(getActivity(), "Lure Type You Selected Skunk + Fish Oil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonCastorId:
                        lureType = "Castor + Fish Oil";
                        Toast.makeText(getActivity(), "Lure Type You Selected Castor + Fish Oil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonFishId:
                        lureType = "Fish Oil";
                        Toast.makeText(getActivity(), "Lure Type You Selected Fish Oil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonNoneId:
                        lureType = "None";
                        Toast.makeText(getActivity(), "Lure Type You Selected None", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        RadioGroup radioGroup02 = (RadioGroup) fragmentView .findViewById(R.id.radioGroup02);
        radioGroup02.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch(checkedId) {
                    case R.id.radioButtonScrubId:
                        habitat = "Scrub";
                        Toast.makeText(getActivity(), "Habitat You Selected Scrub", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonForestId:
                        habitat = "Forest";
                        Toast.makeText(getActivity(), "Habitat You Selected Forest", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonPastureId:
                        habitat = "Pasture";
                        Toast.makeText(getActivity(), "Habitat You Selected Pasture", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonBarrenId:
                        habitat = "Barren";
                        Toast.makeText(getActivity(), "Habitat You Selected Barren", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonAgricId:
                        habitat = "Agric";
                        Toast.makeText(getActivity(), "Habitat You Selected Agric", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        RadioGroup radioGroup03 = (RadioGroup) fragmentView .findViewById(R.id.radioGroup03);
        radioGroup03.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch(checkedId) {
                    case R.id.radioButtonRidgeId:
                        terrain = "Ridge";
                        Toast.makeText(getActivity(), "Terrain You Selected Ridge", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonCliffBaseId:
                        terrain = "Cliff Base";
                        Toast.makeText(getActivity(), "Terrain You Selected Cliff Base", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonDrawId:
                        terrain = "Draw";
                        Toast.makeText(getActivity(), "Terrain You Selected Draw", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonValleyId:
                        terrain = "Valley";
                        Toast.makeText(getActivity(), "Terrain You Selected Valley", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonSaddleId:
                        terrain = "Saddle";
                        Toast.makeText(getActivity(), "Terrain You Selected Saddle", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonPlateauId:
                        terrain = "Plateau";
                        Toast.makeText(getActivity(), "Terrain You Selected Plateau", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        RadioGroup radioGroup04 = (RadioGroup) fragmentView .findViewById(R.id.radioGroup04);
        radioGroup04.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch(checkedId) {
                    case R.id.radioButtonSandId:
                        substrate= "Sand";
                        Toast.makeText(getActivity(), "Substrate You Selected Sand", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonSoilId:
                        substrate= "Soil";
                        Toast.makeText(getActivity(), "Substrate You Selected Soil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonRockId:
                        substrate= "Rock/Gravel";
                        Toast.makeText(getActivity(), "Substrate You Selected Rock/Gravel", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonSnowId:
                        substrate= "Snow";
                        Toast.makeText(getActivity(), "Substrate You Selected Snow", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonVegetationId:
                        substrate= "Vegetation";
                        Toast.makeText(getActivity(), "Substrate You Selected Vegetation", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

        RadioGroup radioGroup05 = (RadioGroup) fragmentView .findViewById(R.id.radioGroup05);
        radioGroup05.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch(checkedId) {
                    case R.id.radioButtonGoodId:
                        potential = "Good";
                        Toast.makeText(getActivity(), "Potential You Selected Good", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonMediumId:
                        potential = "Medium";
                        Toast.makeText(getActivity(), "Potential You Selected Medium", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonPoorId:
                        potential = "Poor";
                        Toast.makeText(getActivity(), "Potential You Selected Poor", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });



    }

    public boolean getInput(){
        Log.d(TAG, "In getInput ");
        stationId=stationIdInput.getText().toString().trim();
        watershedid=WatershedInput.getText().toString().trim();
        latitudeS=LatitudeInput.getText().toString().trim();
        longitudeS=LongitudeInput.getText().toString().trim();
        altitude=ElevationInput.getText().toString().trim();
        cameraId=cameraIDInput.getText().toString().trim();
        //for debug
        Log.d(TAG, "getInput; station id :"+stationId+" watersheid:  "+watershedid+" latitude:  "+latitudeS+" longtitude:  "+longitudeS+" elevation:  "+altitude+" cameraId:  "+cameraId+" Time:  "+currentTime);
        Log.d(TAG, "getInput; terrain: "+terrain+" habitat :"+habitat+" lureType : "+lureType+" substrate: "+substrate+" potential station :"+potential);

        //prevent some blank
        if (stationId.equals("")||watershedid.equals("")||cameraId.equals("")||terrain==null||habitat==null||lureType==null||substrate==null||potential==null){
            return false;
        }
        return true;
    }

    //check internet status
    private Boolean doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            netStatus.setText("On");
            return true;
        } else {
            netStatus.setText("Off");
            return false;
        }
    }

    public void saveDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.kwflogo);
        builder.setTitle("Save the Station?");
        if(doNetCheck()){
            builder.setMessage("Internet : On");
        }else{
            builder.setMessage("Internet: Off , it will upload data later.");
        }

        currentTime = Calendar.getInstance().getTime();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //check has some blank or not
                if(getInput()){
                    createToast(getActivity(),"Create Station Successful :"+currentTime.toString(), Toast.LENGTH_LONG);
                }else{
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "CANCEL", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
    private void save(ArrayList<CameraStation> list){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("camstations",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
        String json =gson.toJson(list);

        editor.putString("station",json);
        editor.apply();
    }
    private ArrayList<CameraStation> load(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("camstations",Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
        String json =sharedPreferences.getString("station",null);
        if (json==null){
            return new ArrayList<CameraStation>();
        }
        Type type = new TypeToken<ArrayList<CameraStation>>() {}.getType();
        return gson.fromJson(json,type);
    }
    private String loaduid(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("uid",null);
    }
    private Member loaduser(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson= new Gson();
        String json = sharedPreferences.getString("user", null);
        Type type =new TypeToken<Member>(){}.getType();
        return gson.fromJson(json,type);
    }
    private void Fireload(final ArrayList<CameraStation> list){
        final Iterator<CameraStation> iter= list.iterator();
        Integer remain =list.size();
        while(iter.hasNext()){
            CameraStation station= iter.next();
            db.collection("CameraStation").add(station).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        iter.remove();
                        counter+=1;

                    }
                    else{

                    }
                }
            });
            String count = counter.toString();
            String rem =remain.toString();
            createToast(getActivity(),count + " stations sent to firebase" +rem +"stations remain",Toast.LENGTH_SHORT);
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("camstations",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
        String json =gson.toJson(list);
        editor.putString("station",json);
        editor.apply();
    }

}

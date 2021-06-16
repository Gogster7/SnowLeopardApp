package org.kashmirworldfoundation.snowleopardapp.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Color;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.kashmirworldfoundation.snowleopardapp.CreateStudy;
import org.kashmirworldfoundation.snowleopardapp.R;


import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;

import java.util.Calendar;
import java.util.Date;

import android.content.SharedPreferences;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.kashmirworldfoundation.snowleopardapp.CameraStation;
import org.kashmirworldfoundation.snowleopardapp.Member;
import org.kashmirworldfoundation.snowleopardapp.MyDateTypeAdapter;
import org.kashmirworldfoundation.snowleopardapp.Study;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class AddFragment  extends Fragment implements View.OnClickListener{
    private static final String TAG = "Add fragment";
    private static final int PICK_IMAGE1 = 100;
    private static final int PICK_IMAGE2 = 200;
    private EditText stationIdInput;
    private EditText WatershedInput;
    private EditText LatitudeInput;
    private EditText LongitudeInput;
    private EditText ElevationInput;
    private EditText cameraIDInput;
    private EditText NotesInput;
    private EditText SdcardInput;
    private TextView Tittle;
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
    private String notes;
    private String sdcard;
    private static final int LOCATION_REQUEST = 111;
    private View fragmentView ;
    private Button save,post,StudyAdd,StudyRefresh,Studyback;
    private String SStudy;
    private ArrayList<String> StudyArray;
    private Date currentTime;
    private ImageButton imgbtn1, imgbtn2;
    private Spinner SpinStudies;
    private Member me;
    private TextView  netStatus;
    private FirebaseFirestore db;
    private FirebaseStorage fStorage;
    private Integer counter=0;
    private CameraStation current= new CameraStation();
    private Boolean[] pic = {Boolean.FALSE,Boolean.FALSE};
    private Boolean Study=Boolean.TRUE;
    private ArrayAdapter<String> dataAdapter;
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
        imgbtn1 = fragmentView.findViewById(R.id.CamPic1);
        imgbtn2 = fragmentView.findViewById(R.id.CamPic2);
        post=fragmentView.findViewById(R.id.postbuttonCam);
        SdcardInput = fragmentView.findViewById(R.id.SdcardInput);
        SpinStudies = (Spinner) fragmentView.findViewById(R.id.StudySpinner);
        Tittle= fragmentView.findViewById(R.id.createStationLabel);
        db=FirebaseFirestore.getInstance();
        fStorage= FirebaseStorage.getInstance();
        NotesInput =fragmentView.findViewById(R.id.NoteInput);

        StudyAdd = fragmentView.findViewById(R.id.AddStudyBtn);
        StudyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateStudy.class));
            }
        });
        StudyRefresh=fragmentView.findViewById(R.id.RefreshStudyBtn);
        Studyback= fragmentView.findViewById(R.id.ChooseStudyBtn);
        me =loaduser();
        /*
        String StudiesS = getActivity().getIntent().getStringExtra("Studies");
        StudyArray=new ArrayList<>(Arrays.asList(StudiesS.split(",")));
        */
        StudyArray = new ArrayList<>();
        StudyArray=loadStudies();

        change();


        dataAdapter= new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, StudyArray);

        SpinStudies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cur = StudyArray.get(position);
                if (cur.equals( "Pick A Study")==Boolean.FALSE && cur.equals("No Studies")==Boolean.FALSE) {
                    SStudy = cur;
                    SpinStudies.setVisibility(View.GONE);
                    Tittle.setText("Add CameraStation");
                    fragmentView.findViewById(R.id.AddStudyBtn).setVisibility(View.GONE);
                    fragmentView.findViewById(R.id.RefreshStudyBtn).setVisibility(View.GONE);
                    stationIdInput.setVisibility(View.VISIBLE);
                    WatershedInput.setVisibility(View.VISIBLE);
                    LatitudeInput.setVisibility(View.VISIBLE);
                    LongitudeInput.setVisibility(View.VISIBLE);
                    ElevationInput.setVisibility(View.VISIBLE);
                    cameraIDInput.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    imgbtn1.setVisibility(View.VISIBLE);
                    imgbtn2.setVisibility(View.VISIBLE);

                    SdcardInput.setVisibility(View.VISIBLE);
                    NotesInput.setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.habitatIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.cameraIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.watershedIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.SdCardLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.lureTypeIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.TerrainIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.substrateIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.notesLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.stationP2IdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.potentialIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.nId).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.eId).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.stationId).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.elevationId).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.stationId).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.LongitudeIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.cameraId).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.SdcardInput).setVisibility(View.VISIBLE);

                    fragmentView.findViewById(R.id.stationPIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.watershedid).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.elevationIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.LatitudeIdLabel).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.radioGroup01).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.radioGroup04).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.radioGroup03).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.radioGroup02).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.radioGroup05).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.saveButtonId).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.postbuttonCam).setVisibility(View.GONE);
                    fragmentView.findViewById(R.id.createStationNetId).setVisibility(View.INVISIBLE);
                    fragmentView.findViewById(R.id.createStationNetStatusId).setVisibility(View.INVISIBLE);
                    fragmentView.findViewById(R.id.CamPic1).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.CamPic2).setVisibility(View.VISIBLE);
                    fragmentView.findViewById(R.id.NoteInput).setVisibility(View.VISIBLE);
                    Studyback.setVisibility(View.VISIBLE);


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        Studyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               change();

            }
        });
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinStudies.setAdapter(dataAdapter);
        StudyRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Study").whereEqualTo("org",me.getOrg()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> studies= new ArrayList<>();
                            studies.add("Pick A Study");
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                Study study = documentSnapshot.toObject(Study.class);
                                studies.add(study.getTittle());
                            }
                            if (studies.size()==1){
                                studies.set(0,"No Studies");
                            }
                            dataAdapter.clear();
                            dataAdapter.addAll(studies);
                        }
                    }
                });
            }
        });
        imgbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(PICK_IMAGE1);
            }
        });
        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(PICK_IMAGE2);
            }
        });
        post.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<CameraStation> Clist= load();
                if (Clist.size()==0){
                    createToast(getContext(),"No Camerat stations saved", Toast.LENGTH_LONG);
                }
                else{
                    Fireload(Clist);
                    ScrollView scroll= fragmentView.findViewById(R.id.ScrollViewAdd);
                    scroll.scrollTo(0,0);
                }

            }
        });
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getInput()){
                    return;
                }






                Date currentTime = Calendar.getInstance().getTime();
                current.setPotential(potential);
                current.setLureType(lureType);
                current.setAltitude(altitudeS);
                current.setLongitudeS(longitudeS);
                current.setLatitudeS(latitudeS);
                current.setStationId(stationId);
                current.setSubstrate(substrate);
                current.setTerrain(terrain);
                current.setWatershedid(watershedid);
                current.setHabitat(habitat);
                current.setOrg(me.getOrg());
                current.setAuthor("Member/" + loaduid());
                current.setCameraId(cameraId);
                current.setPosted(new Timestamp(currentTime));
                current.setPic(me.getProfile());
                current.setaName(me.getFullname());
                current.setNotes(notes);
                current.setSdCard(sdcard);
                current.setStudy(SStudy);
                ArrayList<CameraStation> list = load();

                list.add(current);
                save(list);
                current=new CameraStation();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("SD Card");
                alertDialog.setMessage("Has the SD Card been formatted");


                alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        change();


                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();

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
        notes = NotesInput.getText().toString().trim();
        sdcard = SdcardInput.getText().toString().trim();

        //for debug
        Log.d(TAG, "getInput; station id :"+stationId+" watersheid:  "+watershedid+" latitude:  "+latitudeS+" longtitude:  "+longitudeS+" elevation:  "+altitude+" cameraId:  "+cameraId+" Time:  "+currentTime);
        Log.d(TAG, "getInput; terrain: "+terrain+" habitat :"+habitat+" lureType : "+lureType+" substrate: "+substrate+" potential station :"+potential);

        //prevent some blank
        if (stationId.equals("")){
            createToast(getContext(),"Station Id needed",Toast.LENGTH_LONG);
            return false;
        }
        else if(watershedid.equals("")){
            createToast(getContext(),"WaterShed Id needed",Toast.LENGTH_LONG);
            return false;
        }
        else if(cameraId.equals("")){
            createToast(getContext(),"Camera Id needed",Toast.LENGTH_LONG);
            return false;
        }
        else if(terrain==null){
            createToast(getContext(),"Terrain needed",Toast.LENGTH_LONG);
            return false;

        }
        else if (habitat==null){
            createToast(getContext(),"Habitat needed",Toast.LENGTH_LONG);
            return false;
        }
        else if (lureType==null){
            createToast(getContext(),"Lure Type needed",Toast.LENGTH_LONG);
            return false;
        }
        else if(substrate==null){
            createToast(getContext(),"Station Id needed",Toast.LENGTH_LONG);
            return false;
        }
        else if(potential==null){
            createToast(getContext(),"Potential needed",Toast.LENGTH_LONG);
            return false;
        }
        else if(sdcard.equals("")){
            createToast(getContext(),"SD card  ID needed",Toast.LENGTH_LONG);
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
            // builder.setMessage("Internet : On");
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
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
        String json =gson.toJson(list);

        editor.putString("station",json);
        editor.apply();
    }
    private ArrayList<CameraStation> load(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
        String json =sharedPreferences.getString("station",null);
        if (json==null){
            return new ArrayList<>();
        }
        else{
            createToast(getContext(), "CameraStations here",Toast.LENGTH_LONG);

            Type type = new TypeToken<ArrayList<CameraStation>>() {}.getType();
            return gson.fromJson(json,type);



        }

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
    private ArrayList<String> loadStudies(){
        SharedPreferences sharedPreferences =getContext().getSharedPreferences("user",Context.MODE_PRIVATE);


        Gson gson= new Gson();
        String json = sharedPreferences.getString("studies",null);
        Type type= new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(json,type);

    }
    private void Fireload(final ArrayList<CameraStation> list) {
        final Iterator<CameraStation> iter = list.iterator();
        CollectionReference collection = db.collection("CameraStation");
        while (iter.hasNext()) {
            CameraStation station = iter.next();
            DocumentReference doc = collection.document();
            String path = doc.getId();
            if (station.getCamerapic1() != null) {
                pic[0] = Boolean.TRUE;

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(station.getCamerapic1()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] datan = baos.toByteArray();
                    StorageReference profile = fStorage.getReference(path + "/image1");
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
            if (station.getCamerapic2() != null) {

                try {
                    pic[1] = Boolean.TRUE;

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(station.getCamerapic2()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] datan = baos.toByteArray();
                    StorageReference profile = fStorage.getReference(path + "/image2");
                    UploadTask uploadTask = profile.putBytes(datan);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                            }

                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (pic[0]) {
                station.setCamerapic1(path + "/image1");
            }
            if (pic[1]) {
                station.setCamerapic2(path + "/image2");
            }
            station.setFirebaseId("CameraStation/"+path);
            db.collection("CameraStation").document(path).set(station).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    iter.remove();
                    counter +=1;
                    String count = counter.toString();
                    Integer size = list.size();
                    String rem = size.toString();
                    createToast(getActivity(), count + " stations sent to firebase " + rem + " stations remain", Toast.LENGTH_SHORT);

                }
            });

            counter = 0;
            save(new ArrayList<>());
        }
    }
    private void change(){
        SpinStudies.setVisibility(View.VISIBLE);
        SpinStudies.setAdapter(dataAdapter);

        Tittle.setText("Choose Study");
        fragmentView.findViewById(R.id.AddStudyBtn).setVisibility(View.VISIBLE);
        StudyRefresh.setVisibility(View.VISIBLE);
        stationIdInput.setVisibility(View.GONE);
        stationIdInput.setText("");
        WatershedInput.setVisibility(View.GONE);
        WatershedInput.setText("");
        LatitudeInput.setVisibility(View.GONE);
        LatitudeInput.setText("");
        LongitudeInput.setVisibility(View.GONE);
        LongitudeInput.setText("");
        ElevationInput.setVisibility(View.GONE);
        ElevationInput.setText("");
        cameraIDInput.setVisibility(View.GONE);
        cameraIDInput.setText("");
        save.setVisibility(View.GONE);
        imgbtn1.setVisibility(View.GONE);

        imgbtn2.setVisibility(View.GONE);

        SdcardInput.setVisibility(View.GONE);
        SdcardInput.setText("");
        NotesInput.setVisibility(View.GONE);
        NotesInput.setText("");
        fragmentView.findViewById(R.id.habitatIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.cameraIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.watershedIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.SdCardLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.lureTypeIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.TerrainIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.substrateIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.notesLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.stationP2IdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.potentialIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.nId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.eId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.stationId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.elevationId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.stationId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.LongitudeIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.cameraId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.SdcardInput).setVisibility(View.GONE);

        fragmentView.findViewById(R.id.stationPIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.watershedid).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.elevationIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.LatitudeIdLabel).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.radioGroup01).setVisibility(View.GONE);
        RadioGroup rad=(RadioGroup) fragmentView.findViewById(R.id.radioGroup01);
        rad.clearCheck();

        fragmentView.findViewById(R.id.radioGroup04).setVisibility(View.GONE);
        rad=fragmentView.findViewById(R.id.radioGroup04);
        rad.clearCheck();
        fragmentView.findViewById(R.id.radioGroup03).setVisibility(View.GONE);
        rad=fragmentView.findViewById(R.id.radioGroup03);
        rad.clearCheck();
        fragmentView.findViewById(R.id.radioGroup02).setVisibility(View.GONE);
        rad=fragmentView.findViewById(R.id.radioGroup02);
        rad.clearCheck();
        fragmentView.findViewById(R.id.radioGroup05).setVisibility(View.GONE);
        rad=fragmentView.findViewById(R.id.radioGroup05);
        rad.clearCheck();
        fragmentView.findViewById(R.id.saveButtonId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.postbuttonCam).setVisibility(View.VISIBLE);
        fragmentView.findViewById(R.id.createStationNetId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.createStationNetStatusId).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.CamPic1).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.CamPic2).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.NoteInput).setVisibility(View.GONE);
        Studyback.setVisibility(View.GONE);
        fragmentView.findViewById(R.id.ScrollViewAdd).scrollTo(0,0);
        ImageButton img=fragmentView.findViewById(R.id.CamPic1);
        img.setImageResource(R.drawable.camera);
        img=fragmentView.findViewById(R.id.CamPic2);
        img.setImageResource(R.drawable.camera);
    }
    private void openGallery(int PICK_IMAGE){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestcode,int resultcode, @Nullable Intent data){
        super.onActivityResult(requestcode,resultcode,data);

        if (resultcode== Activity.RESULT_OK && requestcode ==PICK_IMAGE1){
            Uri imageurl =data.getData();
            imgbtn1.setImageURI(imageurl);

            imgbtn1.setDrawingCacheEnabled(true);
            imgbtn1.buildDrawingCache();
            //current.setCamerapic1(ImageFilePath.getPath(getContext(), data.getData()));
            current.setCamerapic1(imageurl.toString());
            Log.e(TAG, imageurl.toString() );


        }
        if (resultcode== Activity.RESULT_OK && requestcode ==PICK_IMAGE2){
            Uri imageurl =data.getData();
            imgbtn2.setImageURI(imageurl);
            imgbtn2.setDrawingCacheEnabled(true);
            imgbtn2.buildDrawingCache();
            current.setCamerapic2(imageurl.toString());
            //current.setCamerapic2(ImageFilePath.getPath(getContext(),data.getData()));



        }

    }



}

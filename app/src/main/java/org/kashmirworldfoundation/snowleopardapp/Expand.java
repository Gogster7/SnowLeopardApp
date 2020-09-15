package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;

public class Expand extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    TextView title, date,cameraid,substrate,lureType,potential,terrain,watershedid,author;
    //    TextView dateLabel;
    int position;
    private static final String TAG = "Expand";
    private Button rebaitBtn;
    private Button nxtBtn;
    private Button nxttBtn; // second Next button
    private Button subBtn; // submit button
    private EditText numPicsInput;
    private EditText signsInput;
    private EditText sdInput;
    private String numPics;
    private String signsT;
    private String sdT;
    private String sLureType;
    private String camWorks;
    private RadioGroup baitGroup;
    private RadioGroup camGroup; // camera functional
    private Date currentTime;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference noteRef = db.document("Notebook/Rebait Log");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // set in manifest
        }

        imageView = findViewById(R.id.expand_imageView);
        title = findViewById(R.id.titleText);
        date = findViewById(R.id.dateText);
        cameraid =findViewById(R.id.cameraId);
        substrate=findViewById(R.id.substrateId);
        lureType=findViewById(R.id.lureTypeId);
        potential=findViewById(R.id.potentialid);
        terrain=findViewById(R.id.terrainId);
        watershedid=findViewById(R.id.watershedid);
        author=findViewById(R.id.authorId);
        rebaitBtn = findViewById(R.id.rebaitBtn);

        // Matching User Listener to Takedown button if form does not belong to current user


        //Station station = (Station) getIntent().getSerializableExtra("station");

        CameraStation station =getIntent().getParcelableExtra("station");

        title.setText(station.getStationId());
        date.setText(station.getPosted().toDate().toString());
        cameraid.setText(station.getCameraId());
        substrate.setText(station.getSubstrate());
        lureType.setText(station.getLureType());
        potential.setText(station.getPotential());
        terrain.setText(station.getTerrain());
        watershedid.setText(station.getWatershedid());
        author.setText(station.getAuthor());

        //need to change later
        imageView.setImageResource(R.drawable.kwflogo);

        // Re-Bait Button function
        rebaitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Expand.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.rebait_sheet,
                                (LinearLayout)findViewById(R.id.rebaitSheetContainer)
                        );
                Log.d(TAG, "onClick: POP UP" +v);
                baitGroup = (RadioGroup) bottomSheetView.findViewById(R.id.baitGroup);
                // Next Button
                nxtBtn = (Button) bottomSheetView.findViewById(R.id.btnNext);
                nxtBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View bottomSheetView2 = LayoutInflater.from(getApplicationContext())
                                .inflate(
                                        R.layout.rebait_sheet1,
                                        (LinearLayout)findViewById(R.id.rebaitSheetContainer1)
                                );
                        bottomSheetDialog.setContentView(bottomSheetView2);
                        bottomSheetDialog.show();
                        camGroup = (RadioGroup) bottomSheetView2.findViewById(R.id.camGroup);
                        numPicsInput = (EditText) bottomSheetView2.findViewById(R.id.picCountId);
                        numPicsInput.setText("0");
                        //overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                        // Second Next Button
                        nxttBtn = (Button) bottomSheetView2.findViewById(R.id.btnNextt);
                        nxttBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                View bottomSheetView3 = LayoutInflater.from(getApplicationContext())
                                        .inflate(
                                                R.layout.rebait_sheet2,
                                                (LinearLayout)findViewById(R.id.rebaitSheetContainer2)
                                        );
                                bottomSheetDialog.setContentView(bottomSheetView3);
                                bottomSheetDialog.show();
                                Log.d(TAG, "onClick: Next");
                                Toast.makeText(Expand.this, "Rebaited", Toast.LENGTH_SHORT).show();
                                signsInput = (EditText) bottomSheetView3.findViewById(R.id.signsInput);
                                sdInput = (EditText) bottomSheetView2.findViewById(R.id.sdInput);
                                subBtn = (Button) bottomSheetView3.findViewById(R.id.subBtn);
                                subBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (getInput()) {
                                            // send to Firebase
                                        }
                                        else{
                                            // Toast message display "missing input"
                                        }
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }


    @Override
    public void onClick(View v) {
    // bait selection
        baitGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "onCheckedChanged: "+checkedId);
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radioSkunkId:
                        sLureType = "Skunk + Fish Oil";
                        nxttBtn.setEnabled(true);
                        break;
                    case R.id.radioCastorId:
                        sLureType = "Castor + Fish Oil";
                        nxttBtn.setEnabled(true);
                        break;
                    case R.id.radioFishId:
                        sLureType = "Fish Oil";
                        nxttBtn.setEnabled(true);
                        break;
                    case R.id.radioNoneId:
                        sLureType = "None";
                        nxttBtn.setEnabled(true);
                        break;
                }
            }
        });
    // Next Question
        camGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "onCheckedChanged: "+checkedId);
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.yesId:
                        camWorks = "Yes";
                        nxttBtn.setEnabled(true);
                        break;
                    case R.id.noId:
                        camWorks = "No";
                        nxttBtn.setEnabled(true);
                        break;
                }
            }
        });
    }

    public boolean getInput(){
        Log.d(TAG, "In getInput ");
        numPics=numPicsInput.getText().toString().trim();
        signsT=signsInput.getText().toString().trim();
        sdT=sdInput.getText().toString().trim();

        //for debug
        Log.d(TAG, "getInput; num Pics :"+numPics+" Signs :"+signsT+" SD :"+sdT);

        //prevent some blank
        if (numPics.equals("")||sdT.equals("")||sLureType==null||camWorks==null){
            return false;
        }
        return true;
    }

    public void addNote(View v) {
        currentTime = Calendar.getInstance().getTime();

        Rebait note = new Rebait(numPics, signsT, sdT, sLureType, camWorks);

        notebookRef.add(note);
    }




}

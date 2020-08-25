package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private String sLureType;
    private RadioGroup baitGroup;


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


        Station station = (Station) getIntent().getSerializableExtra("station");

        title.setText(station.getStationId());
        date.setText(station.getPosted());
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
                bottomSheetView.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: Next");
                        Toast.makeText(Expand.this, "Rebaited", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
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
                        nxtBtn.setEnabled(true);
                        break;
                    case R.id.radioCastorId:
                        sLureType = "Castor + Fish Oil";
                        nxtBtn.setEnabled(true);
                        break;
                    case R.id.radioFishId:
                        sLureType = "Fish Oil";
                        nxtBtn.setEnabled(true);
                        break;
                    case R.id.radioNoneId:
                        sLureType = "None";
                        nxtBtn.setEnabled(true);
                        break;
                }
            }
        });
    }
}

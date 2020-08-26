package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class Expand extends AppCompatActivity{

    ImageView imageView;
    TextView title, date,cameraid,substrate,lureType,potential,terrain,watershedid,author;
    //    TextView dateLabel;
    int position;
    private static final String TAG = "Expand";


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

    }

}

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
    TextView title, date,cameraid,substrate,lureType,potential,terrain,watershedid;
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


        Intent intent = getIntent();

        Bundle bundle = this.getIntent().getExtras();
        int pic = bundle.getInt("image");
        String aTitle = intent.getStringExtra("title");
        String aDate = intent.getStringExtra("date");
        imageView.setImageResource(pic);
        title.setText(aTitle);
        date.setText(aDate);

        //get data from station object
        Station station=(Station)intent.getSerializableExtra("station");
        cameraid.setText(station.getCameraId());
        substrate.setText(station.getSubstrate());
        lureType.setText(station.getLureType());
        potential.setText(station.getPotential());
        terrain.setText(station.getTerrain());
        watershedid.setText(station.getWatershedid());


        //actionBar.setTitle(aTitle);

/*        if (position == 0) {
            Intent intent = getIntent();

            Bundle bundle = this.getIntent().getExtras();
            int pic = bundle.getInt("image");
            String aTitle = intent.getStringExtra("title");
            String aDate = intent.getStringExtra("date");

            imageView.setImageResource(pic);
            title.setText(aTitle);
            date.setText(aDate);

            //actionBar.setTitle(aTitle);
        }

        if (position == 1) {
            Intent intent = getIntent();

            Bundle bundle = this.getIntent().getExtras();
            int pic = bundle.getInt("image");
            String aTitle = intent.getStringExtra("title");
            String aDate = intent.getStringExtra("date");

            imageView.setImageResource(pic);
            title.setText(aTitle);
            date.setText(aDate);

            //actionBar.setTitle(aTitle);
        }

        if (position == 2) {
            Intent intent = getIntent();

            Bundle bundle = this.getIntent().getExtras();
            int pic = bundle.getInt("image");
            String aTitle = intent.getStringExtra("title");
            String aDate = intent.getStringExtra("date");

            imageView.setImageResource(pic);
            title.setText(aTitle);
            date.setText(aDate);

            //actionBar.setTitle(aTitle);
        }

        if (position == 3) {
            Intent intent = getIntent();

            Bundle bundle = this.getIntent().getExtras();
            int pic = bundle.getInt("image");
            String aTitle = intent.getStringExtra("title");
            String aDate = intent.getStringExtra("date");

            imageView.setImageResource(pic);
            title.setText(aTitle);
            date.setText(aDate);

            //actionBar.setTitle(aTitle);
        }*/
    }

}

package org.kashmirworldfoundation.snowleopardapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MapCreateActivity extends AppCompatActivity {
    private TextView testShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapcreate);

        testShow=findViewById(R.id.testCreateMapTextId);
        Intent i =this.getIntent();
        String test =i.getStringExtra("Name");
        testShow.setText(test);

    }
}

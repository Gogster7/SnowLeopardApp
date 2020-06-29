package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Expand extends AppCompatActivity {

    ImageView imageView;
    TextView title, date;
    int position;

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

        Intent intent = getIntent();

        Bundle bundle = this.getIntent().getExtras();
        int pic = bundle.getInt("image");
        String aTitle = intent.getStringExtra("title");
        String aDate = intent.getStringExtra("date");

        imageView.setImageResource(pic);
        title.setText(aTitle);
        date.setText(aDate);

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

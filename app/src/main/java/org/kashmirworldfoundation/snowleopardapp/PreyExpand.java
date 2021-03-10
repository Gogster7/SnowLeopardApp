package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

public class PreyExpand extends AppCompatActivity {
    private TextView Author,Longitude,Lattitude,Notes,Date,Prey,Back;

    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prey_expand);
        Author= findViewById(R.id.EXPreyAuthorText);
        Lattitude=findViewById(R.id.EXPreyLattitudeText);
        Longitude=findViewById(R.id.EXPreyLongitudeText);
        Notes=findViewById(R.id.EXPreyNotesText);
        Prey=findViewById(R.id.EXPreyText);
        img=findViewById(R.id.PreyExpandImage);
        Back= findViewById(R.id.PreyExpandBack);
        Date=findViewById(R.id.EXPreyDateText);
        Back= findViewById(R.id.PreyExpandBack);
        Prey prey= getIntent().getParcelableExtra("Prey");
        Lattitude.setText(prey.getLatitudeS());
        Longitude.setText(prey.getLongitudeS());
        Notes.setText(prey.getNote());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(prey.getPosted().toDate());
        Date.setText(format);
        Prey.setText(prey.getPrey());
        Author.setText(prey.getAuthor());
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        StorageReference ref = FirebaseStorage.getInstance().getReference(prey.getPic());
        GlideApp.with(this)
                .load(ref)
                .into(img);


    }

}
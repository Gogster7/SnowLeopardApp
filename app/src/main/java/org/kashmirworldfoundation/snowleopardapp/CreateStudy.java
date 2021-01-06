package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateStudy extends AppCompatActivity {
    private EditText TittleInput;
    private EditText LocationInput;
    private EditText MissionInput;
    private EditText StartInput;
    private EditText EndInput;
    private Button Post;
    private TextView Back;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study);
        TittleInput=findViewById(R.id.StudyTittleInput);
        LocationInput=findViewById(R.id.StudyLocationInput);
        MissionInput=findViewById(R.id.StudyMissionInput);
        StartInput=findViewById(R.id.StudyDateStartInput);
        EndInput=findViewById(R.id.StudyDateEndInput);
        Post = findViewById(R.id.StudyPostBtn);
        Back= findViewById(R.id.StudyBack);
        db= FirebaseFirestore.getInstance();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Study study= new Study();
                study.setTittle(TittleInput.getText().toString());
                study.setLocation(LocationInput.getText().toString());
                study.setMission(MissionInput.getText().toString());
                String start =StartInput.getText().toString();
                String end =EndInput.getText().toString();
                Date date1;
                Date date2;
                Timestamp ts = null;
                Timestamp ts2 =null;
                Utils utils= new Utils();
                Member me=utils.loaduser(getApplicationContext());
                study.setOrg(me.getOrg());
                try {
                    date1=new SimpleDateFormat("dd/MM/yyyy").parse(start);
                    date2= new SimpleDateFormat("dd/MM/yyyy").parse(end);
                    ts= new Timestamp(date1);
                    ts2 = new Timestamp(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (ts==null ||ts2==null){

                }
                else{
                    study.setStart(ts);
                    study.setEnd(ts2);
                }
                db.collection("Study").add(study);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

}

package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateStudy extends AppCompatActivity {
    private EditText TitleInput;
    private EditText LocationInput;
    private EditText MissionInput;
    private TextView StartInput;
    private TextView EndInput;
    private Button Post;
    private TextView Back;
    private FirebaseFirestore db;
    private Button pickStartSateID;
    private Button studyDateEndLabel;
    String start = "";
    String end = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study);
        TitleInput =findViewById(R.id.StudyTittleInput);
        LocationInput=findViewById(R.id.StudyLocationInput);
        MissionInput=findViewById(R.id.StudyMissionInput);
        StartInput=findViewById(R.id.studyDateStart);
        EndInput=findViewById(R.id.studyDateEndLabel);
        Post = findViewById(R.id.StudyPostBtn);
        Back= findViewById(R.id.StudyBack);
        db= FirebaseFirestore.getInstance();
        pickStartSateID = findViewById(R.id.pickstartdateid);
        studyDateEndLabel = findViewById(R.id.pickenddateid);

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
                study.setTitle(TitleInput.getText().toString());
                study.setLocation(LocationInput.getText().toString());
                study.setMission(MissionInput.getText().toString());

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

                if (ts != null && ts2 != null){
                    study.setStart(ts);
                    study.setEnd(ts2);
                }
                if (study.title.equals("")) {
                    Toast.makeText(CreateStudy.this, "Please enter a title", Toast.LENGTH_LONG ).show();
                }
                else if (study.location.equals("")) {
                    Toast.makeText(CreateStudy.this, "Please enter a location", Toast.LENGTH_LONG ).show();
                }
                else if (study.mission.equals("")) {
                    Toast.makeText(CreateStudy.this, "Please enter a mission", Toast.LENGTH_LONG ).show();
                }
                else if (ts == null || ts2 == null) {
                    Toast.makeText(CreateStudy.this, "Please enter the dates of the study", Toast.LENGTH_LONG ).show();
                }
                else if (ts.compareTo(ts2) > 0) {
                    Toast.makeText(CreateStudy.this, "Please enter valid dates", Toast.LENGTH_LONG ).show();
                }
                else {
                    db.collection("Study").add(study);
                    Intent i= new Intent(getApplicationContext(), MainActivity.class);

                    i.putExtra("Study",study);
                    startActivity(i);
                }

            }
        });

        pickStartSateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerStart(v);
            }
        });

        studyDateEndLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerEnd(v);
            }
        });
    }



    public void datePickerStart(View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(day);
//                StartInput.setText(dateTime);
                start=String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                StartInput.setText(start);

            }

        }, year, month, day).show();
    }

    public void datePickerEnd(View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
//                String dateTime = String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(day);
                end=String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                EndInput.setText(end);
            }

        }, year, month, day).show();
    }




}

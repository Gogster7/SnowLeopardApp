package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RegisterSpinner1 extends AppCompatActivity {

    Spinner mCountry;
    Button mSubmit;
    FirebaseFirestore db;
    TextView mBack,mRefresh;
    CollectionReference ref;
    List<Org> Aorg=new ArrayList<>();
    List<String> Countries =new ArrayList<>();
    String Sorg;
    String Scountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_spinner1);

        mCountry=findViewById(R.id.Spinner_Country);

        mBack = findViewById(R.id.BackOrg);
        mRefresh = findViewById(R.id.RefreshSpin1);

        mSubmit = findViewById(R.id.Submit1);

        db= FirebaseFirestore.getInstance();

        ref=db.collection("Organization");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterSpinner0.class));
                finish();
            }
        });
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(getApplicationContext(),RegisterSpinner1.class);
            }
        });
        final Set<String> set = new LinkedHashSet<>();

        final ArrayAdapter<String> dataAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item, Countries);


        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Intent intent = getIntent();
        Sorg =intent.getStringExtra("Orgname");
        //attaching data adapter to spinner
        mCountry.setAdapter(dataAdapter);

        ref.whereEqualTo("orgName",Sorg).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Org org = documentSnapshot.toObject(Org.class);
                        Aorg.add(org);
                    }
                    for (Org org : Aorg){

                        set.add(org.getOrgCountry());
                    }
                    Countries=new ArrayList<>(set);

                    Countries.add(0,"Select Region");
                    dataAdapter.addAll(Countries);
                }
            }

        });



        mCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Country"))
                {
                    // do nothing
                }
                else
                {
                    // selecting a spinner item
                    Scountry = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterSpinner2.class);
                Bundle b = new Bundle();
                b.putString("OrgName", Sorg);
                b.putString("Country",Scountry);
                i.putExtras(b);
                startActivity(i);
            }
        });


    }
}

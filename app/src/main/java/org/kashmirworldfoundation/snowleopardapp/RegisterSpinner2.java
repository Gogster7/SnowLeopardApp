package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RegisterSpinner2 extends AppCompatActivity {
    Spinner mRegion;
    Button mSubmit;
    FirebaseFirestore db;
    TextView mBack;
    CollectionReference ref;
    List<Org> Aorg=new ArrayList<Org>();
    List<String> Regions =new ArrayList<>();
    String Sorg,Scountry,Sregion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_spinner2);
        mRegion=findViewById(R.id.Spinner_Region);

        mBack = findViewById(R.id.backCon);

        mSubmit = findViewById(R.id.submit3);

        db= FirebaseFirestore.getInstance();

        ref=db.collection("Organization");

        final Set<String> set = new LinkedHashSet<>();

        final ArrayAdapter<String> dataAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item, Regions);


        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Intent intent = getIntent();
        Sorg =intent.getStringExtra("OrgName");
        Scountry = intent.getStringExtra("Country");
        //attaching data adapter to spinner
        mRegion.setAdapter(dataAdapter);


        ref.whereEqualTo("orgName",Sorg).whereEqualTo("orgCountry",Scountry).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Org org = documentSnapshot.toObject(Org.class);
                    Aorg.add(org);
                }
                for (Org org : Aorg){

                    set.add(org.getOrgRegion());
                }
                Regions=new ArrayList<>(set);

                Regions.add(0,"Select Region");
                dataAdapter.addAll(Regions);
            }
        });



        mRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sregion = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if data is selected
                if (Sregion == null || Sregion.equals("Select Region")){
                    Toast.makeText(RegisterSpinner2.this, "Need to select a region", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Add data to bundle
                Intent i= new Intent(getApplicationContext(),Register.class);
                Bundle b = new Bundle();
                b.putString("OrgName",Sorg);
                b.putString("Country", Scountry);
                b.putString("Region",Sregion);
                i.putExtras(b);
                startActivity(i);
            }
        });


    }
}


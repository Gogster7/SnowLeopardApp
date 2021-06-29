package org.kashmirworldfoundation.snowleopardapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

public class RegisterSpinner0 extends AppCompatActivity {

    Spinner mOrg;
    Button mSubmit;
    FirebaseFirestore db;
    TextView mRefresh,mBack;
    CollectionReference ref;

    List<Org> Aorg=new ArrayList<>();
    List<String> orgs =new ArrayList<>();

    String Sorg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_spinner0);

        mOrg = findViewById(R.id.register_orgS3);

        mRefresh = findViewById(R.id.RefreshSpin0);
        mBack =findViewById(R.id.GoBackLogin);

        mSubmit = findViewById(R.id.submit0);

        db= FirebaseFirestore.getInstance();

        ref=db.collection("Organization");

        final Set<String> set = new LinkedHashSet<>();

        final ArrayAdapter<String> dataAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item, orgs);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        mOrg.setAdapter(dataAdapter);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the data is selected
                if (Sorg == null || Sorg.equals("Select Org")){
                    Toast.makeText(RegisterSpinner0.this, "Need to select an organization", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Add your data to bundle
                Intent I = new Intent(getApplicationContext(), RegisterSpinner1.class);
                Bundle bundle = new Bundle();
                bundle.putString("Orgname", Sorg);
                I.putExtras(bundle);
                startActivity(I);
            }
        });
        ref.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null) {
                    Toast.makeText(RegisterSpinner0.this, "Error " + e.getMessage(), Toast.LENGTH_LONG);

                }
                else{
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Org org = documentSnapshot.toObject(Org.class);

                        Aorg.add(org);
                    }

                    for (Org org : Aorg){

                        set.add(org.getOrgName());
                    }
                    orgs=new ArrayList<>(set);

                    orgs.add(0,"Select Org");
                    dataAdapter.addAll(orgs);

                }
            }

        });


        mOrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sorg = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

}

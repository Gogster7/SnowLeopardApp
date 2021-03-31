package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegisterOrg extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText mEmail, mOrgname, mPhone, mOrgWebsite, mRegion;
    Button mbRegister;
    TextView mbLogin;
    FirebaseAuth fAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_org);
        mOrgname = findViewById(R.id.OrgName);
        mPhone = findViewById(R.id.OrgPhone);
        mOrgWebsite = findViewById(R.id.orgwebsite);
        mEmail = findViewById(R.id.OrgEmail);

        mbLogin = findViewById(R.id.logindr);
        mbRegister = findViewById(R.id.RegisterB);
        mRegion = findViewById(R.id.Region);
        fAuth = FirebaseAuth.getInstance();

        final Spinner spinner = findViewById(R.id.countries);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this,R.array.Countries,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });

        mbRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Orgname = mOrgname.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String website = mOrgWebsite.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String country =spinner.getSelectedItem().toString().trim();
                String region = mRegion.getText().toString().trim();
                if(TextUtils.isEmpty(Orgname)){
                    mOrgname.setError("Orgname Required");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email Required.");
                    return;
                }

                if (TextUtils.isEmpty(website)){
                    mOrgWebsite.setError(("Website Required"));
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    mOrgWebsite.setError(("Phone Number Required"));
                    return;
                }

                if (country.equals("Country")){
                    Toast.makeText(RegisterOrg.this, "Need to select a country", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(region)){
                    mRegion.setError("Region Required");
                    return;
                }

                db=FirebaseFirestore.getInstance();

                final Org morg = new Org();
                morg.setOrgCountry(country);
                morg.setOrgRegion(region);
                morg.setOrgName(Orgname);
                morg.setOrgWebsite(website);
                morg.setOrgPhone(phone);
                morg.setOrgEmail(email);



                final Intent i = new Intent(getApplicationContext(), Register_Org_Admin.class);
//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("Orgname", Orgname);
                bundle.putString("Country", country);
                bundle.putString("Region", region);

//Add the bundle to the intent
                i.putExtras(bundle);

                db.collection("Organization").whereEqualTo("orgName",Orgname).whereEqualTo("orgCountry",country).whereEqualTo("orgRegion",region).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                                if (task.getResult().isEmpty()){

                                    db.collection("Organization").add(morg).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterOrg.this,"Organization Sucesfully Created",Toast.LENGTH_LONG).show();
                                                Log.e("Tag", "Sucess 1");
                                                saveAdmin();
                                                sendMessage(Orgname,phone,website,country,region);
                                                startActivity(i);
                                            }
                                            else{
                                                Toast.makeText(RegisterOrg.this,"Error submitting Organization",Toast.LENGTH_LONG).show();
                                                Log.e("Tag", "Fail 1");
                                                recreate();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(RegisterOrg.this,"Error Organization already registered",Toast.LENGTH_LONG).show();
                                    Log.e("Tag","Fail2");
                                    recreate();
                                }
                        }
                        else{
                            Toast.makeText(RegisterOrg.this,"Error checking for duplicate Organizations",Toast.LENGTH_LONG).show();
                        }
                    }
                });



//Fire that second activity




            }






        });





    }
    public String capitalizeFirstLetter(String string) {
        string =string.toLowerCase();
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void sendMessage(final String orgname, final String phone, final String website, final String country,final String region) {
        final ProgressDialog dialog = new ProgressDialog(RegisterOrg.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            String Body="Orgname = " +orgname +" \n phone = " + phone+ "\n website =" + website + "\nCountry = " + country + "\nRegion = " + region;
            String subject = orgname + " wants to register under KWF app ";

            public void run() {
                try {
                    //"aliyah@kashmirworldfoundation.org"
                    GMailSender sender = new GMailSender("adm1nkwf1675@gmail.com", "Chowder1675!");
                    sender.sendMail(subject,
                            Body,
                            "adm1nkwf1675@gmail.com","aliyah@kashmirworldfoundation.org"
                            );
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    private void saveAdmin(){
        SharedPreferences sharedPreferences = RegisterOrg.this.getSharedPreferences("Admin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();


        Gson gson = new Gson();

        editor.putBoolean("Admin",true);
        editor.apply();
    }
}


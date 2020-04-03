package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterOrg extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText mEmail, mPassword, mOrgname, mPhone, mOrgWebsite, mRegion;
    Button mbRegister;
    TextView mbLogin;
    FirebaseAuth fAuth;
    FirebaseDatabase fData;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_org);
        mOrgname = findViewById(R.id.Orgname);
        mPhone = findViewById(R.id.Orgphone);
        mOrgWebsite = findViewById(R.id.orgwebsite);
        mEmail = findViewById(R.id.orgEmail);
        mPassword = findViewById(R.id.opassword);
        mbLogin = findViewById(R.id.logindr);
        mbRegister = findViewById(R.id.registerB);
        mRegion = findViewById(R.id.region);
        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        final Spinner spinner = findViewById(R.id.countries);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this,R.array.Countries,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mbRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Orgname = mOrgname.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String website = mOrgWebsite.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String country =spinner.getSelectedItem().toString().trim();
                String region = mRegion.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if (country.equals("Country")){
                    Toast.makeText(RegisterOrg.this, "Need to select a country", Toast.LENGTH_SHORT).show();
                }



                if (password.length()<8){
                    mPassword.setError("Password needs to be at least 8 chars.");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterOrg.this,"User Created", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterOrg.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                reff = fData.getReference().child("NewOrganization");
                Org Myorg = new Org();
                Myorg.setOrgEmail(email);
                Myorg.setMembers();
                Myorg.setOrgname(Orgname);
                Myorg.setOrgphone(phone);
                Myorg.setOrgWebsite(website);
                reff.push().setValue(Myorg);

            }
        });

        mbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void sendafmail(String Orgname,
                           String email,
                           String website,
                           String phone,
                           String country,
                           String region) throws Exception {
        String Subject = Orgname + "requesting approval";
        String From = "Adm1n1675@gmail.com";
        String Password = "Chowder1675!";
        String to = "matthewduypham0303@gmail.com";
        String Body= "This" + Orgname + "requesting approval \n Orgmail:" + email;
        Body = Body +"\nPhone number:" + phone + "\nWebsite:" + website + "\nCountry:" + country;
        Body = Body + "\nRegion:" + region + "\n \n \n \n";


    }

}

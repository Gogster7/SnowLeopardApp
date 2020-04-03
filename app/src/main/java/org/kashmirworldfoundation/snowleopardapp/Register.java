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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    private Spinner spinner;

    EditText mFullName, mJobTitle, mOrganization, mEmail, mPassword, mReEnter, mPhonenumber;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    Spinner mCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName         = findViewById(R.id.fullName);
        mJobTitle         = findViewById(R.id.jobTitle);
        mEmail            = findViewById(R.id.email);
        mOrganization     = findViewById(R.id.organization);
        mPhonenumber      = findViewById(R.id.phone);
        mPassword         = findViewById(R.id.password);
        mReEnter          = findViewById(R.id.reEnter);
        mRegisterBtn      = findViewById(R.id.registrationBtn);
        mLoginBtn         = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);

        spinner     = findViewById(R.id.counrty);

        List<String> categories = new ArrayList<>();
        categories.add(0, "Choose Country");
        categories.add("Afghanistan");
        categories.add("Bhutan");
        categories.add("China");
        categories.add("India");
        categories.add("Kashmir");
        categories.add("Kazakhstan");
        categories.add("Kyrgyzstan");
        categories.add("Mongolia");
        categories.add("Nepal");
        categories.add("Pakistan");
        categories.add("Russia");
        categories.add("Tajikistan");
        categories.add("Uzbekistan");

        //Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, categories);

        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Country"))
                {
                    // do nothing
                }
                else
                {
                    // selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();

                    //show selected spinner item
                    Toast.makeText(parent.getContext(), "Country: " +item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });




        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), Login.class));
             }
         });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String reEnter = mReEnter.getText().toString().trim();
                String fullName = mFullName.getText().toString().trim();
                String organization = mOrganization.getText().toString().trim();
                String phoneNumber = mPhonenumber.getText().toString().trim();

                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Full name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(organization)){
                    mOrganization.setError("Organization is Required.");
                    return;
                }
                if(TextUtils.isEmpty(phoneNumber)){
                    mPhonenumber.setError("Phone number is Required.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password must be at least 6 Characters");
                    return;
                }
                if(!password.equals(reEnter)){
                    mReEnter.setError("Passwords must Match");
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            

                        }else{
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}

package org.kashmirworldfoundation.snowleopardapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {


    Org Forg;
    EditText mFullName, mJobTitle, mOrganization, mEmail, mPassword, mReEnter, mPhonenumber,mRegion;
    Button mRegisterBtn;
    TextView mLoginBtn,mRegisterOrgB;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;
    Boolean mUser=false;
    StorageReference fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName         = findViewById(R.id.fullName);
        mJobTitle         = findViewById(R.id.jobTitle);
        mEmail            = findViewById(R.id.email);
        mPhonenumber      = findViewById(R.id.phone);
        mPassword         = findViewById(R.id.password);
        mReEnter          = findViewById(R.id.reEnter);
        mRegisterBtn      = findViewById(R.id.registrationBtn);
        mLoginBtn         = findViewById(R.id.createText);
        mRegisterOrgB = findViewById(R.id.RegisterOrgB);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);
        fStore = FirebaseStorage.getInstance().getReference();






        mRegisterOrgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterOrg.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=getIntent();
                db = FirebaseFirestore.getInstance();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String reEnter = mReEnter.getText().toString().trim();
                final String fullName = mFullName.getText().toString().trim();
                final String organization = i.getStringExtra("OrgName");
                final String phoneNumber = mPhonenumber.getText().toString().trim();
                final String job = mJobTitle.getText().toString().trim();
                final String region =i.getStringExtra("Region");
                final String country = i.getStringExtra("Country");
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
                final Member mem =new Member();
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            db.collection("Organization").whereEqualTo("orgName",organization).
                                    whereEqualTo("orgCountry", country).whereEqualTo("orgRegion",region).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                            mem.setOrg(documentSnapshot.getReference().getPath());
                                            Org org = documentSnapshot.toObject(Org.class);
                                            Forg = org;

                                        }
                                        mem.setAdmin(Boolean.FALSE);
                                        mem.setEmail(email);
                                        mem.setFullname(fullName);
                                        mem.setJob(job);
                                        mem.setPhone(phoneNumber);
                                        mem.setProfile("profile/kwflogo.jpg");
                                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                        db.collection("Member").document(user.getUid()).set(mem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                sendMessage(fullName,phoneNumber,email,job,Forg.getOrgEmail());
                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                            }


                                        });

                                    }


                                }
                            });
                        }
                    }
                });





            }
        });

    }
    private void sendMessage(final String name, final String phone, final String email, final String job,final String receive) {
        final ProgressDialog dialog = new ProgressDialog(Register.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            String Body="name = " +name +" \n phone = " + phone+ "\n email =" + email + "\njob = " + job;
            String subject = name + " wants to join your organization";
            public void run() {
                try {
                    GMailSender sender = new GMailSender("adm1nkwf1675@gmail.com", "Chowder1675!");
                    sender.sendMail(subject,
                            Body,
                            "adm1nkwf1675@gmail.com",
                            receive);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
}
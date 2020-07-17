package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


public class RegisterOrgAdmin extends AppCompatActivity {
    EditText mEmail, mPassword, mPhone, mJob,mName;
    Button mbRegisterA;
    FirebaseAuth fAuth2;
    FirebaseFirestore db2;
    FirebaseStorage St2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_org_admin);
        mbRegisterA = findViewById(R.id.AdminRegisterB);
        mEmail = findViewById(R.id.AdminEmail);
        mPassword = findViewById(R.id.AdminPassword);
        mPhone = findViewById(R.id.AdminPhone);
        mJob = findViewById(R.id.AdminJob);
        mName =findViewById(R.id.AdminName);
        fAuth2 = FirebaseAuth.getInstance();
        mbRegisterA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                St2 = FirebaseStorage.getInstance();
                db2  = FirebaseFirestore.getInstance();
                final String Aname = mName.getText().toString().trim();
                final String Aemail = mEmail.getText().toString().trim();
                final String Apassword = mPassword.getText().toString().trim();
                final String Ajob = mJob.getText().toString().trim();
                final String Aphone = mPhone.getText().toString().trim();

                Intent intent = getIntent();
                final String Orgname =intent.getStringExtra("Orgname");
                final String Region = intent.getStringExtra("Region");
                final String Country = intent.getStringExtra("Country");
                //Orgname= capitalizeFirstLetter(Orgname);
                //Region = capitalizeFirstLetter(Region);
                //Country = capitalizeFirstLetter(Country);
                final Member memA =new Member();
                fAuth2.createUserWithEmailAndPassword(Aemail,Apassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            db2.collection("Organization").whereEqualTo("orgName",Orgname).whereEqualTo("orgCountry",Country).
                                    whereEqualTo("orgRegion",Region).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                            Toast.makeText(RegisterOrgAdmin.this,"Org Found", Toast.LENGTH_SHORT).show();
                                            memA.setOrg(documentSnapshot.getReference().getPath());

                                        }
                                        memA.setJob(Ajob);
                                        memA.setFullname(Aname);
                                        memA.setPhone(Aphone);
                                        memA.setAdmin(Boolean.TRUE);
                                        memA.setEmail(Aemail);
                                        memA.setProfile("profile/kwflogo.jpg");
                                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                        db2.collection("Member").document(user.getUid()).set(memA).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(RegisterOrgAdmin.this,"User Created", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }
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
    public String capitalizeFirstLetter(String string) {
        string =string.toLowerCase();
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }
}

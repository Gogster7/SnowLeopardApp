package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn,mRegisterOrgBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore Fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth= FirebaseAuth.getInstance();
        Fstore =FirebaseFirestore.getInstance();
        /*
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

         */
        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password);
        mLoginBtn   = findViewById(R.id.LoginBtn);
        mRegisterBtn = findViewById(R.id.Register0);
        mRegisterOrgBtn = findViewById(R.id.RegisterOrgBtn);
        fAuth       = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterSpinner0.class));
                /*
                Intent i = new Intent(getApplicationContext(),RegisterOrgAdmin.class);
                Bundle b= new Bundle();
                b.putString("Orgname", "org2");
                b.putString("Country", "China");
                b.putString("Region", "N/A");
                i.putExtras(b);
                startActivity(i);

                 */
            }
        });
        mRegisterOrgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterOrg.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= mEmail.getText().toString().trim();
                String pass =mPassword.getText().toString().trim();
                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid=fAuth.getUid();
                            Toast.makeText(Login.this,"Welcome", Toast.LENGTH_LONG).show();
                            Fstore.collection("Member").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Member me=task.getResult().toObject(Member.class);
                                        save(me, uid);
                                    }
                                }
                            });


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG ).show();
                        }

                    }
                });
            }
        });


    }
    private void save(Member member,String uid){
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json =gson.toJson(member);

        editor.putString("user",json);
        editor.putString("uid",uid);
        editor.apply();
    }
}


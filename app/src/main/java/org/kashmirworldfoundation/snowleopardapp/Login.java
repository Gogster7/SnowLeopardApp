package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn,mRegisterOrgBtn, mForgetBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    ImageView Background;
    ConstraintLayout layout;
    ArrayList<String> studies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth= FirebaseAuth.getInstance();
        layout = findViewById(R.id.LoginLayout);
        /*
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

         */
        mForgetBtn = findViewById(R.id.LoginForget);
        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password);
        mLoginBtn   = findViewById(R.id.LoginBtn);
        mRegisterBtn = findViewById(R.id.Register0);
        mRegisterOrgBtn = findViewById(R.id.RegisterOrgBtn);
        fAuth       = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        Background =findViewById(R.id.BackgroundLogin);
        Background.setVisibility(View.VISIBLE);
        fetchData();

        mForgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterSpinner0.class));
               
            }
        });
        mRegisterOrgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAdmin()){
                    startActivity(new Intent(getApplicationContext(),Register_Org_Admin.class));
                }

                startActivity(new Intent(getApplicationContext(), RegisterOrg.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils util = new Utils();

                studies=new ArrayList<>();
                String email= mEmail.getText().toString().trim();
                String pass =mPassword.getText().toString().trim();
                if (util.getAgreement(Login.this)){
                    LayoutInflater inflater= LayoutInflater.from(Login.this);
                    View view=inflater.inflate(R.layout.disclaimer_layout, null);


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
                    alertDialog.setTitle("Terms of Service");
                    alertDialog.setView(view);
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Login.this,"Agreement needed to login",Toast.LENGTH_LONG).show();
                        }

                    });

                    alertDialog.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            util.setAgreement(Login.this);
                            login(email,pass);
                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
                else {
                    login(email,pass);
                }

            }

        });

    }
    private void fetchData() {
        StorageReference ref = FirebaseStorage.getInstance().getReference("assets/Start.png");
        GlideApp.with(this)
                .load(ref)
                .into(Background);


    }
    private void saveStudies(ArrayList<String> studies){
        SharedPreferences sharedPreferences = Login.this.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();


        Gson gson = new Gson();
        String json =gson.toJson(studies);
        editor.putString("studies",json);
        editor.apply();
    }
    private void saveMember (Member mem,String uid){
        SharedPreferences sharedPreferences = Login.this.getSharedPreferences("user", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Gson gson = new Gson();
        String json =gson.toJson(mem);
        editor.putString("user",json);
        editor.putString("uid",uid);
        editor.apply();
    }
    private boolean getAdmin(){
        SharedPreferences sharedPreferences = Login.this.getSharedPreferences("Admin", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("Admin",false);

    }
    private void login(String email, String pass){
        fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    fStore.document("Member/"+fAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                Member me=task.getResult().toObject(Member.class);
                                saveMember(me,fAuth.getUid());
                                studies.add("Pick A Study");

                                fStore.collection("Study").whereEqualTo("org",me.getOrg()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                                Study study = documentSnapshot.toObject(Study.class);
                                                studies.add(study.getTittle());
                                            }
                                            if (studies.size()==1){
                                                studies.set(0,"No Studies");
                                            }
                                            saveStudies(studies);
                                                    /*
                                                    Intent i= new Intent(getApplicationContext(),MainActivity.class);
                                                    Bundle b = new Bundle();
                                                    b.putString("Studies",studies.toString());

                                                    i.putExtras(b);
                                                    Toast.makeText(Login.this,"Welcome", Toast.LENGTH_LONG).show();
                                                    startActivity(i);

                                                    */
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));


                                        }
                                        else{
                                            studies.set(0, "No Studies");
                                            saveStudies(studies);
                                                    /*
                                                    Intent i= new Intent(getApplicationContext(),MainActivity.class);
                                                    Bundle b = new Bundle();
                                                    b.putString("Studies",studies.toString());

                                                    i.putExtras(b);
                                                    Toast.makeText(Login.this,"Welcome", Toast.LENGTH_LONG).show();
                                                    startActivity(i);

                                                     */
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                        }
                                    }
                                });

                            }
                        }
                    });

                }
                else{

                    Toast.makeText(Login.this, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG ).show();

                }

            }
        });
    }
}

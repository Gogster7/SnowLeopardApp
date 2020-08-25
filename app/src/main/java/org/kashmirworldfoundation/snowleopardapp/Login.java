package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn,mRegisterOrgBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    ImageView Background;
    ConstraintLayout layout;
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



        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterSpinner0.class));
               
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
                            fStore.document("Member/"+fAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){

                                        saveMember(task.getResult().toObject(Member.class),fAuth.getUid());
                                        Toast.makeText(Login.this,"Welcome", Toast.LENGTH_LONG).show();


                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
        });

    }
    private void fetchData() {
        StorageReference ref = FirebaseStorage.getInstance().getReference("assets/Start.png");
        GlideApp.with(this)
                .load(ref)
                .into(Background);


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

}

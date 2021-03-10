package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Add_Prey extends AppCompatActivity {
    private EditText PreyInput;
    private EditText LongitudeInput;
    private EditText LattitudeInput;
    private EditText NoteInput;
    private ImageView img;
    private Button Post;
    private TextView Back;
    private FirebaseFirestore db;
    private FirebaseStorage fStorage;
    private float[] latlong=new float[2];
    Uri uri;
    Boolean pic=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__prey);
        PreyInput=findViewById(R.id.PreyTittleInput);
        LongitudeInput=findViewById(R.id.PreyLongitudeInput);
        LattitudeInput=findViewById(R.id.PreyLattitudeInput);
        NoteInput=findViewById(R.id.PreyNoteInput);
        img=findViewById(R.id.PreyImg);
        Post=findViewById(R.id.PreyAddBtn);
        Back=findViewById(R.id.PreyBack);
        db=FirebaseFirestore.getInstance();
        fStorage= FirebaseStorage.getInstance();
        begin();
        uri=null;
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        //remember to check empty
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prey prey= new Prey();
                CollectionReference collection =db.collection("Prey");
                DocumentReference doc = collection.document();
                String path=doc.getId();
                Date currentTime = Calendar.getInstance().getTime();
                postpic(path);
                prey.setPrey(PreyInput.getText().toString());
                prey.setLatitudeS(LattitudeInput.getText().toString());
                prey.setLongitudeS(LongitudeInput.getText().toString());
                prey.setNote(NoteInput.getText().toString());
                prey.setPosted(new Timestamp(currentTime));
                prey.setPic(uri.toString());

                Utils utils= new Utils();
                Member member=utils.loaduser(getApplicationContext());
                prey.setMember("Member/"+utils.loadUid(getApplicationContext()));

                prey.setOrg(member.getOrg());
                prey.setAuthor(member.getFullname());
                utils.saveprey(prey,getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(0);
            }
        });


    }
    private void openGallery(int PICK_IMAGE){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestcode,int resultcode, @Nullable Intent data){
        super.onActivityResult(requestcode,resultcode,data);

        if (resultcode== Activity.RESULT_OK ){
            uri =data.getData();
            //img.setImageURI(uri);

            img.setDrawingCacheEnabled(true);
            img.buildDrawingCache();
            try {
                ExifInterface exif=new ExifInterface(new File(uri.getPath()));

                exif.getLatLong(latlong);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String temp;
            if(latlong[0]!=0){
                temp=String.valueOf(latlong[0]);
                LattitudeInput.setText(temp);
            }
            if(latlong[1]!=0){
                temp=String.valueOf(latlong[1]);
                LongitudeInput.setText(temp);
            }
            temp=String.valueOf(latlong[0]);
            LattitudeInput.setText(temp);
            temp=String.valueOf(latlong[1]);
            LongitudeInput.setText(temp);
            end();


        }


    }
    public void postpic(String path){
        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datan = baos.toByteArray();
            StorageReference profile = fStorage.getReference(path + "/image");
            UploadTask uploadTask = profile.putBytes(datan);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void begin(){
        PreyInput.setVisibility(View.GONE);
        LongitudeInput.setVisibility(View.GONE);
        LattitudeInput.setVisibility(View.GONE);
        LongitudeInput.setText("");
        LattitudeInput.setText("");
        NoteInput.setVisibility(View.GONE);
        Post.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        findViewById(R.id.PreyTittleLabel).setVisibility(View.GONE);
        findViewById(R.id.PreyLattitudeLabel).setVisibility(View.GONE);
        findViewById(R.id.PreyLongitudeLabel).setVisibility(View.GONE);
        findViewById(R.id.PreyNoteLabel).setVisibility(View.GONE);
        pic=true;
        latlong[0]=0;
        latlong[1]=0;

    }
    private void end(){
        PreyInput.setVisibility(View.VISIBLE);
        LongitudeInput.setVisibility(View.VISIBLE);
        LattitudeInput.setVisibility(View.VISIBLE);
        NoteInput.setVisibility(View.VISIBLE);
        Post.setVisibility(View.VISIBLE);
        img.setVisibility(View.GONE);
        findViewById(R.id.PreyTittleLabel).setVisibility(View.VISIBLE);
        findViewById(R.id.PreyLattitudeLabel).setVisibility(View.VISIBLE);
        findViewById(R.id.PreyLongitudeLabel).setVisibility(View.VISIBLE);
        findViewById(R.id.PreyNoteLabel).setVisibility(View.VISIBLE);
        pic=false;
        String Slat=LattitudeInput.getText().toString();
        String Slong=LongitudeInput.getText().toString();

        if(Slat=="0"&& Slong=="0"){
            Toast.makeText(getApplicationContext(),"No GPS location in image",Toast.LENGTH_LONG);
        }

    }
    @Override
    public void onBackPressed() {
      if (pic){
          super.onBackPressed();
      }
      else{
          begin();
      }
    }

}

package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.kashmirworldfoundation.snowleopardapp.GlideApp;
import org.kashmirworldfoundation.snowleopardapp.Login;
import org.kashmirworldfoundation.snowleopardapp.Member;
import org.kashmirworldfoundation.snowleopardapp.R;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private static final int PICK_IMAGE = 100;
    private TextView Pname,Pphone,Padmin,Pemail;
    private View fragmentView ;
    private ImageView Ppic;
    private Button logout;
    private FirebaseFirestore Fstore;
    private FirebaseAuth Fauth;
    private FirebaseStorage Fstorage;
    private ImageButton Mupload;
    private Uri imageurl;
    private String Uid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fstorage= FirebaseStorage.getInstance();
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        Fstore=FirebaseFirestore.getInstance();
        Fauth=FirebaseAuth.getInstance();
        Pname=(TextView)fragmentView.findViewById(R.id.NameP);
        Pphone=(TextView)fragmentView.findViewById(R.id.PhoneP);
        Padmin =(TextView)fragmentView.findViewById(R.id.AdminP);
        Pemail =(TextView)fragmentView.findViewById(R.id.EmailP);
        Ppic = (ImageView) fragmentView.findViewById(R.id.ProfilePic);
        Mupload= (ImageButton) fragmentView.findViewById(R.id.Pupload);
        Uid=Fauth.getCurrentUser().getUid();
        Member me = loaduser();

        Fstore.collection("Member").document(Uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Member user=documentSnapshot.toObject(Member.class);
                Pname.setText("Name: " + user.getFullname());
                Pphone.setText("Phone: " + user.getPhone());
                Pemail.setText("Email: " + user.getEmail());
                if (user.getAdmin()){
                    Padmin.setText("Admin: True");
                }
                else{
                    Padmin.setVisibility(View.INVISIBLE);
                    //Padmin.setText("Admin: False");
                }
                fetchData(user.getProfile(),Ppic);

            }
        });
        //fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = (Button) fragmentView.findViewById(R.id.logoutP);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                clear();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
        Mupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        return fragmentView;
    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onClick(View v) {
        Button logout = (Button) fragmentView.findViewById(R.id.logoutP);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth fauth= FirebaseAuth.getInstance();
                fauth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
    }
    private void fetchData(String location, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference(location);

        GlideApp.with(this)
                .load(ref);
                //.into(image);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK && requestCode ==PICK_IMAGE){
            imageurl =data.getData();
            Ppic.setImageURI(imageurl);
            Ppic.setDrawingCacheEnabled(true);
            Ppic.buildDrawingCache();

            Bitmap bitmap = ((BitmapDrawable) Ppic.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datan = baos.toByteArray();
            StorageReference profile= Fstorage.getReference("profile/" +Uid);
            UploadTask uploadTask = profile.putBytes(datan);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Fstore.collection("Member").document(Uid).update("profile","profile"+Uid);
                    }
                    else{
                        createToast(getContext(),"Error adding profile please try again with a stable connection",Toast.LENGTH_LONG);
                    }
                }
            });


        }
    }
    public static void createToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, "" + message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(Color.parseColor("#008577"));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(140, 75, 140, 75);
        tv.setTextColor(0xFFFFFFFF);
        toast.show();
    }
    private Member loaduser(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson= new Gson();
        String json = sharedPreferences.getString("user", null);
        Type type =new TypeToken<Member>(){}.getType();
        return gson.fromJson(json,type);
    }
    private void clear(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.clear();

    }
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
}

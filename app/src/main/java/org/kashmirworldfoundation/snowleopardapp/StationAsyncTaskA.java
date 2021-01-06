package org.kashmirworldfoundation.snowleopardapp;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.kashmirworldfoundation.snowleopardapp.Fragment.ListFragment;

import java.util.ArrayList;

public class StationAsyncTaskA extends AsyncTask<String, Void, String> {
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseAuth FireAuth;

    private ArrayList<CameraStation> CStations= new ArrayList<>();
    private Member mem;
    private String Org;
    private static final String TAG = "StationAsyncTask";
    private int count;
    private int size;

    private Station_List listFragment;
    private String study;
    StationAsyncTaskA(Station_List li,String stud){listFragment=li; study=stud;}

    protected void update(){
        listFragment.updateStationList(CStations);
        listFragment.updateList();
    }



    protected String doInBackground(String... strings) {

        // Add data from Firebase on the the Arrays
        try {
            FireAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            collectionReference = firebaseFirestore.collection("CameraStation");
        }
        catch (Exception e){
            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        String string= "";

        firebaseFirestore.collection("Member").document(FireAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    mem=task.getResult().toObject(Member.class);
                    collectionReference.whereEqualTo("org",mem.getOrg()).whereEqualTo("study",study).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                size = task.getResult().size();
                                Log.e("size", ""+size);
                                for (DocumentSnapshot objectDocumentSnapshot: task.getResult()){
                                    CameraStation stat = objectDocumentSnapshot.toObject(CameraStation.class);
                                    CStations.add(stat);
                                    count++;
                                    if(count==size){
                                        update();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });




        return null;
    }
}

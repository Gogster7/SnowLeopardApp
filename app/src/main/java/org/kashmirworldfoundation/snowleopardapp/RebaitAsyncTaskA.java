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

import java.util.ArrayList;

public class RebaitAsyncTaskA extends AsyncTask<String,Void,String> {
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;


    private ArrayList<Rebait> CRebait= new ArrayList<>();

    private static final String TAG = "StationAsyncTask";
    private int count;
    private int size;

    private Rebait_List expand;

    RebaitAsyncTaskA(Rebait_List ex){expand=ex; }

    protected void update(){
        expand.updateStationList(CRebait);
        expand.updateList();
    }



    protected String doInBackground(String... strings) {

        // Add data from Firebase on the the Arrays
        try {

            firebaseFirestore = FirebaseFirestore.getInstance();
            collectionReference = firebaseFirestore.document(expand.pathRecord).collection("Rebate Log");
            String string= "";


        }
        catch (Exception e){
            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    size=task.getResult().size();
                    Log.e("size", ""+size);
                    for (DocumentSnapshot objectDocumentSnapshot: task.getResult()){
                        Rebait reb = objectDocumentSnapshot.toObject(Rebait.class);
                        CRebait.add(reb);
                        count++;
                        if(count==size){
                            update();
                        }
                    }
                }
            }
        });




        return null;
    }
}

package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.kashmirworldfoundation.snowleopardapp.Fragment.ListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class StationAsyncTask extends AsyncTask<String, Void, String> {

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private ArrayList<String> mTitle = new ArrayList<String>();
    private ArrayList<String> mDate = new ArrayList<String>();
    private static final String TAG = "StationAsyncTask";
    private int count;
    private int size;

    private ListFragment listFragment;

    StationAsyncTask(ListFragment li){listFragment=li;}

    protected void update(){
        listFragment.updateTitle(mTitle);
        listFragment.updateDate(mDate);
        listFragment.updateList();
    }


    @Override
    protected String doInBackground(String... strings) {

        // Add data from Firebase on the the Arrays
        try {
            firebaseFirestore = FirebaseFirestore.getInstance();
            collectionReference = firebaseFirestore.collection("CameraStation");
        }
        catch (Exception e){
            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    size=queryDocumentSnapshots.size();
                    for (DocumentSnapshot objectDocumentSnapshot:queryDocumentSnapshots)
                    {
                        String stationId = objectDocumentSnapshot.getString("StationId");
                        String date = objectDocumentSnapshot.getString("SDate");
                        mTitle.add(stationId);
                        mDate.add(date);
                        count++;
                        if(count==size){
                            update();
                        }
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  Toast.makeText(getActivity(),"Fails to retrieve Camera Station", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        catch (Exception e){
            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return null;
    }
}

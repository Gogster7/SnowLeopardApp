package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.kashmirworldfoundation.snowleopardapp.Fragment.ListFragment;
import org.kashmirworldfoundation.snowleopardapp.Station;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StationAsyncTask extends AsyncTask<String, Void, String> {

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private ArrayList<Station> stations=new ArrayList<>();


    private static final String TAG = "StationAsyncTask";
    private int count;
    private int size;

    private ListFragment listFragment;

    StationAsyncTask(ListFragment li){listFragment=li;}

    protected void update(){
        listFragment.updateStationList(stations);
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
                        Station newStation=new Station();

                        newStation.setStationId(objectDocumentSnapshot.getString("StationId"));
                        newStation.setAltitude(objectDocumentSnapshot.getString("altitude"));
                        newStation.setAuthor(objectDocumentSnapshot.getString("author"));
                        newStation.setCameraId(objectDocumentSnapshot.getString("cameraId"));
                        newStation.setCountry(objectDocumentSnapshot.getString("country"));
                        newStation.setHabitat(objectDocumentSnapshot.getString("habitat"));
//                        newStation.setIDate(objectDocumentSnapshot.getString("IDate"));

                        newStation.setLattitudeS(objectDocumentSnapshot.getString("lattitudeS"));
                        newStation.setLongitudeS(objectDocumentSnapshot.getString("longitudeS"));

                        newStation.setLureType(objectDocumentSnapshot.getString("lureType"));
                        newStation.setOrg(objectDocumentSnapshot.getString("org"));
                        newStation.setPic(objectDocumentSnapshot.getString("pic"));
                        newStation.setAuthor(objectDocumentSnapshot.getString("author"));
                        Timestamp s =objectDocumentSnapshot.getTimestamp("posted");


                        if(s!=null){
                            newStation.setPosted(s.toDate().toString());
                        }

                        newStation.setPotential(objectDocumentSnapshot.getString("potential"));

                        newStation.setRegion(objectDocumentSnapshot.getString("region"));

                        newStation.setSubstrate(objectDocumentSnapshot.getString("substrate"));
                        newStation.setTerrain(objectDocumentSnapshot.getString("terrain"));
                        newStation.setWatershedid(objectDocumentSnapshot.getString("watershedid"));

                        //this part is for getting data from firebase's different name
                        //station id
                        String stationId = objectDocumentSnapshot.getString("StationId");
                        if(stationId==null){
                            stationId=objectDocumentSnapshot.getString("stationId");
                        }
                        newStation.setStationId(stationId);

                        //date
                        String date = objectDocumentSnapshot.getString("SDate");
                        if(date==null){
                            date = objectDocumentSnapshot.getTimestamp("posted").toDate().toString();
                        }
                        newStation.setPosted(date);
                        stations.add(newStation);

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

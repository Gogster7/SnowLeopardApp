package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.kashmirworldfoundation.snowleopardapp.DeleteAsyncTask;
import org.kashmirworldfoundation.snowleopardapp.Member;
import org.kashmirworldfoundation.snowleopardapp.Study;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StationAsyncTask extends AsyncTask<String, Void, String> {

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseAuth FireAuth;

    private ArrayList<Study> CStations= new ArrayList<>();
    private Member mem;
    private String Org;
    private static final String TAG = "StationAsyncTask";
    private int count;
    private int size;

    private ListFragment listFragment;

    StationAsyncTask(ListFragment li){listFragment=li;}

    protected void update(){
        listFragment.updateStationList(CStations);
        listFragment.updateList();
    }


    @Override
    protected String doInBackground(String... strings) {

        // Add data from Firebase on the the Arrays
        try {
            FireAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            collectionReference = firebaseFirestore.collection("Study");
        }
        catch (Exception e){
            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Calendar c= Calendar.getInstance();
        Date currentTime = c.getTime();

        c.add(Calendar.DATE,7);
        Date prevTime=c.getTime();
        final Date[] end1 = new Date[1];
        final ArrayList<Pair<String,String> >paths=new ArrayList();
        final ArrayList<String> stations=new ArrayList<>();
        firebaseFirestore.collection("Member").document(FireAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    mem=task.getResult().toObject(Member.class);
                    collectionReference.whereEqualTo("org",mem.getOrg()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                size = task.getResult().size();
                                for (DocumentSnapshot objectDocumentSnapshot: task.getResult()) {
                                    Study stat = objectDocumentSnapshot.toObject(Study.class);
                                    if (stat != null && stat.getEnd() != null) {
                                        end1[0] = stat.getEnd().toDate();
                                        if (end1[0].compareTo(prevTime) < 0) {
                                            stations.add(stat.getTitle());
                                        }
                                        if (end1[0].compareTo(currentTime) < 0) {
                                            Log.e(TAG, stat.getTitle() + "/n" + end1[0].toString() + "/n" + currentTime.toString());
                                            paths.add(new Pair<>(objectDocumentSnapshot.getReference().getPath(), stat.getTitle()));
                                        }

                                        CStations.add(stat);
                                        count++;
                                    }

                                    if(count==size){
                                        update();
                                        if(stations.isEmpty()==false){
                                            listFragment.studyMiss(stations,listFragment);
                                        }
                                        if (paths.isEmpty()==false){
                                            for(Pair<String,String> stuff: paths){
                                                new DeleteAsyncTask(stuff.first,stuff.second,mem).execute();
                                            }

                                        }
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

package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.kashmirworldfoundation.snowleopardapp.Expand;

import org.kashmirworldfoundation.snowleopardapp.R;
import org.kashmirworldfoundation.snowleopardapp.Station;


import java.util.ArrayList;


public class ListFragment extends Fragment implements View.OnClickListener  {

    // objects
    private View ListFragment;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private static final String TAG = "ListFragment";

    //
    private RecyclerView recyclerView;
    private ListFragmentAdapter listFragmentAdapter;
    private ArrayList<Station> stationArrayList;
    private int pos;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {;

        ListFragment=inflater.inflate(R.layout.activity_listfragment_recycler, container, false);
        recyclerView=ListFragment.findViewById(R.id.recyler);
        stationArrayList= new ArrayList<>();
        // Add data from Firebase on the the Arrays
        new StationAsyncTask(this).execute();

        return ListFragment;
    }

    @Override
    public void onClick(View v) {
        pos =recyclerView.getChildLayoutPosition(v);
        Station selectiion=stationArrayList.get(pos);
        Intent i= new Intent(getActivity().getApplicationContext(), Expand.class);
        i.putExtra("station",selectiion);
        startActivity(i);


    }


    //SationAsyncTask would update this
    public void updateStationList(ArrayList<Station> s){
        stationArrayList.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        listFragmentAdapter=new ListFragmentAdapter(stationArrayList,this);
        recyclerView.setAdapter(listFragmentAdapter);
        LinearLayoutManager a=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(a);
    }


    @Override
    public void onResume(){
        super.onResume();
    }


    public static ListFragment newInstance() {
        return new ListFragment();
    }

}
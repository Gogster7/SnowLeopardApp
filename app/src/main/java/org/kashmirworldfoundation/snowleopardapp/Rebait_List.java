package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Rebait_List extends AppCompatActivity {


    public String AuthorS;
    private RecyclerView recyclerView;
    public String pathRecord;
    private ArrayList<Rebait> CRebaitArrayList= new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebait__list);
        recyclerView=findViewById(R.id.RecyclerRebait2);
        AuthorS=getIntent().getStringExtra("stationN");
        pathRecord=getIntent().getStringExtra("path");
        new RebaitAsyncTaskA(this).execute();

    }
    public void updateStationList(ArrayList<Rebait> s){
        CRebaitArrayList.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        Rebait_ListAdapter listAdapter=new Rebait_ListAdapter(CRebaitArrayList,this);
        recyclerView.setAdapter(listAdapter);
        LinearLayoutManager a=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(a);


    }
}
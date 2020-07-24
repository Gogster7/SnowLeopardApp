package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.kashmirworldfoundation.snowleopardapp.R;
import org.kashmirworldfoundation.snowleopardapp.Station;


import java.util.ArrayList;


public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentViewholder> {
    ListFragment listFragment;
    ArrayList<Station> stationList;
    Station station;

    ListFragmentAdapter(ArrayList<Station> stationList,ListFragment listFragment){
        this.listFragment=listFragment;
        this.stationList=stationList;
    }

    @NonNull
    @Override
    public ListFragmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext())
                //pass the list_row_item back to itemView
                .inflate(R.layout.row_station,parent,false);

        //wait to open browser
        itemView.setOnClickListener(listFragment);
        //and pass the entry
        return new ListFragmentViewholder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ListFragmentViewholder holder, int position) {
        station=stationList.get(position);

        holder.stationId.setText(station.getStationId());
        holder.dateId.setText(station.getPosted());
        holder.imgId.setImageResource(R.drawable.kwflogo);

    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

}

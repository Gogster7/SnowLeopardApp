package org.kashmirworldfoundation.snowleopardapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.kashmirworldfoundation.snowleopardapp.Fragment.ListFragment;
import org.kashmirworldfoundation.snowleopardapp.Fragment.ListFragmentViewholder;

import java.util.ArrayList;

public class Station_ListAdapter extends RecyclerView.Adapter<Station_List_ViewHolder> {
    Station_List activity;
    //final public ListItemClickListener mOnClickListener;

    ArrayList<CameraStation> CstationList;
    Context mContext;
    CameraStation Cstation;
    Station_ListAdapter(ArrayList<CameraStation> stationList,Station_List activity){
        this.activity=activity;
        this.CstationList=stationList;

    }

    @NonNull
    @Override
    public Station_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext())
                //pass the list_row_item back to itemView
                .inflate(R.layout.row_station,parent,false);
        Log.e("layout", "inflated");
        //wait to open browser
        itemView.setOnClickListener(activity);
        //and pass the entry
        return new Station_List_ViewHolder(itemView);

    }


    public void onBindViewHolder(@NonNull Station_List_ViewHolder holder, int position) {
        Cstation=CstationList.get(position);

        holder.stationId.setText(Cstation.getStationId());
        holder.dateId.setText(Cstation.getPosted().toDate().toString());
        fetchData(Cstation.getPic(),holder.imgId);

    }
    private void fetchData(String location, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference(location);
        GlideApp.with(activity)
                .load(ref)
                .into(image);
    }







    public int getItemCount() {
        return CstationList.size();
    }

}

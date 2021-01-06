package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.kashmirworldfoundation.snowleopardapp.CameraStation;
import org.kashmirworldfoundation.snowleopardapp.GlideApp;
import org.kashmirworldfoundation.snowleopardapp.R;
import org.kashmirworldfoundation.snowleopardapp.Study;


import java.util.ArrayList;


public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentViewholder> {
    ListFragment listFragment;


    ArrayList<Study> CstationList;
    Study Cstation;
    ListFragmentAdapter(ArrayList<Study> stationList, ListFragment listFragment){
        this.listFragment=listFragment;
        this.CstationList=stationList;

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
        Cstation=CstationList.get(position);

        holder.Study.setText(Cstation.getTittle());
        holder.dateId.setText(Cstation.getEnd().toDate().toString());
        holder.imgId.setImageResource(R.drawable.kwflogo);


    }

    private void fetchData(String location, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference(location);
        GlideApp.with(listFragment)
                .load(ref)
                .into(image);
    }

    @Override
    public int getItemCount() {
        return CstationList.size();
    }

}

package org.kashmirworldfoundation.snowleopardapp;

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

import java.util.ArrayList;

public class Rebait_ListAdapter extends RecyclerView.Adapter<Rebait_List_ViewHolder> {

    Rebait_List activity;
    //final public ListItemClickListener mOnClickListener;

    ArrayList<Rebait> CrebaitList;
    Context mContext;
    Rebait Crebait;
    Rebait_ListAdapter(ArrayList<Rebait> RebaitList,Rebait_List activity){
        this.activity=activity;
        this.CrebaitList=RebaitList;

    }

    @NonNull
    @Override
    public Rebait_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext())
                //pass the list_row_item back to itemView
                .inflate(R.layout.row_rebait,parent,false);
        Log.e("layout", "inflated");
        //wait to open browser

        //and pass the entry
        return new Rebait_List_ViewHolder(itemView);

    }


    public void onBindViewHolder(@NonNull Rebait_List_ViewHolder holder, int position) {
        Crebait=CrebaitList.get(position);

        holder.Name.setText(activity.AuthorS);
        holder.Date.setText(Crebait.getCurrentTime().toString());
        holder.Works.setText(Crebait.getCamWorks());
        holder.Bait.setText(Crebait.getLureType());
        holder.Sd.setText(Crebait.getSdNum());
        holder.Pics.setText(Crebait.getTotalPics());
        holder.Notes.setText("");

    }





    public int getItemCount() {
        return CrebaitList.size();
    }

}

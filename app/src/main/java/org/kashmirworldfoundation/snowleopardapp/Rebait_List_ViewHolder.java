package org.kashmirworldfoundation.snowleopardapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Rebait_List_ViewHolder extends RecyclerView.ViewHolder{
    TextView Name,Notes,Sd,Works,Bait,Date, Pics;
    public Rebait_List_ViewHolder(@NonNull View itemView) {
        super(itemView);
        Name=itemView.findViewById(R.id.rowEname);
        Notes=itemView.findViewById(R.id.rowComments);
        Sd=itemView.findViewById(R.id.rowSDnum);
        Works=itemView.findViewById(R.id.rowWorks);
        Bait=itemView.findViewById(R.id.rowBait);
        Date=itemView.findViewById(R.id.rowDateId);
        Pics=itemView.findViewById(R.id.rowNumPics);
    }
}

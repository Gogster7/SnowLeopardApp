package org.kashmirworldfoundation.snowleopardapp;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.kashmirworldfoundation.snowleopardapp.R;
public class Station_List_ViewHolder extends RecyclerView.ViewHolder{



        TextView stationId;
        TextView dateId;
    ImageView imgId;

        public Station_List_ViewHolder(View view) {
            super(view);
            stationId=view.findViewById(R.id.rowStationId2);
            dateId=view.findViewById(R.id.rowDateId2);
            imgId=view.findViewById(R.id.rowImgId2);


        }
    }


package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.kashmirworldfoundation.snowleopardapp.R;

import java.text.BreakIterator;

public class ListFragmentViewholder extends RecyclerView.ViewHolder {

    TextView Study;
    TextView dateId;
    ImageView imgId;

    public ListFragmentViewholder(View view) {
        super(view);
        Study=view.findViewById(R.id.rowStationId2);
        dateId=view.findViewById(R.id.rowDateId2);
        imgId=view.findViewById(R.id.rowImgId2);

    }
}

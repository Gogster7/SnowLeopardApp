package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.kashmirworldfoundation.snowleopardapp.Expand;
import org.kashmirworldfoundation.snowleopardapp.MapCreateList;
import org.kashmirworldfoundation.snowleopardapp.R;

public class MapFragment extends Fragment {
    // objects
    private View mapFragment;
    private Button createMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mapFragment=inflater.inflate(R.layout.fragment_map, container, false);
        createMap=mapFragment.findViewById(R.id.mapCreateButtonId);
        createMap.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               createMap();
           }
       });

        return mapFragment;
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public void createMap(){
        Intent intent = new Intent(getActivity().getApplicationContext(), MapCreateList.class);
        startActivity(intent);
    }

}

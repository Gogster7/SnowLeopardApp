package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kashmirworldfoundation.snowleopardapp.R;


public class ListFragment extends Fragment {

    private View ListFragment ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListFragment=inflater.inflate(R.layout.fragment_list, container, false);
        return ListFragment;
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    public static ListFragment newInstance() {
        return new ListFragment();
    }



}

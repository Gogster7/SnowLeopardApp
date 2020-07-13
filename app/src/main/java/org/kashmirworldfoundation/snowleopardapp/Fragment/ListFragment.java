package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.kashmirworldfoundation.snowleopardapp.Expand;
import org.kashmirworldfoundation.snowleopardapp.R;
import org.kashmirworldfoundation.snowleopardapp.Station;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ListFragment extends Fragment  {

    // objects
    private View ListFragment;
    private ListView listView;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private ArrayList<Station> stations=new ArrayList<>();
    private int images[] = {R.drawable.kwflogo};
    private static final String TAG = "ListFragment";

    private ArrayList<String> mTitle = new ArrayList<String>();
    private ArrayList<String> mDate = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {;

        ListFragment=inflater.inflate(R.layout.fragment_list, container, false);

        listView = ListFragment.findViewById(R.id.ListView);


        // Add data from Firebase on the the Arrays
        new StationAsyncTask(this).execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity().getApplicationContext(), Expand.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                bundle.putInt("image",images[0]);
                intent.putExtras(bundle);
                intent.putExtra("title",mTitle.get(position));
                intent.putExtra("date",mDate.get(position));
                intent.putExtra("station",stations.get(position));
                // now put title and description in expand activity
//                intent.putExtra("stationId",stations.get(position).getStationId());
//                intent.putExtra("posted",stations.get(position).getPosted().toString());

                // also put your postion
                intent.putExtra("position",""+position);


                startActivity(intent);
                //Toast.makeText(getActivity(), "Station One Date", Toast.LENGTH_SHORT).show();

               /* if (position == 0) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Expand.class);
                    // this intent put our 0 index image to another activity
                    Bundle bundle = new Bundle();
                    bundle.putInt("image",images[0]);
                    intent.putExtras(bundle);
                    // now put title and description in expand activity
                    intent.putExtra("title",mTitle[0]);
                    intent.putExtra("Date",mDate[0]);
                    // also put your postion
                    intent.putExtra("position",""+0);
                    startActivity(intent);
                    //Toast.makeText(getActivity(), "Station One Date", Toast.LENGTH_SHORT).show();
                }
                if (position == 1) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Expand.class);
                    // this intent put our 1 index image to another activity
                    Bundle bundle = new Bundle();
                    bundle.putInt("image",images[0]);
                    intent.putExtras(bundle);
                    // now put title and description in expand activity
                    intent.putExtra("title",mTitle[1]);
                    intent.putExtra("Date",mDate[1]);
                    // also put your postion
                    intent.putExtra("position",""+1);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Expand.class);
                    // this intent put our 2 index image to another activity
                    Bundle bundle = new Bundle();
                    bundle.putInt("image",images[0]);
                    intent.putExtras(bundle);
                    // now put title and description in expand activity
                    intent.putExtra("title",mTitle[2]);
                    intent.putExtra("Date",mDate[2]);
                    // also put your postion
                    intent.putExtra("position",""+2);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Expand.class);
                    // this intent put our 3 index image to another activity
                    Bundle bundle = new Bundle();
                    bundle.putInt("image",images[0]);
                    intent.putExtras(bundle);
                    // now put title and description in expand activity
                    intent.putExtra("title",mTitle[3]);
                    intent.putExtra("Date",mDate[3]);
                    // also put your postion
                    intent.putExtra("position",""+3);
                    startActivity(intent);
                }*/
            }
        });

        return ListFragment;
    }

    //SationAsyncTask would update this
    public void updateTitle(ArrayList<String> t) {
        mTitle.addAll(t);
    }

    //SationAsyncTask would update this
    public void updateDate(ArrayList<String> d) {
        mDate.addAll(d);
    }

    //SationAsyncTask would update this
    public void updateStationList(ArrayList<Station> s){
        stations.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        MyAdapter adapter = new MyAdapter(getActivity(),mTitle, mDate,stations, images);
        listView.setAdapter(adapter);
    }


    @Override
    public void onResume(){
        super.onResume();
    }


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rTitle = new ArrayList<String>();
        ArrayList<String> rDate = new ArrayList<>();
        ArrayList<Station> stations=new ArrayList<>();
        int rImgs[];

        MyAdapter (Context c, ArrayList<String> title, ArrayList<String> date, ArrayList<Station> s,int imgs[]) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDate = date;
            this.rImgs = imgs;
            this.stations=s;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDate = row.findViewById(R.id.textView2);

            images.setImageResource(rImgs[0]);
            myTitle.setText(rTitle.get(position));
            myDate.setText(rDate.get(position));
            Log.d(TAG, "getView: !"+stations.get(position).getStationId());
            return row;
        }
    }


}
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

import org.kashmirworldfoundation.snowleopardapp.Expand;
import org.kashmirworldfoundation.snowleopardapp.R;


import java.util.Calendar;
import java.util.Date;


public class ListFragment extends Fragment {

    private View ListFragment;
    private ListView listView;
    private String mTitle[] = {"StationOne", "StationTwo", "StationThree", "StationFour"};
    private String mDate[] = {"Date1", "Date2", "Date3", "Date4"};
    private int images[] = {R.drawable.kwflogo};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListFragment=inflater.inflate(R.layout.fragment_list, container, false);

        listView = ListFragment.findViewById(R.id.ListView);

        MyAdapter adapter = new MyAdapter(getActivity(), mTitle, mDate, images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
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
                }
            }
        });

        return ListFragment;
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
        String rTitle[];
        String rDate[];
        int rImgs[];

        MyAdapter (Context c, String title[], String date[], int imgs[]) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDate = date;
            this.rImgs = imgs;
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
            myTitle.setText(rTitle[position]);
            myDate.setText(rDate[position]);

            return row;
        }
    }


}
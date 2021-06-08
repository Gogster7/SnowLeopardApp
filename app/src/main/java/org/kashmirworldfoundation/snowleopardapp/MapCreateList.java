

package org.kashmirworldfoundation.snowleopardapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MapCreateList extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private MapAdapter mapAdapter;
    private ArrayList<MapItem> m;
    private int pos;
    private static final String TAG = "MapCreateList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapcreate_list);
        recyclerView=findViewById(R.id.recyler);

        m= new ArrayList<>();
//         m.add(new MapItem("Baidu Map","This is Baidu Map",R.drawable.du_logo));
        m.add(new MapItem("Google map","Use it Online",R.drawable.googlemap_logo));
        m.add(new MapItem("MapBox","This is mapbox.",R.drawable.mapbox_logo));

        mapAdapter=new MapAdapter(m,this);
        recyclerView.setAdapter(mapAdapter);
        LinearLayoutManager a=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(a);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: the item");

        pos =recyclerView.getChildLayoutPosition(v);
        Log.d(TAG, "in onClick Pos position" + pos);
        MapItem selectiion=m.get(pos);
        //mapbox
        if(pos==1){
            mapStyleSet();
        }

        if(pos==0){


            String symbol=selectiion.getName();
            Intent i= new Intent(getApplicationContext(),GoogleMapActivity.class);

            i.putExtra("Name",symbol);
            startActivity(i);

        }

// for Baidu map
//         if (pos == 0) {
//             String symbol=selectiion.getName();
//             Intent i= new Intent(getApplicationContext(),MapCreateActivity.class);
//             i.putExtra("Name",symbol);
//             startActivity(i);
//         }



    }
    private void mapStyleSet() {
        // Set up download interaction. Display a dialog
        // when the user clicks download button and require
        // a user-provided region name
        AlertDialog.Builder builder = new AlertDialog.Builder(MapCreateList.this);


        builder.setTitle("Choose a Map Style")
                .setItems(R.array.MapStyle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Intent i= new Intent(getApplicationContext(), OfflineManagerActivity.class);

                        switch(which){
                            case 0:
                                i.putExtra("Style","Streets");
                                break;
                            case 1:
                                i.putExtra("Style","OutDoors");
                                break;
                            case 2:
                                i.putExtra("Style","Satellite Streets");
                                break;
                            default:

                        }


                        startActivity(i);
                    }
                });



        // Display the dialog
        builder.show();
    }



}






//mapAdapter
class MapAdapter extends RecyclerView.Adapter<mapListViewholder> {
    MapCreateList mapCreateList;
    ArrayList<MapItem> m;
    MapItem item;

    MapAdapter(ArrayList<MapItem> m,MapCreateList mapCreateList){
        this.mapCreateList=mapCreateList;
        this.m=m;
    }


    @NonNull
    @Override
    public mapListViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext())
                //pass the list_row_item back to itemView
                .inflate(R.layout.row_createmap,parent,false);

        //wait to open browser
        itemView.setOnClickListener(mapCreateList);
        //and pass the entry
        return new mapListViewholder(itemView);
    }
    //take note out from arraylist//update all the data
    @Override
    public void onBindViewHolder(@NonNull mapListViewholder holder, int position) {
        item=m.get(position);

        holder.mapName.setText(item.getName());
        holder.mapContent.setText(item.getMapContent());
        holder.mapLogo.setImageResource(item.getMapLogo());

    }
    @Override
    public int getItemCount() {
        return m.size();
    }


}

//viewHolder
class mapListViewholder extends RecyclerView.ViewHolder{
    TextView mapName;
    TextView mapContent;
    ImageView mapLogo;

    public mapListViewholder(View view) {
        super(view);
        mapName=view.findViewById(R.id.rowStationId2);
        mapContent=view.findViewById(R.id.rowDateId2);
        mapLogo=view.findViewById(R.id.mapListLogoId);
    }

}

//mapItem
class MapItem{
    private String mapName;
    private String mapContent;
    private int mapLogo;


    MapItem(String mapName,String mapContent,int mapLogo){
        this.mapName=mapName;
        this.mapContent=mapContent;
        this.mapLogo=mapLogo;
    }

    String getName(){
        return mapName;
    }
    String getMapContent(){
        return mapContent;
    }

    int getMapLogo(){
        return mapLogo;
    }


}

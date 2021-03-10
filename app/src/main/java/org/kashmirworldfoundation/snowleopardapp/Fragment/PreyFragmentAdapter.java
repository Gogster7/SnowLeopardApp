package org.kashmirworldfoundation.snowleopardapp.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.kashmirworldfoundation.snowleopardapp.GlideApp;
import org.kashmirworldfoundation.snowleopardapp.Prey;
import org.kashmirworldfoundation.snowleopardapp.R;

import java.util.ArrayList;

public class PreyFragmentAdapter extends RecyclerView.Adapter<PreyFragmentViewHolder> {
    PreyFragment preyFragment;
    ArrayList<Prey> preylist;
    Prey Cprey;
    PreyFragmentAdapter(ArrayList<Prey> prey,PreyFragment preyFragment){
        this.preyFragment=preyFragment;
        this.preylist=prey;
    }
    @NonNull
    @Override
    public PreyFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                //pass the list_row_item back to itemView
                .inflate(R.layout.row_prey,parent,false);

        //wait to open browser
        itemView.setOnClickListener(preyFragment);
        //and pass the entry
        return new PreyFragmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PreyFragmentViewHolder holder, int position) {

            Cprey=preylist.get(position);

            holder.Prey.setText(Cprey.getPrey());
            holder.dateId.setText(Cprey.getPosted().toDate().toString());
            fetchData(Cprey.getPic(),holder.imgId);




    }
    private void fetchData(String location, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference(location);
        GlideApp.with(preyFragment)
                .load(ref)
                .into(image);
    }
    @Override
    public int getItemCount() {
        return preylist.size();
    }
}

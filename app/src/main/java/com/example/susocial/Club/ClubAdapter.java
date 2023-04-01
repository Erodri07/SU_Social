package com.example.susocial.Club;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.susocial.ClubDetail;
import com.example.susocial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> { //implements Filterable

    private List<ClubModel> clublist;
    private List<String> documentIds;
    private OnItemClickListener mlistener;

    public ClubAdapter(List<String>documentIds){
        this.documentIds = documentIds;
    }
    public interface OnItemClickListener{
        void onItemClick(int position, String clubName);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Clubs");
        clublist = new ArrayList<>();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String name = document.getString("Name");
                        String description = document.getString("Description");
                        String rate = "N/A";
                        String image = document.getString("clubPic");
                        ClubModel clubMode = new ClubModel(image,name,description,rate);
                        if(clublist!=null){
                            clublist.add(clubMode);
                        }
                        documentIds.add(document.getId());
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clubinfo_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapter.ViewHolder holder, int position) {
        String documentId = documentIds.get(position);
        ClubModel currentclub = clublist.get(position);
        Picasso.get().load(currentclub.getImageview1()).into(holder.imageView);
        holder.textView1.setText((CharSequence) currentclub.getTextview1());
        holder.textView2.setText((CharSequence) currentclub.getTextview2());
        holder.textView3.setText(currentclub.getTextview3());


    }

    @Override
    public int getItemCount() {
        return documentIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{ //implements View.OnClickListener
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        //Context context;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.clubPicture);
            textView1 = itemView.findViewById(R.id.club_name);
            textView2 = itemView.findViewById(R.id.club_introduction);
            textView3 = itemView.findViewById(R.id.club_rate);

            itemView.setOnClickListener(this);

            itemView.setOnClickListener(v -> {
                if(mlistener != null){
                    int position = getAdapterPosition();
                    String clubName = textView1.getText().toString();
                    if (position !=RecyclerView.NO_POSITION){
                        mlistener.onItemClick(position, clubName);
                    }
                }
            });



        }

        @Override
        public void onClick(View v) {

        }
    }
}

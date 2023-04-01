package com.example.susocial.Event;

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

import com.example.susocial.EventDetail;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventList;
    private List<String> documentIds;
    private OnItemClickListener mlistener;

    public EventAdapter(List<String> documentIds){
        this.documentIds = documentIds;
    }
    public interface OnItemClickListener{
        void onItemClick(int position, String eventName);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }
    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Events");
        eventList= new ArrayList<>();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String name = document.getString("Name");
                        String date = document.getString("Date");
                        String time = document.getString("Time");
                        String location = document.getString("Location");
                        String image = document.getString("eventPic");
                        //int image = R.drawable.ic_profile;
                        EventModel eventMode = new EventModel(image, name,location,date,time);
                        if(eventList!=null){
                            eventList.add(eventMode);
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
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eveninfo_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        String documentId = documentIds.get(position);
        EventModel currentEvent = eventList.get(position);
        Picasso.get().load(currentEvent.getEventImage()).into(holder.imageView);
        holder.textView1.setText(currentEvent.getEventName());
        holder.textView2.setText(currentEvent.getEventLocat());
        holder.textView3.setText(currentEvent.getEventDate());
        holder.textView4.setText(currentEvent.getEventTime());


    }

    @Override
    public int getItemCount() {
        return documentIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.eventPicture);
            textView1 = itemView.findViewById(R.id.event_name);
            textView2 = itemView.findViewById(R.id.event_location);
            textView3 = itemView.findViewById(R.id.event_date);
            textView4 = itemView.findViewById(R.id.event_time);

            itemView.setOnClickListener(this);

            itemView.setOnClickListener(v -> {
                if(mlistener != null){
                    int position = getAdapterPosition();
                    String eventName = textView1.getText().toString();
                    if (position !=RecyclerView.NO_POSITION){
                        mlistener.onItemClick(position, eventName);
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}

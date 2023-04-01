package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.susocial.Event.EventModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EventDetail extends AppCompatActivity implements View.OnClickListener {
    private DocumentReference clubRef;
    private DocumentReference collectRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<EventModel> eventlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        getSupportActionBar().setTitle("Event Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");

        TextView nameTextView = findViewById(R.id.edetail_name);
        TextView descripTextView = findViewById(R.id.edetail_descrip);
        TextView locatTextView = findViewById(R.id.edetail_location);
        ImageView eventimageView = findViewById(R.id.edetail_image);
        TextView timeTextView = findViewById(R.id.edetail_time);
        TextView dateTextView = findViewById(R.id.edetail_date);

        DocumentReference eventlist = db.collection("Events").document(eventName);
        eventlist.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot Data: " + document.getData());
                        String ename = document.getString("Name");
                        String edescription = document.getString("Description");
                        String elocat = document.getString("Location");
                        String etime = document.getString("Time");
                        String edate = document.getString("Date");
                        String url = document.getString("eventPic");

                        nameTextView.setText(ename);
                        descripTextView.setText(edescription);
                        locatTextView.setText(elocat);
                        timeTextView.setText(etime);
                        dateTextView.setText(edate);
                        Picasso.get().load(url).resize(300,300).centerCrop().into(eventimageView);
//                      Picasso.get().load(url).fit().centerCrop().into(clubimageView);
                    }
                    else {
                        Log.d("TAG", "No Such Document");
                        Log.d("TAG", eventName);
                    }
                }
                else {
                    Log.d("TAG", "Get Failed with, " + task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
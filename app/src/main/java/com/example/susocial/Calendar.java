package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.susocial.Club.ClubAdapter;
import com.example.susocial.Club.ClubModel;
import com.example.susocial.Event.EventAdapter;
import com.example.susocial.Event.EventModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Calendar extends AppCompatActivity implements View.OnClickListener, EventAdapter.OnItemClickListener {
    //Mingyan Zhang implemented the whole Calendar file.
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    //ClubAdapter adapter;
    private EventAdapter adapter;
    private List<EventModel> eventlist;
    private List<String> documentIds;
    private FirebaseFirestore db;
    private DocumentReference eventRef;
    private CollectionReference collectRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // UI for ActionBar
        getSupportActionBar().setTitle("Calendar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        List <String> documentIds = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView_calendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(documentIds);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.loadData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(int position, String eventName) {
        Intent intent = new Intent(this,EventDetail.class);
        intent.putExtra("eventName",eventName);
        startActivity(intent);

    }
}
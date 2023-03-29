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


public class Home extends AppCompatActivity implements View.OnClickListener,ClubAdapter.OnItemClickListener{
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    // private DocumentReference  userRef //Implement once we get profile unique information

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    //ClubAdapter adapter;
    private ClubAdapter adapter;
    private List<ClubModel> clublist;
    private List<String> documentIds;
    private FirebaseFirestore db;
    private DocumentReference clubRef;
    private CollectionReference collectRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        List <String> documentIds = new ArrayList<>();

       // SearchView searchView = findViewById(R.id.search_home);

        recyclerView = findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClubAdapter(documentIds);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.loadData();


        //UI for ActionBar
        getSupportActionBar().setTitle("Home");

        // Bottom Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(),Message.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),Calendar.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }


    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if (user==null){
            startActivity(new Intent(Home.this, LoginUI.class));
        }

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ClubDetail.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}
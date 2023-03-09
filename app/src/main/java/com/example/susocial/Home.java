package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.susocial.Club.ClubAdapter;
import com.example.susocial.Club.ClubModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity implements View.OnClickListener{
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    // private DocumentReference  userRef //Implement once we get profile unique information

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ClubModel>clublist;
    ClubAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        //Club RecyclerView
        initData();
        initRecyclerView();

        //UI for ActionBar
        getSupportActionBar().setTitle("Home");

        // Bottom Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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

    private void initData() {
        clublist = new ArrayList<>();
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
    }

    private void initRecyclerView() {
        recyclerView=findViewById(R.id.recyclerView_home);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ClubAdapter(clublist);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


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
}
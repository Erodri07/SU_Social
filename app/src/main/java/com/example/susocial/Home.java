package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

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
        clublist.add(new ClubModel(R.drawable.ic_profile, "Active Minds","Active Minds is an organization to promote mental health awareness and combat stigmas surrounding mental illness. " +
                "We are looking for more people who are devoted to our cause for this semester.","4.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "African Student Union","The mission of ASU is to promote and develop fellowship among African students and cultivate in them the spirit of " +
                "service in public interest. ASU shall, among other things, seek to build strong bonds between other minority organizations at SU and SUNY -ESF and work to increase the awareness of African " +
                "Students' interest and concerns at SU and SUNY-ESF. ASU also seeks to educate people about the continent of Africa and illuminate all the positive things that are so often overshadowed by the media.","4.0/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Alpha Chi Omega Fraternity","Together, Alpha Chi Omega’s members are working to impact the lives of others, especially through our national " +
                "philanthropy: domestic violence awareness. Alpha Chi Omega’s influence even spreads to Congress, where members are working with other Greek organizations on legislation to improve the lives and safety of " +
                "collegiate members. Lambda Chapter specifically works with the local Vera House here in Syracuse.","3.5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Baked Magazine","Baked Magazine is Syracuse University’s student-run food magazine, and our goal is to let you all know everything about food. " +
                "We have recipes, reviews, and everything in between. Enjoy and eat up!","3.0/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Campus Bible Fellowship","Campus Bible Fellowship is a Bible study, fellowship, and discussion group to encourage an understanding of the biblical " +
                "Christian faith. Everyone is encouraged to attend.","4.9/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Chinese Dance Student Association","Understand Chinese Pop Culture by learning dances that consists of Chinese traditional elements. Building up " +
                "a dance crew for video shootings.","5/5"));
        clublist.add(new ClubModel(R.drawable.ic_profile, "Information Security Club","The mission of Information Security Club is to help members develop the technical skills needed to secure network " +
                "infrastructure and end user devices, and to promote awareness of cybersecurity issues and challenges. We focus on offense (hacking) and defense (hardening) of systems, devices, and overall security policies." +
                " We also participate in various security competitions across the country, so that the club members get a hands on hacking experience.","4.0/5"));
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_home);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        }
        );


        return true;
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
package com.example.susocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserOrLeader extends AppCompatActivity implements View.OnClickListener {
    private Button chooseCLeader, chooseUser,chooseELeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_or_leader);

        chooseCLeader = findViewById(R.id.ChooseClubLeader);
        chooseCLeader.setOnClickListener(this);
        chooseELeader = findViewById(R.id.ChooseEventLeader);
        chooseELeader.setOnClickListener(this);
        chooseUser = findViewById(R.id.ChooseUser);
        chooseUser.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ChooseClubLeader:
                startActivity(new Intent(UserOrLeader.this, ClubCreation.class));
                break;
            case R.id.ChooseEventLeader:
                startActivity(new Intent(UserOrLeader.this, event_creation.class));
                break;
            case R.id.ChooseUser:
                startActivity(new Intent(UserOrLeader.this, Home.class));
                break;
        }
    }
}
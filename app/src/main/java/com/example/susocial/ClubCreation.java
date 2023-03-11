package com.example.susocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ClubCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_creation);

        getSupportActionBar().setTitle("Create A Club");
    }
}
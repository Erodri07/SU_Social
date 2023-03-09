package com.example.susocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setTitle("Rate this Club");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
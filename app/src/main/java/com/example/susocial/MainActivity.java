package com.example.susocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button profileButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI for ActionBar
        getSupportActionBar().setTitle("Main Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Buttons
        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.profileButton:
                openEventActivity();
                break;
            case R.id.searchButton:
                openClubActivity();
                break;
        }
    }

    private void openClubActivity() {
        Intent intent = new Intent(this, ClubCreation.class);
        startActivity(intent);
    }

    private void openEventActivity() {
        Intent intent = new Intent(this, ClubCreation.class);
        startActivity(intent);
    }

}
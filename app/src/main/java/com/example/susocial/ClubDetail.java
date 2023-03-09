package com.example.susocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.example.susocial.R;

public class ClubDetail extends AppCompatActivity implements View.OnClickListener {
    private Button clubRate;
    private Button clubChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);

        getSupportActionBar().setTitle("Club Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clubRate = findViewById(R.id.navg_rate);
        clubRate.setOnClickListener(this);

        clubChat = findViewById(R.id.navg_chat);
        clubChat.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navg_rate:
                startActivity(new Intent(ClubDetail.this, Review.class));
                break;
            case R.id.navg_chat:
                startActivity(new Intent(ClubDetail.this,Message.class));

    }
}
}

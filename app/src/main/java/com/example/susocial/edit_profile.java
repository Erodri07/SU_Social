package com.example.susocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class edit_profile extends AppCompatActivity implements View.OnClickListener {
    private Button editProfCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editProfCancel=findViewById(R.id.editProfile_cancel);
        editProfCancel.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.editProfile_cancel:
                startActivity(new Intent(edit_profile.this, Profile.class));
                break;
        }
    }
}
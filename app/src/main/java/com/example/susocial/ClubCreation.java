package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ClubCreation extends AppCompatActivity implements View.OnClickListener {

    private EditText clubName, clubPres, clubDes, clubContact;
    private Button cancelButton, submitButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_creation);

        getSupportActionBar().setTitle("Create A Club");

        clubName = findViewById(R.id.clubCreationName);
        clubPres = findViewById(R.id.clubCreationLeaderName);
        clubContact = findViewById(R.id.clubCreationContactInfo);
        clubDes = findViewById(R.id.clubCreationDescription);

        submitButton = findViewById(R.id.clubCreationSubmit);
        submitButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.clubCreationCancel);
        cancelButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clubCreationSubmit:
                createClub();
                break;
            case R.id.clubCreationCancel:
                startActivity(new Intent(ClubCreation.this, Home.class));
        }
    }

    private void createClub() {
        String name = clubName.getText().toString();
        String president = clubPres.getText().toString();
        String contactInfo = clubContact.getText().toString();
        String description = clubDes.getText().toString();

        if (TextUtils.isEmpty(name)) {
            clubName.setError("Club Name cannot be Empty");
            clubName.requestFocus();
        } else if (TextUtils.isEmpty(president)) {
            clubPres.setError("Club President cannot be Empty");
            clubPres.requestFocus();
        } else if (TextUtils.isEmpty(contactInfo)) {
            clubContact.setError("Contact Info cannot be Empty");
            clubContact.requestFocus();
        } else if (TextUtils.isEmpty(description)) {
            clubDes.setError("Description cannot be Empty");
            clubDes.requestFocus();
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("Name", name);
            data.put("President", president);
            data.put("ContactInfo", contactInfo);
            data.put("Description", description);

            db.collection("ClubsAndEvents")
                            .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error adding document", e);
                                                }
                                            });
            Toast.makeText(this, "Club Created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClubCreation.this, Home.class));

        }
    }
}
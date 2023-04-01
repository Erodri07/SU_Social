package com.example.susocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class club_name extends AppCompatActivity{

    private EditText clubName;
    private Button submitButton;
    private FirebaseFirestore db;
    private DocumentReference clubRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_name);

        clubName = findViewById(R.id.clubNameStart);
        submitButton = findViewById(R.id.submitNameButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clubNameNext = clubName.getText().toString();
                if (TextUtils.isEmpty(clubNameNext)){
                    clubName.setError("Club Name cannot be Empty");
                    clubName.requestFocus();
                }
                else {
                    pushName(clubName.getText().toString());
                }
            }
        });
    }

    private void pushName(String clubName) {
        Intent intent = new Intent(this,ClubCreation.class);
        intent.putExtra("clubName",clubName);
        startActivity(intent);
    }
}

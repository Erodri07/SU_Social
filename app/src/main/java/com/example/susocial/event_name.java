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

public class event_name extends AppCompatActivity {

    private EditText eventName;
    private Button submitButton;
    private FirebaseFirestore db;
    private DocumentReference clubRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_name);

        eventName = findViewById(R.id.nameEvent);
        submitButton = findViewById(R.id.submitNameEventButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vu) {
                String clubNameNext = eventName.getText().toString();
                if (TextUtils.isEmpty(clubNameNext)){
                    eventName.setError("Event Name cannot be Empty");
                    eventName.requestFocus();
                }
                else {
                    pushName(eventName.getText().toString());
                }
            }
        });
    }

    private void pushName(String eventName) {
        Intent intent = new Intent(this,event_creation.class);
        intent.putExtra("eventName",eventName);
        startActivity(intent);
    }
}

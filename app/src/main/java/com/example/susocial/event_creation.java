package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class event_creation extends AppCompatActivity implements View.OnClickListener{

    private EditText eventName, eventDate, eventTime, eventLocation, eventDescription;
    private Button cancelButton, submitButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        eventName = findViewById(R.id.eventCreationName);
        eventDate = findViewById(R.id.eventCreationDate);
        eventTime = findViewById(R.id.eventCreationTime);
        eventLocation = findViewById(R.id.eventCreationLocation);
        eventDescription = findViewById(R.id.eventCreationDescription);

        submitButton = findViewById(R.id.eventCreationSubmit);
        submitButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.eventCreationCancel);
        cancelButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eventCreationSubmit:
                createEvent();
                break;
            case R.id.eventCreationCancel:
                startActivity(new Intent(event_creation.this, Home.class));
        }
    }

    private void createEvent(){
        String name = eventName.getText().toString();
        String date = eventDate.getText().toString();
        String time = eventTime.getText().toString();
        String location = eventLocation.getText().toString();
        String description = eventDate.getText().toString();

        if (TextUtils.isEmpty(name)) {
            eventName.setError("Event Name cannot be Empty");
            eventName.requestFocus();
        } else if (TextUtils.isEmpty(date)) {
            eventDate.setError("Date cannot be Empty");
            eventDate.requestFocus();
        } else if (TextUtils.isEmpty(time)) {
            eventTime.setError("Time cannot be Empty");
            eventTime.requestFocus();
        } else if (TextUtils.isEmpty(location)) {
            eventLocation.setError("Location cannot be Empty");
            eventLocation.requestFocus();
        } else if (TextUtils.isEmpty(description)) {
            eventDescription.setError("Description cannot be Empty");
            eventDescription.requestFocus();
        } else {
            CollectionReference eventsList = db.collection("Events");
            Map<String, Object> data = new HashMap<>();
            data.put("Name", name);
            data.put("Date", date);
            data.put("Time", time);
            data.put("Location", location);
            data.put("Description", description);
            eventsList.document(name).set(data);

//            db.collection("Events")
//                    .add(data)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("TAG", "Error adding document", e);
//                        }
//                    });
            Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(event_creation.this, Home.class));
        }
    }

}
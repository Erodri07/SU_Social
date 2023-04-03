package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class UserOrLeader extends AppCompatActivity implements View.OnClickListener {
    private Button chooseCLeader, chooseUser,chooseELeader;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private String SUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_or_leader);

        //Mingyan Zhang
        chooseCLeader = findViewById(R.id.ChooseClubLeader);
        chooseCLeader.setOnClickListener(this);
        chooseELeader = findViewById(R.id.ChooseEventLeader);
        chooseELeader.setOnClickListener(this);
        chooseUser = findViewById(R.id.ChooseUser);
        chooseUser.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(UserOrLeader.this, LoginUI.class));
        }
        else {
            userRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                SUID = value.getString("userSUID");
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ChooseClubLeader:
                Query validClubSUIDs = db.collection("Club Leader IDs").whereEqualTo("SUID", SUID);
                AggregateQuery countValidClubSUIDs = validClubSUIDs.count();
                countValidClubSUIDs.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AggregateQuerySnapshot snapshot = task.getResult();
                            Log.d("TAG", "Count: " + snapshot.getCount());
                            if (snapshot.getCount() == 0){
                                Toast.makeText(UserOrLeader.this, "You are not A Club Leader", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(UserOrLeader.this, club_name.class));
                            }
                        }
                    }
                });
                break;
            case R.id.ChooseEventLeader:
                Query validEventSUIDs = db.collection("Club Leader IDs").whereEqualTo("SUID", SUID);
                AggregateQuery countValidEventSUIDs = validEventSUIDs.count();
                countValidEventSUIDs.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AggregateQuerySnapshot snapshot = task.getResult();
                            Log.d("TAG", "Count: " + snapshot.getCount());
                            if (snapshot.getCount() == 0){
                                Toast.makeText(UserOrLeader.this, "You are not An Event Leader", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(UserOrLeader.this, event_name.class));
                            }
                        }
                    }
                });
                break;
            case R.id.ChooseUser:
                startActivity(new Intent(UserOrLeader.this, Home.class));
                break;
        }
    }
}
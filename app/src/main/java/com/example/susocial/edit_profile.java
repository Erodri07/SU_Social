package com.example.susocial;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class edit_profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private String userID;
    private Button editProfCancel;
    private Button saveChanges;

    private EditText fullName;
    private EditText major;
    private EditText gradYear;
    private EditText userInterests;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editProfCancel=findViewById(R.id.editProfile_cancel);
        editProfCancel.setOnClickListener(this);
        saveChanges = findViewById(R.id.editProfile_save);
        saveChanges.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(edit_profile.this, LoginUI.class));
        }
        else {
            userRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        fullName = findViewById(R.id.edit_name);
        major = findViewById(R.id.edit_major);
        gradYear = findViewById(R.id.edit_year);
        userInterests = findViewById(R.id.edit_interest);

        

    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.editProfile_cancel:
                startActivity(new Intent(edit_profile.this, Profile.class));
                break;
            case R.id.editProfile_save:
                updateProfile();
        }
    }
    private void updateProfile() {
        String newUserName = fullName.getText().toString();
        String newMajor = major.getText().toString();
        String newGradYear = gradYear.getText().toString();
        String newInterests = userInterests.getText().toString();

        if (TextUtils.isEmpty(newUserName)) {
            fullName.setError("New Username cannot be Empty");
            fullName.requestFocus();
        }
        else if (TextUtils.isEmpty(newMajor)) {
            major.setError("New Major cannot be Empty");
            major.requestFocus();
        }
        else if (TextUtils.isEmpty(newGradYear)) {
            gradYear.setError("Gradutation Year cannot be Empty");
            gradYear.requestFocus();
        }
        else if (TextUtils.isEmpty(newInterests)) {
            userInterests.setError("User Interests cannot be Empty");
            userInterests.requestFocus();
        }
        else{
            userRef.update("userName", newUserName);
            userRef.update("Major", newMajor);
            userRef.update("Graduation Year", newGradYear);
            userRef.update("Interests", newInterests);

            startActivity(new Intent(edit_profile.this, Profile.class));
        }
    }
}
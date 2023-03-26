package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class passwordRetrieval extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private Button submitButton;
    private EditText userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval);

        mAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.passwordResetEmail);
        submitButton = findViewById(R.id.passwordResetButton);
        submitButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.passwordResetButton:
                resetPass();
        }
    }

    private void resetPass() {
        String email = userEmail.getText().toString();

        if (TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            userEmail.setError("Invalid Email");
            userEmail.requestFocus();
        }
        else{
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(passwordRetrieval.this, "Email Sent", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(passwordRetrieval.this, "Failed. Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(passwordRetrieval.this, "Log In Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
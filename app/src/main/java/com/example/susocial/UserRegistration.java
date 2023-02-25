package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    private EditText userName;
    private EditText userEmail;
    private EditText userPass;
    private EditText userPassCheck;
    private EditText userSUID;

    private Button registerUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userName = findViewById(R.id.fullName);
        userEmail = findViewById(R.id.SUEmailRegister);
        userPass = findViewById(R.id.PasswordRegister);
        userPassCheck = findViewById(R.id.passwordConfirm);
        userSUID = findViewById(R.id.SUID);

        registerUser = findViewById(R.id.RegisterButton);
        registerUser.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.RegisterButton:
                createUser();
                break;
        }
    }

    private void createUser() {
        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPass.getText().toString();
        String passwordCheck = userPassCheck.getText().toString();
        String suID = userSUID.getText().toString();


        if (TextUtils.isEmpty(name)){
            userName.setError("Name cannot be Empty");
            userName.requestFocus();
        }
        else if (TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            userEmail.setError("Invalid Email");
            userEmail.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            userPass.setError("Empty password not Allowed");
            userPass.requestFocus();
        } else if (TextUtils.isEmpty(passwordCheck)) {
            userPassCheck.setError("Empty password not Allowed");
            userPassCheck.requestFocus();
        }
        else if (!password.matches(passwordCheck)) {
            userPassCheck.setError("Passwords must Match");
            userPass.requestFocus();
            userPassCheck.requestFocus();
        }
        else if (password.length() < 6) {
            userPass.setError("Password should be at least 6 characters");
            userPass.requestFocus();
        }
        else if (TextUtils.isEmpty(suID)) {
            userSUID.setError("SUID cannot be Empty");
            userSUID.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UserRegistration.this, "User Registered", Toast.LENGTH_SHORT).show();
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);

                        Map<String,Object> user = new HashMap<>();
                        user.put("userName", name);
                        user.put("userEmail", email);
                        user.put("userSUID", suID);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: user Profile is Registered for: "+ userID);
                            }
                        });

                        startActivity(new Intent(UserRegistration.this, LoginUI.class));
                    } else {
                        Toast.makeText(UserRegistration.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
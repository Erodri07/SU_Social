package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginUI extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private Button logInButton;
    private Button forgotPassButton;
    private Button registerButton;

    private EditText emailInput;
    private EditText passInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);

        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.SUEmailLogin);
        passInput = findViewById(R.id.PasswordLogin);

        logInButton = findViewById(R.id.loginButton);
        logInButton.setOnClickListener(this);
        forgotPassButton = findViewById(R.id.GetPasswordButton);
        forgotPassButton.setOnClickListener(this);
        registerButton = findViewById(R.id.createAccButton);
        registerButton.setOnClickListener(this);

        //popup text for if user fails
        //if(login fails) then display this message
        Toast.makeText(LoginUI.this, "Login Failed", Toast.LENGTH_SHORT).show();
    }

    private void logInUser(){
        String email = emailInput.getText().toString();
        String password = passInput.getText().toString();

        if(TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            emailInput.setError("Invalid email");
            emailInput.requestFocus();

        }
        else if (TextUtils.isEmpty(password)){
            passInput.setError("Empty Password not allowed");
            passInput.requestFocus();
        }

        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginUI.this, "Successful Login!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginUI.this, Home.class));
                    }
                    else{
                        Toast.makeText(LoginUI.this, "Log In Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                logInUser();
                break;
            case R.id.createAccButton:
                openRegisterActivity();
                break;
        }
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, UserRegistration.class);
        startActivity(intent);
    }
}
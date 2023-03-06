package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    //areNotificationsEnabled()
    private final int NOTIFICATION_PERMISSION_CODE = 1;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog myDialog;
    private Button btndeleteCancel,btndeleteYes;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference userRef;

    private TextView userName;
    private TextView userEmail;
    private TextView userMajor;
    private TextView userGradYear;
    private TextView userInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //UI for ActionBar
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = findViewById(R.id.displayUserName);
        userEmail = findViewById(R.id.displayEmail);
        userMajor = findViewById(R.id.displayMajor);
        userGradYear = findViewById(R.id.displayGradYear);
        userInterests = findViewById(R.id.displayInterests);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(Profile.this, LoginUI.class));
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
                userName.setText(value.getString("userName"));
                userEmail.setText(value.getString("userEmail"));
                userMajor.setText(value.getString("Major"));
                userGradYear.setText(value.getString("Graduation Year"));
                userInterests.setText(value.getString("Interests"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settingItem1:
                startActivity(new Intent(Profile.this,edit_profile.class));
            case R.id.settingItem2:
                if (ContextCompat.checkSelfPermission(Profile.this,
                        Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Profile.this, "Notification Permissions Granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestNotiPermission();
                }
                return true;
            case R.id.settingItem3:
                deleteConfirmationDialog();
                return true;
            case R.id.settingItem4:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, LoginUI.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Delete Account Confiramtion Popup window
    public void deleteConfirmationDialog(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.deleteaccount_confirmation,null);

        btndeleteCancel = (Button)popupView.findViewById(R.id.delete_cancel);
        btndeleteYes = (Button) popupView.findViewById(R.id.delete_delete);

        dialogBuilder.setView(popupView);
        myDialog = dialogBuilder.create();
        myDialog.show();

        btndeleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Profile.this,"Account Deleted",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Profile.this, LoginUI.class));
                        }
                        else {
                            Toast.makeText(Profile.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btndeleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

    }

    private void requestNotiPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.POST_NOTIFICATIONS)) {

            new AlertDialog.Builder(this)
                    .setTitle("PermissionNeeded")
                    .setMessage("Permission needed for Notifications")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Profile.this,
                                    new String[] {Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notifications Will Now Be Pushed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
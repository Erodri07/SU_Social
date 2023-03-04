package com.example.susocial;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    //areNotificationsEnabled()
    private final int NOTIFICATION_PERMISSION_CODE = 1;
    private Button requestNotiButton;
    private Button logoutButton;
    private Button editProfButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog myDialog;
    private Button btndeleteCancel,btndeleteYes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //UI for ActionBar
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestNotiButton = findViewById(R.id.RequestNotifications);
        requestNotiButton.setOnClickListener(this);

        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);

        editProfButton = findViewById(R.id.EditProfile);
        editProfButton.setOnClickListener(this);


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
                startActivity(new Intent(Profile.this, LoginUI.class));
            }
        });
        btndeleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RequestNotifications:
                //request Notifications from User
                if (ContextCompat.checkSelfPermission(Profile.this, 
                        Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Profile.this, "Notification Permissions Granted", 
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestNotiPermission();
                }
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, LoginUI.class));
                break;
                // click on button, should go to edit_profile activity;
            case R.id.EditProfile:
                startActivity(new Intent(Profile.this,edit_profile.class));
                break;

        }
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
}
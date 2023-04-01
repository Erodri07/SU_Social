package com.example.susocial;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class event_creation extends AppCompatActivity implements View.OnClickListener{

    private EditText eventDate, eventTime, eventLocation, eventDescription;
    private TextView eventName;
    private Button cancelButton, submitButton, addEventPic;
    private ImageView eventPicture;
    private FirebaseFirestore db;
    private StorageReference imageStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        eventName = findViewById(R.id.eventCreationName);
        eventDate = findViewById(R.id.eventCreationDate);
        eventTime = findViewById(R.id.eventCreationTime);
        eventLocation = findViewById(R.id.eventCreationLocation);
        eventDescription = findViewById(R.id.eventCreationDescription);

        eventPicture = findViewById(R.id.profilePhoto);

        submitButton = findViewById(R.id.eventCreationSubmit);
        submitButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.eventCreationCancel);
        cancelButton.setOnClickListener(this);
        addEventPic = findViewById(R.id.addProfileImageButton);
        addEventPic.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String clubNameEnd = intent.getStringExtra("eventName");
        eventName.setText(clubNameEnd);

        imageStorageRef = FirebaseStorage.getInstance().getReference("images");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eventCreationSubmit:
                createEvent();
                break;
            case R.id.addProfileImageButton:
                uploadPicture();
                break;
            case R.id.eventCreationCancel:
                startActivity(new Intent(event_creation.this, Home.class));
        }
    }

    private void uploadPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startImagePickerActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> startImagePickerActivity=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        //upload image uri to Firebase Cloud Storage where user profile images are stored
                        uploadImage(imageUri);
                    }
                }
            });

    private void uploadImage(Uri uri){
        //put image uri in Firebase Cloud Storage
        StorageReference fileRef = imageStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                //get permanent image url of the profile image that is stored in Firebase Cloud Storage
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //update user profile image in the Firebase database with the image url from Cloud Storage
                        //CollectionReference clubsList = db.collection("Clubs");
                        Map<String, Object> data = new HashMap<>();
                        data.put("eventPic", uri.toString());
                        String name = eventName.getText().toString();

                        db.collection("Events").document(name)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error writing document", e);
                                    }
                                });
                        Picasso.get().load(uri.toString()).resize(210,210).centerCrop().into(eventPicture);
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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
        } else if (eventPicture.getDrawable() == null) {
            submitButton.setError("Club Picture Required");
            eventPicture.requestFocus();
            Toast.makeText(event_creation.this, "Event Picture Required", Toast.LENGTH_SHORT).show();
        }else {
//            CollectionReference eventsList = db.collection("Events");
//            Map<String, Object> data = new HashMap<>();
//            data.put("Name", name);
//            data.put("Date", date);
//            data.put("Time", time);
//            data.put("Location", location);
//            data.put("Description", description);
//            eventsList.document(name).set(data);

            DocumentReference eventsList = db.collection("Events").document(name);
            eventsList.update("Name", name);
            eventsList.update("Date", date);
            eventsList.update("Time", time);
            eventsList.update("Location", location);
            eventsList.update("Description", description);


            Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(event_creation.this, Home.class));
        }
    }

}
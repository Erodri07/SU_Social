package com.example.susocial;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ClubCreation extends AppCompatActivity implements View.OnClickListener {

    private EditText clubName, clubPres, clubDes, clubContact;
    private Button cancelButton, submitButton, pictureButton;
    private FirebaseFirestore db;
    private ImageView clubPicture;
    private StorageReference imageStorageRef;
    private DocumentReference clubRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_creation);

        getSupportActionBar().setTitle("Create A Club");

        clubName = findViewById(R.id.clubCreationName);
        clubPres = findViewById(R.id.clubCreationLeaderName);
        clubContact = findViewById(R.id.clubCreationContactInfo);
        clubDes = findViewById(R.id.clubCreationDescription);

        clubPicture = findViewById(R.id.profilePhoto);

        submitButton = findViewById(R.id.clubCreationSubmit);
        submitButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.clubCreationCancel);
        cancelButton.setOnClickListener(this);
        pictureButton = findViewById(R.id.addProfileImageButton);
        pictureButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        imageStorageRef = FirebaseStorage.getInstance().getReference("images");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clubCreationSubmit:
                createClub();
                break;
//            case R.id.addProfileImageButton:
//                uploadPicture();
//                break;
            case R.id.clubCreationCancel:
                startActivity(new Intent(ClubCreation.this, Home.class));
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
                        CollectionReference clubsList = db.collection("Clubs");
                        //Map<String, Object> data = new HashMap<>();
                        //data.put("clubPic", uri.toString());
                        String name = clubName.getText().toString();

                        clubRef = db.collection("Clubs").document(name);
                        clubRef.update("clubPic", uri.toString());
                    }
                });
            }
        });
        Toast.makeText(this, "Club Created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ClubCreation.this, Home.class));
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void createClub() {
        String name = clubName.getText().toString();
        String president = clubPres.getText().toString();
        String contactInfo = clubContact.getText().toString();
        String description = clubDes.getText().toString();

        if (TextUtils.isEmpty(name)) {
            clubName.setError("Club Name cannot be Empty");
            clubName.requestFocus();
        } else if (TextUtils.isEmpty(president)) {
            clubPres.setError("Club President cannot be Empty");
            clubPres.requestFocus();
        } else if (TextUtils.isEmpty(contactInfo)) {
            clubContact.setError("Contact Info cannot be Empty");
            clubContact.requestFocus();
        } else if (TextUtils.isEmpty(description)) {
            clubDes.setError("Description cannot be Empty");
            clubDes.requestFocus();
        } else {
            CollectionReference clubsList = db.collection("Clubs");
            Map<String, Object> data = new HashMap<>();
            data.put("Name", name);
            data.put("President", president);
            data.put("ContactInfo", contactInfo);
            data.put("Description", description);
            clubsList.document(name).set(data);

//            Map<String, Object> dummyReview = new HashMap<>();
//            dummyReview.put("Rate", "0.0");
//            clubsList.document(name).collection("Comments").document().set(dummyReview);

            uploadPicture();
        }
    }
}
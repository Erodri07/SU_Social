package com.example.susocial;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ClubCreation extends AppCompatActivity implements View.OnClickListener {

    private EditText clubPres, clubDes, clubContact;
    private Button cancelButton, submitButton, pictureButton;
    private TextView clubName;
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

        Intent intent = getIntent();
        String clubNameEnd = intent.getStringExtra("clubName");
        clubName.setText(clubNameEnd);

        imageStorageRef = FirebaseStorage.getInstance().getReference("images");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clubCreationSubmit:
                createClub();
                break;
            case R.id.addProfileImageButton:
                uploadPicture();
                break;
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
                        //CollectionReference clubsList = db.collection("Clubs");
                        Map<String, Object> data = new HashMap<>();
                        data.put("clubPic", uri.toString());
                        String name = clubName.getText().toString();

                        db.collection("Clubs").document(name)
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

                        Picasso.get().load(uri.toString()).resize(210,210).centerCrop().into(clubPicture);

                        //clubRef = db.collection("Clubs").document(name);
                        //DocumentReference clubRef = db.collection("Clubs").document(name);
                        //clubRef.update("clubPic", uri.toString());
                    }
                });
            }
        });
        //Toast.makeText(this, "Club Created", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(ClubCreation.this, Home.class));
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
        } else if (TextUtils.isEmpty(contactInfo) || !(Patterns.EMAIL_ADDRESS.matcher(contactInfo).matches())) {
            clubContact.setError("Invalid Email");
            clubContact.requestFocus();
        } else if (TextUtils.isEmpty(description)) {
            clubDes.setError("Description cannot be Empty");
            clubDes.requestFocus();
        } else if (clubPicture.getDrawable() == null) {
            submitButton.setError("Club Picture Required");
            clubPicture.requestFocus();
            Toast.makeText(ClubCreation.this, "Club Picture Required", Toast.LENGTH_SHORT).show();
        } else {
            //CollectionReference clubsList = db.collection("Clubs").;
//            Map<String, Object> data = new HashMap<>();
//            data.put("Name", name);
//            data.put("President", president);
//            data.put("ContactInfo", contactInfo);
//            data.put("Description", description);
//            data.put("clubPic", clubPicture);
            DocumentReference clubRef = db.collection("Clubs").document(name);
            clubRef.update("Name", name);
            clubRef.update("President", president);
            clubRef.update("ContactInfo", contactInfo);
            clubRef.update("Description", description);

            //clubsList.document(name).set(data);


            Toast.makeText(this, "Club Created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClubCreation.this, Home.class));


//            Map<String, Object> dummyReview = new HashMap<>();
//            dummyReview.put("Rate", "0.0");
//            clubsList.document(name).collection("Comments").document().set(dummyReview);

            //uploadPicture();
        }
    }
}
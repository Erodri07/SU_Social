package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.susocial.Club.ClubModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ClubDetail extends AppCompatActivity implements View.OnClickListener {
    private Button clubRate;

    private DocumentReference clubRef;
    private DocumentReference collectRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<ClubModel> clublist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);

        getSupportActionBar().setTitle("Club Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clubRate = findViewById(R.id.navg_rate);
        clubRate.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String clubName = intent.getStringExtra("clubName");

        TextView nameTextView = findViewById(R.id.cdetail_name);
        TextView descripTextView = findViewById(R.id.cdetail_descrip);
        TextView rateTextView = findViewById(R.id.cdetail_rate);
        TextView contactTextView = findViewById(R.id.cdetail_contact);
        ImageView clubimageView = findViewById(R.id.cdetail_image);
        TextView leaderTextView = findViewById(R.id.cdetail_leader);
        // clubimageView.setImageResource(R.drawable.ic_profile);

        DocumentReference clubsList = db.collection("Clubs").document(clubName);
        clubsList.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot Data: " + document.getData());
                        String cname = document.getString("Name");
                        String cdescription = document.getString("Description");
                        String ccontact = document.getString("ContactInfo");
                        String crate = "N/A";
                        String cleader = document.getString("President");
                        String url = document.getString("clubPic");


                        nameTextView.setText(cname);
                        descripTextView.setText(cdescription);
                        ///rateTextView.setText(crate);
                        contactTextView.setText(ccontact);
                        //clubimageView.setImageResource(cimage);
                        Picasso.get().load(url).fit().centerCrop().into(clubimageView);
                        leaderTextView.setText(cleader);
                    }
                    else {
                        Log.d("TAG", "No Such Document");
                        Log.d("TAG", clubName);
                    }
                }
                else {
                    Log.d("TAG", "Get Failed with, " + task.getException());
                }
            }
        });
        CollectionReference clubReview = clubsList.collection("Comments");
        clubReview.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Double count = 0.0;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        count += Double.parseDouble(Objects.requireNonNull(documentSnapshot.getString("Rate")));
                    }
                    NumberFormat formatter = new DecimalFormat("#0.00");
                    Double average = count / task.getResult().getDocuments().size();
                    rateTextView.setText(formatter.format(average));
                }
            }
        });

    }


    @Override
    //Mingyan Zhang
    public void onClick(View v) {
        Intent intent1 = getIntent();
        String clubName = intent1.getStringExtra("clubName");
        switch (v.getId()) {
            case R.id.navg_rate:
                Intent intent = new Intent(this, Review.class);
                intent.putExtra("ClubName",clubName); //heyhey
                startActivity(intent);
                break;

        }
    }
}

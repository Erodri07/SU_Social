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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;

public class ClubDetail extends AppCompatActivity implements View.OnClickListener {
    private Button clubRate;
    private Button clubChat;
    private DocumentReference clubRef;
    private CollectionReference collectRef;
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

        clubChat = findViewById(R.id.navg_chat);
        clubChat.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        TextView nameTextView = findViewById(R.id.cdetail_name);
        TextView descripTextView = findViewById(R.id.cdetail_descrip);
        TextView rateTextView = findViewById(R.id.cdetail_rate);
        TextView contactTextView = findViewById(R.id.cdetail_contact);
        ImageView clubimageView = findViewById(R.id.cdetail_image);
        TextView leaderTextView = findViewById(R.id.cdetail_leader);
        // clubimageView.setImageResource(R.drawable.ic_profile);

        // clubRef = db.collection("Clubs").document(mAuth.getCurrentUser().getUid());
        collectRef = db.collection("Club");

        collectRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String cname = document.getString("Name");
                        String cdescription = document.getString("Description");
                        String ccontact = document.getString("ContactInfo");
                        String crate = "N/A";
                        String cleader = document.getString("President");
                        int cimage = R.drawable.ic_profile;

                        nameTextView.setText(cname);
                        descripTextView.setText(cdescription);
                        rateTextView.setText(crate);
                        contactTextView.setText(ccontact);
                        clubimageView.setImageResource(cimage);
                        leaderTextView.setText(cleader);

                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navg_rate:
                startActivity(new Intent(ClubDetail.this, Review.class));
                break;
            case R.id.navg_chat:
                startActivity(new Intent(ClubDetail.this, Message.class));

        }
    }
}

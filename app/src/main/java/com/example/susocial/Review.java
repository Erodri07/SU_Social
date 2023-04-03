package com.example.susocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.susocial.Club.ClubAdapter;
import com.example.susocial.Club.ClubModel;
import com.example.susocial.Comment.CommentAdapter;
import com.example.susocial.Comment.CommentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Review extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText comment_edit;
    private RatingBar comment_star;
    private Button btn_submit, btn_cancel;
    private FloatingActionButton add_comment;
    private FirebaseFirestore db;
    private DocumentReference documentID;
    private CollectionReference clubRef;

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<CommentModel> commentList;
    private List<String> documentIds;
    private ClubModel Clubs;

    CollectionReference commentCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setTitle("Rate this Club");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_comment = findViewById(R.id.addcomment);
        add_comment.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        // need to get current Document reference
        clubRef = db.collection("Clubs");

        List<String> documentIds = new ArrayList<>();
        recyclerView = findViewById(R.id.review_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(documentIds);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String clubName = intent.getStringExtra("ClubName");

        adapter.LoadComment(clubName);
       // testing 1

    }


    // testing


    public void AddCommentDiaglog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View commentPopup = getLayoutInflater().inflate(R.layout.newcomment,null);

        btn_cancel = (Button) commentPopup.findViewById(R.id.comment_cancel);
        btn_submit = (Button) commentPopup.findViewById(R.id.comment_submit);
        comment_edit = (EditText) commentPopup.findViewById(R.id.comment_text);
        comment_star = (RatingBar) commentPopup.findViewById(R.id.comment_star);

        dialogBuilder.setView(commentPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddComment();
                dialog.dismiss();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //CollectionReference collectionReference = db.collection("Clubs").document(clubName).collection("Comments");
        commentList = new ArrayList<>();

    }

    private void AddComment() {
        String comment = comment_edit.getText().toString();
        String rate = String.valueOf(comment_star.getRating());

        if (TextUtils.isEmpty(comment)) {
            comment_edit.setError("Comment cannot be Empty");
            comment_edit.requestFocus();
            Toast.makeText(this, "Comment cannot be Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(rate)) {
            Toast.makeText(this, "Rate cannot be Empty", Toast.LENGTH_SHORT).show();
            comment_star.requestFocus();
        } else {
            Intent intent = getIntent();
            String clubName = intent.getStringExtra("ClubName");

            Map<String, Object> data = new HashMap<>();
            data.put("Comment", comment);
            data.put("Rate", rate);

            commentCollect = clubRef.document(clubName).collection("Comments");
            commentCollect.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("TAG", "DocumentSnapshot added with ID:" + documentReference.getId());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("TAG", "Error adding document", e);
                }
            });
            Toast.makeText(this,"Comment Added",Toast.LENGTH_SHORT).show();
//
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addcomment:
                AddCommentDiaglog();
                break;
        }
    }
}
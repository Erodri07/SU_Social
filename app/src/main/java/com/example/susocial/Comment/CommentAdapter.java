package com.example.susocial.Comment;

import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.susocial.Club.ClubAdapter;
import com.example.susocial.Club.ClubModel;
import com.example.susocial.Comment.CommentAdapter;
import com.example.susocial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> commentlist;
    private List<String> documentIds;
    //private String clubName;

    public CommentAdapter(List<String>documentIds){
        this.documentIds = documentIds;
    }

    public void LoadComment(String clubName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentCollect = db.collection("Clubs").document(clubName).collection("Comments");
        commentlist = new ArrayList<>();

        commentCollect.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String comment = document.getString("Comment");
                        String rate = document.getString("Rate");
                        CommentModel commentModel = new CommentModel(comment,rate,R.id.borderchangeLayout);
                        if(commentlist !=null){
                            commentlist.add(commentModel);
                        }
                        documentIds.add(document.getId());
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_display_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        String documentId = documentIds.get(position);
        CommentModel currentcomment = commentlist.get(position);
        if (position % 2 == 0) {
            holder.layout.setBackgroundResource(R.drawable.border);
        }
        else {
            holder.layout.setBackgroundResource(R.drawable.border2);
        }
        holder.textView1.setText((CharSequence) currentcomment.getComment());
        holder.textView2.setText((CharSequence) currentcomment.getRate());
    }

    @Override
    public int getItemCount() {
        return documentIds.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;
        private RelativeLayout layout;
        public ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.comment_display);
            textView2 = view.findViewById(R.id.rate_display);
            layout = view.findViewById(R.id.borderchangeLayout);

        }
    }
}

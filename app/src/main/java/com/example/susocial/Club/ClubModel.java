package com.example.susocial.Club;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ClubModel {

    private String imageview1;
    private String textview1;
    private String textview2;
    private String textview3;
//    private String textView4;
//    private String textView5;
    public ClubModel(){}
    public ClubModel(String imageview1, String textview1,String textview2,String textview3){
        this.imageview1 = imageview1;
        this.textview1 = textview1;
        this.textview2 = textview2;
        this.textview3 = textview3;
//        this.textview4 = textview4;
//        this.textview5 = textview5;
    }

    public String getImageview1() {
        return imageview1;
    }

    public String getTextview1() {
        return textview1;
    }

    public String getTextview2() {
        return textview2;
    }

    public String getTextview3() {
        return textview3;
    }
//    public String getTextview4() {
//        return textview4;
//    }
//    public String getTextview5() {
//        return textview5;
//    }


}




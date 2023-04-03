package com.example.susocial.Comment;

public class CommentModel {
    //Mingyan Zhang implemented the whole CommentModel file.
    private String rate;
    private String comment;

    private int layout;

    public CommentModel(){}
    //hdhd

    public CommentModel(String comment, String rate,int layout){
        this.rate = rate;
        this.comment = comment;
        this.layout = layout;
    }
    public String getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public int getLayout(){ return layout;}


}

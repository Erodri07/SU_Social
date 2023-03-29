package com.example.susocial;

public class MessageModel{
    String messageSend;
    String senderId;

    public MessageModel(){}

    public MessageModel(String messageSend,String senderId){
        this.messageSend = messageSend;
        this.senderId =senderId;
    }

    public void setMessage(String messageSend){
        this.messageSend = messageSend;

    }
    public void setSenderId(String senderId){
        this.senderId =senderId;
    }
    public String getMessageSend(){
        return  messageSend;
    }
    public String getSenderId(){
        return senderId;
    }

}

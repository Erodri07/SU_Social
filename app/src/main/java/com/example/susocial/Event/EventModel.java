package com.example.susocial.Event;

public class EventModel {
    private String eventImage;
    private String eventName;
    private String eventLocat;
    private String eventDate;
    private String eventTime;

    public EventModel(){}
    public EventModel(String eventImage,String eventName,String eventLocat,String eventDate,String eventTime){
        this.eventImage = eventImage;
        this.eventName = eventName;
        this.eventLocat = eventLocat;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public String getEventImage(){
        return eventImage;
    }
    public String getEventName(){
        return eventName;
    }
    public String getEventLocat(){
        return eventLocat;
    }
    public String getEventTime(){
        return eventTime;
    }
    public String getEventDate(){
        return eventDate;
    }


}

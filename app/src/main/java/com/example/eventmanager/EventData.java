package com.example.eventmanager;

public class EventData {
    private String fbId;
    private String eventName;
    private String event;
    private String eventDate;
    private String eventLocation;
    private String eventDescription;
    private String groupLink;

    public EventData() {
        // Default constructor
    }

    public EventData(String eventName, String event, String eventDate, String eventLocation, String eventDescription, String groupLink) {
        this.eventName = eventName;
        this.event = event;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.groupLink = groupLink;
    }

    // Getters
    public String getEventName() {
        return eventName;
    }

    public String getEvent() {
        return event;
    }

    public String getEventDate(){
        return eventDate;
    }

    public  String getEventLocation(){
        return eventLocation;
    }
    public String getEventDescription() {
        return eventDescription;
    }

    public String getGroupLink() {
        return groupLink;
    }

    public String getFbId(){ return fbId; }

    public void setFbId(String fbId){ this.fbId = fbId ;}
}
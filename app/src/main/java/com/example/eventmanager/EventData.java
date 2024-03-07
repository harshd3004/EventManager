package com.example.eventmanager;

public class EventData {

    private int id;
    private String eventName;
    private String event;
    private String eventDate;
    private String eventLocation;
    private String eventDescription;

    public EventData() {
        // Default constructor
    }

    public EventData(String eventName, String event, String eventDate, String eventLocation, String eventDescription) {
        this.eventName = eventName;
        this.event = event;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
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

    public String getDate() { return  eventDate;    }

    public String getLocation() { return eventLocation;    }

    public String getDescription() {  return eventDescription;    }
}


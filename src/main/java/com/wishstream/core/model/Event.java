package com.wishstream.core.model;

public class Event {

    private String date;
    private String event;
    private String notes;
    private String recurrence;
    private String type; // For "update/new"

    public Event(String event, String eventDate, String notes, String recurrence) {
    }

    public Event() {

    }

    // Getters and Setters

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

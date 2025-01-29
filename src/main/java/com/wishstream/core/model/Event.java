package com.wishstream.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_relation_id", nullable = false)
    private String userRelationId;

    @Column(name = "event_name", nullable = false)
    private String event;

    @Column(name = "event_date_utc", nullable = false)
    private LocalDateTime eventDateUtc;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "local_event_time", nullable = false)
    private LocalDateTime localEventTime;

    @Column(name = "recurrence", nullable = false)
    private String recurrence; // none, daily, weekly, monthly, yearly

    @Column(name = "type", nullable = false)
    private String type; // new, update

    @Column(name = "notes")
    private String notes;

    /**
     * Converts the given local date & time to UTC for storage.
     */
    public void setEventDateInUtc(LocalDateTime localDate, String timezone) {
        ZonedDateTime localZdt = ZonedDateTime.of(localDate, ZoneId.of(timezone));
        this.eventDateUtc = localZdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        this.timezone = timezone;
        this.localEventTime = localDate;
    }

    /**
     * Converts stored UTC event time back to user's local time.
     */
    public LocalDateTime getEventDateInLocalTime() {
        return eventDateUtc.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserRelationId() {
        return userRelationId;
    }

    public void setUserRelationId(String userRelationId) {
        this.userRelationId = userRelationId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public LocalDateTime getEventDateUtc() {
        return eventDateUtc;
    }

    public void setEventDateUtc(LocalDateTime eventDateUtc) {
        this.eventDateUtc = eventDateUtc;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public LocalDateTime getLocalEventTime() {
        return localEventTime;
    }

    public void setLocalEventTime(LocalDateTime localEventTime) {
        this.localEventTime = localEventTime;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

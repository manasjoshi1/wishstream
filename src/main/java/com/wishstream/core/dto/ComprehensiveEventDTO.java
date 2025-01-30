package com.wishstream.core.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ComprehensiveEventDTO {
    // User fields
    private String userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPhone;
    private String userPhoneExtension;

    // UserRelation fields
    private String userRelationId;
    private String relationFirstName;
    private String relationLastName;
    private String relation;
    private String relationType;
    private String relationEmail;
    private String relationPhone;
    private String relationPhoneExtension;
    private String timezone;

    // Event fields
    private Long eventId;
    private String eventName;
    private LocalDateTime eventDateUtc;
    private String notes;
    private String status;

    //phone fields
    private String messageOnPhone;

    //Email fields
    private String subject;
    private String body;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhoneExtension() {
        return userPhoneExtension;
    }

    public void setUserPhoneExtension(String userPhoneExtension) {
        this.userPhoneExtension = userPhoneExtension;
    }

    public String getUserRelationId() {
        return userRelationId;
    }

    public void setUserRelationId(String userRelationId) {
        this.userRelationId = userRelationId;
    }

    public String getRelationFirstName() {
        return relationFirstName;
    }

    public void setRelationFirstName(String relationFirstName) {
        this.relationFirstName = relationFirstName;
    }

    public String getRelationLastName() {
        return relationLastName;
    }

    public void setRelationLastName(String relationLastName) {
        this.relationLastName = relationLastName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationEmail() {
        return relationEmail;
    }

    public void setRelationEmail(String relationEmail) {
        this.relationEmail = relationEmail;
    }

    public String getRelationPhone() {
        return relationPhone;
    }

    public void setRelationPhone(String relationPhone) {
        this.relationPhone = relationPhone;
    }

    public String getRelationPhoneExtension() {
        return relationPhoneExtension;
    }

    public void setRelationPhoneExtension(String relationPhoneExtension) {
        this.relationPhoneExtension = relationPhoneExtension;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getEventDateUtc() {
        return eventDateUtc;
    }

    public void setEventDateUtc(LocalDateTime eventDateUtc) {
        this.eventDateUtc = eventDateUtc;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageOnPhone() {
        return messageOnPhone;
    }

    public void setMessageOnPhone(String messageOnPhone) {
        this.messageOnPhone = messageOnPhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
